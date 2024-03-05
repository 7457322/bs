package bs.reptile.winform;

import bs.common.*;
import bs.reptile.database.entity.Reptile;
import bs.reptile.database.mapper.ReptileMapper;
import bs.reptile.winform.dto.ReptileConfig;
import bs.reptile.winform.dto.ReptileExpression;
import bs.reptile.winform.dto.ReptileField;
import bs.reptile.winform.dto.ReptileRootConfig;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.*;

/**
 * 爬虫机器人
 *
 * @author 冰鼠
 * @version 1.0.1
 * @date 2024/3/1 13:43
 * @package bs.reptile.winform
 * @copyright 冰鼠
 */
@Data
public class ReptileRobot {
    /**
     * 根配置
     */
    ReptileRootConfig rootConfig = null;
    //缓存数据
    Map<String, Integer> reptileKeys = new HashMap<>();

    /**
     * 加载配置信息
     *
     * @param rootConfigJson 根节点配置
     */
    public void loadConfigs(String rootConfigJson) {
        if (ComStr.isEmpty(rootConfigJson)) return;
        rootConfig = JSON.parseObject(rootConfigJson, ReptileRootConfig.class);
        initConfig(rootConfig.getConfigs(), null);
    }

    /**
     * 递归初使化配置
     *
     * @param configs 配置列表
     */
    void initConfig(List<ReptileConfig> configs, ReptileConfig parent) {
        for (ReptileConfig config : configs) {
            config.setParent(parent);//父级

            //解析爬取字段和继承字段
            List<ReptileField> inheritFields = new ArrayList<>();
            List<ReptileField> parseFields = new ArrayList<>();
            config.setInheritFields(inheritFields);
            config.setParseFields(parseFields);
            for (ReptileField field : config.getFields()) {
                String inheritField = field.getValue();
                if (ComStr.isEmpty(inheritField)) {
                    parseFields.add(field);
                } else {
                    inheritFields.add(field);
                }
            }

            //递归执行
            List<ReptileConfig> childConfigs = config.getConfigs();
            if (childConfigs == null || childConfigs.size() == 0) continue;
            initConfig(config.getConfigs(), config);
        }
    }

    /**
     * 执行爬虫
     */
    public void execute() {
        if (rootConfig == null) {
            ComLog.info("执行前请先加载配置信息");
            return;
        }
        execute(rootConfig.getConfigs());
    }

    /**
     * 执行爬虫，递归执行配置树
     *
     * @param configs 配置列表
     */
    void execute(List<ReptileConfig> configs) {
        for (ReptileConfig cfg : configs) {
            cfg.setResult(execute(cfg));//获取数据
            List<ReptileConfig> childConfigs = cfg.getConfigs();//子集
            if (childConfigs != null) execute(childConfigs);
        }
    }

    /**
     * 执行爬虫，执行单个配置
     *
     * @param config 配置信息
     * @return 爬取结果
     */
    List<Map<String, String>> execute(ReptileConfig config) {
        //返回结果
        List<Map<String, String>> parseResult = new ArrayList<>();
        //解析Url表达式
        String urlTemplate = config.getUrl();
        ArrayList<ReptileExpression> urlExpressions = ExpressionProcess.parseTemplate(urlTemplate, "parent.");
        //解析配置字段
        ArrayList<ReptileExpression> fieldExpressions = ExpressionProcess.parseConfigFields(config.getInheritFields(), "self.");
        //读取Url对应的层级数去重列表
        HashSet<Integer> urlLevels = ExpressionProcess.getLevels(urlExpressions);
        //读取Url对应的父层结果集
        Map<String, List<Map<String, String>>> urlParentResults = getParentResults(config, urlLevels);
        //读取配置字段对应的层级数去重列表
        HashSet<Integer> fieldLevels = ExpressionProcess.getLevels(fieldExpressions);
        for (Integer i : urlLevels) fieldLevels.remove(i);
        //读取配置字段对应的父层结果集
        Map<String, List<Map<String, String>>> fieldParentResults = getParentResults(config, fieldLevels);

        //不存在Url表达式时，直接执行并返回
        if (urlExpressions.size() == 0) {
            parseResult.addAll(loadData(config, urlTemplate, new HashMap<>()));
        } else {
            //循环加载url并解析爬取数据
            ComMap.eachCall(urlParentResults, urlContext -> {
                List<String> urls = ExpressionProcess.readValues(urlTemplate, urlExpressions, urlContext);
                for (String url : urls) {
                    parseResult.addAll(loadData(config, url, urlContext));
                }
            });
        }

        //循环组合继承字段并生成继承数据（继承字段存在变量和常量需要解析）
        List<Map<String, String>> result = new ArrayList<>();
        ComMap.eachCall(fieldParentResults, fieldsContext -> {
            List<Map<String, String>> resetValuesList = ExpressionProcess.readValues(fieldExpressions, fieldsContext);
            //所有爬取的结果记录
            for (Map<String, String> row : parseResult) {
                //所有表达式组合的结果记录
                for (Map<String, String> resetValues : resetValuesList) {
                    Map<String, String> copy = ComMap.copy(row);
                    result.add(copy);
                    //替换每个字段模板中变量值
                    for (ReptileField field : config.getInheritFields()) {
                        String val = ComStr.replace(ExpressionProcess.regUrlField, field.getValue(), t -> resetValues.get(t.group(0)));
                        copy.put(field.getName(), val);
                    }
                }
            }
        });
        return result;
    }


    /**
     * 读取父层的结果集
     *
     * @param config 配置对象
     * @param levels 层级数（从当前层级0计算，父级为1，父父级为2）
     * @return 结果集
     */
    Map<String, List<Map<String, String>>> getParentResults(ReptileConfig config, HashSet<Integer> levels) {
        Map<String, List<Map<String, String>>> parentResults = new HashMap<>();
        for (Integer level : levels.stream().sorted().toArray(Integer[]::new)) {
            if (level == 0) {
                parentResults.put("self.", new ArrayList<>());
                continue;
            }
            ReptileConfig parent = config;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < level; i++) {
                parent = parent.getParent();
                sb.append("parent.");
            }
            List<Map<String, String>> result = parent.getResult();
            if (result == null) continue;
            parentResults.put(sb.toString(), result);
        }
        return parentResults;
    }

    List<Map<String, String>> loadData(
            ReptileConfig config,
            String url,
            Map<String, Map<String, String>> urlContext) {
        List<Map<String, String>> maps = new ArrayList<>();
        try {
            String html = ComHttp.request(url, config.getMethod(), config.getParams());
            switch (config.getResultType()) {
                case "json":
                    break;
                case "xml":
                    break;
                case "html":
                default:
                    maps = HtmlProcess.readValues(config, html, urlContext);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }

    public void loadCache() {
        reptileKeys.clear();
        List<Map<String, Object>> maps = ComDb.executeSql("SELECT id,link FROM bs_reptile WHERE source='" + rootConfig.getSource().replace("'", "''") + "'");
        for (Map<String, Object> map : maps) {
            reptileKeys.put(map.get("link").toString(), (Integer) map.get("id"));
        }
    }

    //保存数据
    public void save() {
        if (rootConfig == null) {
            ComLog.info("保存前请先加载配置信息");
            return;
        }
        save(rootConfig.getConfigs());
    }

    void save(List<ReptileConfig> configs) {
        for (ReptileConfig cfg : configs) {
            save(cfg);
            List<ReptileConfig> configsChild = cfg.getConfigs();
            if (configsChild != null) save(configsChild);
        }
    }

    void save(ReptileConfig config) {
        if (!config.isSave() || config.getResult() == null || config.getResult().size() == 0) return;
        ComDb.run(sqlSession -> {
            ReptileMapper mapper = sqlSession.getMapper(ReptileMapper.class);
            String source = rootConfig.getSource();
            Date now = new Date();
            for (Map<String, String> map : config.getResult()) {
                Reptile reptile = new Reptile();
                reptile.setSource(source);
                reptile.setCreateTime(now);
                ComMap.toObject(map, reptile);
                if (reptileKeys.containsKey(reptile.getLink())) {
                    reptile.setId(reptileKeys.get(reptile.getLink()));
                    mapper.updateById(reptile);
                } else {
                    mapper.insert(reptile);
                }
            }
        });
    }
}
