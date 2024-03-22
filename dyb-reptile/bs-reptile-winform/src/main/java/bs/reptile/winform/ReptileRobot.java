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
import java.util.regex.Matcher;
import java.util.stream.Collectors;

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
    }

    /**
     * 初使化配置
     */
    void initConfigs() {
        initConfigs(rootConfig.getConfigs(), null);
    }

    /**
     * 递归初使化配置
     *
     * @param configs 配置列表
     */
    void initConfigs(List<ReptileConfig> configs, ReptileConfig parent) {
        for (ReptileConfig config : configs) {
            initConfig(config, parent);
            //递归执行
            List<ReptileConfig> childConfigs = config.getConfigs();
            if (childConfigs == null || childConfigs.size() == 0) continue;
            initConfigs(config.getConfigs(), config);
        }
    }

    //解析单个配置
    void initConfig(ReptileConfig config, ReptileConfig parent) {
        if (config.isInit()) return;
        config.setInit(true);
        config.setParent(parent);//父级

        //解析Url表达式
        String urlTemplate = config.getUrl();
        ArrayList<ReptileExpression> urlExpressions = ExpressionProcess.parseTemplate(urlTemplate, "parent.");
        config.setUrlExpressions(urlExpressions);

        //读取Url对应的层级数去重列表
        HashSet<Integer> urlLevels = ExpressionProcess.getLevels(urlExpressions);
        config.setUrlLevels(urlLevels);

        //解析爬取字段和继承字段
        List<ReptileField> selectFields = new ArrayList<>();
        List<ReptileField> constFields = new ArrayList<>();
        List<ReptileField> urlFields = new ArrayList<>();
        List<ReptileField> unUrlFields = new ArrayList<>();
        List<ReptileExpression> urlFieldExpressions = new ArrayList<>();
        List<ReptileExpression> unUrlFieldExpressions = new ArrayList<>();
        config.setSelectFields(selectFields);
        config.setUrlFields(urlFields);
        config.setUnUrlFields(unUrlFields);
        config.setUrlFieldExpressions(urlFieldExpressions);
        config.setUnUrlFieldExpressions(unUrlFieldExpressions);
        for (ReptileField field : config.getFields()) {
            //是否为选择器
            String expressionField = field.getValue();
            if (ComStr.isEmpty(expressionField)) {
                selectFields.add(field);
                continue;
            }
            //是否为常量
            ArrayList<ReptileExpression> expressions = ExpressionProcess.parseTemplate(expressionField, "self.");
            if (expressions.size() == 0) {
                constFields.add(field);
                continue;
            }
            //是否为Url字段
            HashSet<Integer> fieldLevels = ExpressionProcess.getLevels(expressions);
            if (fieldLevels.stream().allMatch(t -> urlLevels.contains(t))) {
                urlFields.add(field);
                urlFieldExpressions.addAll(expressions);
            } else {
                unUrlFields.add(field);
                unUrlFieldExpressions.addAll(expressions);
            }
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
        List<ReptileExpression> urlExpressions = config.getUrlExpressions();

        //不存在Url表达式时，直接执行并返回
        if (urlExpressions.size() == 0) {
            parseResult.addAll(loadData(config, urlTemplate, new HashMap<>(), new HashMap<>()));
        } else {
            //读取Url对应的父层上下文字典
            Map<String, List<Map<String, String>>> urlContexts = getParentResults(config, config.getUrlLevels());
            //读取配置字段对应的层级数去重列表
            HashSet<Integer> fieldLevels = ExpressionProcess.getLevels(config.getUnUrlFieldExpressions());
            //读取配置字段对应的父层结果集
            Map<String, List<Map<String, String>>> unUrlFieldContexts = getParentResults(config, fieldLevels);
            //循环加载url并解析爬取数据
            ComMap.eachCall(urlContexts, urlContext -> {
                List<String> urls = ExpressionProcess.readValues(urlTemplate, urlExpressions, urlContext);
                for (String url : urls) {
                    parseResult.addAll(loadData(config, url, urlContext, unUrlFieldContexts));
                }
            });
        }
        return parseResult;
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
                ArrayList<Map<String, String>> selfList = new ArrayList<>();
                selfList.add(new HashMap<>());
                parentResults.put("self.", selfList);
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

    List<Map<String, String>> loadData(ReptileConfig config, String url) {
        return loadData(config, url, new HashMap<>(), new HashMap<>());
    }

    List<Map<String, String>> loadData(
            ReptileConfig config, String url,
            Map<String, Map<String, String>> urlContext,
            Map<String, List<Map<String, String>>> unUrlContexts) {
        //创建Url表达式产生的上下文记录
        List<Map<String, String>> urlFieldValues = ExpressionProcess.readValues(config.getUrlFieldExpressions(), urlContext);
        //创建Url表达式产生的结果记录
        List<Map<String, String>> urlRecords = new ArrayList<>();
        urlFieldValues.forEach(ctx -> {
            HashMap<String, String> map = new HashMap<>();
            config.getUrlFields().forEach(t -> {
                map.put(t.getName(), ComStr.replace(ExpressionProcess.regUrlField, t.getValue(), m -> ctx.get(m.group(0))));
            });
            urlRecords.add(map);
        });
        //更新常量到结果记录中
        if (config.getConstFields() != null)
            urlRecords.forEach(m -> config.getConstFields().forEach(t -> m.put(t.getName(), t.getValue())));
        //创建选择器字段产生的结果记录
        List<Map<String, String>> selectRecords = new ArrayList<>();
        try {
            String text = ComHttp.request(url, config.getMethod(), config.getParams());
            switch (config.getResultType()) {
                case "json":
                    break;
                case "xml":
                    break;
                case "html":
                default:
                    selectRecords = HtmlProcess.readValues(config, text, urlContext);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //组合 选择器字段产生的结果记录 和 表达式产生的结果记录
        List<Map<String, String>> resultxs = new ArrayList<>();
        List<Map<String, String>> finalSelectRecords = selectRecords;
        urlRecords.forEach(ur -> {
            finalSelectRecords.forEach(uur -> {
                Map<String, String> map = new HashMap<>();
                map.putAll(ur);
                map.putAll(uur);
                resultxs.add(map);
            });
        });
        if (null == unUrlContexts || unUrlContexts.size() == 0) return resultxs;

        //设置当前请求属性
        HashMap<String, String> mapSelf = new HashMap<>();
        mapSelf.put("url", url);
        //创建非Url表达式产生的结果记录
        List<Map<String, String>> unUrlRecords = new ArrayList<>();
        //循环加载非url字段各层组合列表
        ComMap.eachCall(unUrlContexts, ctx -> {
            ctx.put("self.", mapSelf);
            //读取非url字段表达式和其值的列表
            List<Map<String, String>> unUrlFieldExpressionValues = ExpressionProcess.readValues(config.getUnUrlFieldExpressions(), ctx);
            unUrlFieldExpressionValues.forEach(unUrlMap -> {
                //添加字段
                HashMap<String, String> map = new HashMap<>();
                config.getUnUrlFields().forEach(f -> {
                    map.put(f.getName(), ComStr.replace(ExpressionProcess.regUrlField, f.getValue(), m -> unUrlMap.get(m.group(0))));
                });
                unUrlRecords.add(map);
            });
            ctx.remove("self.");
        });

        //组合 非Url字段的结果记录 和 url字段的结果记录
        List<Map<String, String>> results = new ArrayList<>();
        unUrlRecords.forEach(uur -> {
            resultxs.forEach(ur -> {
                Map<String, String> map = new HashMap<>();
                map.putAll(ur);
                map.putAll(uur);
                results.add(map);
            });
        });

        return results;
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
                if(reptile.getLink().length()>750){
                    System.out.println(reptile.getLink().length());
                }
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
