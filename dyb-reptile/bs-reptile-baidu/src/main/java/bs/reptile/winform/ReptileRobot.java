package bs.reptile.winform;

import bs.common.*;
import bs.reptile.winform.dto.*;
import bs.reptile.database.entity.Reptile;
import bs.reptile.database.mapper.ReptileMapper;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class ReptileRobot {
    //根配置
    ReptileRootConfig rootConfig = null;
    //缓存数据
    Map<String, Integer> reptileKeys = new HashMap<>();

    //加载配置信息
    public void loadConfigs(String rootConfigJson) {
        if (ComStr.isEmpty(rootConfigJson)) return;
        rootConfig = JSON.parseObject(rootConfigJson, ReptileRootConfig.class);
    }

    //执行抓取
    public void execute() {
        if (rootConfig == null) {
            ComLog.info("执行前请先加载配置信息");
            return;
        }
        execute(rootConfig.getConfigs(), null);
    }

    void execute(List<ReptileConfig> configs, ReptileConfig parent) {
        for (ReptileConfig cfg : configs) {
            if (parent != null) cfg.setParent(parent);//设置父级
            cfg.setResult(execute(cfg));//获取数据
            List<ReptileConfig> configsChild = cfg.getConfigs();//子集
            if (configsChild != null) execute(configsChild, cfg);
        }
    }

    //执行抓取，解析url参数模板
    List<Map<String, String>> execute(ReptileConfig config) {
        //返回结果
        List<Map<String, String>> maps = new ArrayList<>();
        //解析Url参数
        String urlTemplate = config.getUrl();
        ArrayList<ReptileUrlExpression> urlExpressions = getUrlFields(urlTemplate);
        //不存在表达式时，直接执行并返回
        if (urlExpressions.size() == 0) {
            maps.addAll(execute(config, urlTemplate));
            return maps;
        }
        //层级数去重
        HashSet<Integer> hsi = new HashSet<>();
        urlExpressions.stream().forEach(t -> {
            t.getFields().stream().forEach(x -> {
                hsi.add(x.getLevel());
            });
        });
        //读取各层结果集
        Map<String, List<Map<String, String>>> resultMap = new HashMap<>();
        for (Integer level : hsi.stream().sorted().toArray(Integer[]::new)) {
            ReptileConfig parent = config;
            StringBuilder sb = new StringBuilder();
            for (Integer i = 0; i < level; i++) {
                parent = parent.getParent();
                sb.append("parent.");
            }
            resultMap.put(sb.toString(), parent.getResult());
        }
        //循环执行
        ComMap.eachCall(resultMap, path -> {
            List<String> urls = new ArrayList<>();
            urls.add(urlTemplate);
            for (ReptileUrlExpression ue : urlExpressions) {
                List<ReptileUrlField> fields = ue.getFields();
                switch (fields.size()) {
                    case 1:
                        String val = getUrlFieldValue(path, fields.get(0));
                        for (Integer i = 0; i < urls.size(); i++) {
                            String url = urls.get(i);
                            urls.set(i, url.replace(ue.getSearch(), val));
                        }
                        break;
                    case 2:
                        ReptileUrlField ufMin = fields.get(0);
                        ReptileUrlField ufMax = fields.get(1);
                        String minStr = getUrlFieldValue(path, ufMin);
                        String maxStr = getUrlFieldValue(path, ufMax);
                        Integer min = Integer.parseInt(ComStr.isEmpty(minStr) ? ufMin.getName() : minStr);
                        Integer max = Integer.parseInt(ComStr.isEmpty(minStr) ? ufMax.getName() : maxStr);
                        Integer lenFor = urls.size();
                        for (Integer i = 0; i < lenFor; i++) {
                            String url = urls.get(i);
                            urls.set(i, url.replace(ue.getSearch(), min.toString()));
                            for (Integer j = min + 1; j <= max; j++) {
                                urls.add(url.replace(ue.getSearch(), j.toString()));
                            }
                        }
                        break;
                }
            }
            for (String url : urls) {
                maps.addAll(execute(config, url));
            }
        });
        return maps;
    }

    //读取Url字段值
    String getUrlFieldValue(Map<String, Map<String, String>> path, ReptileUrlField field) {
        Map<String, String> info = path.get(field.getParent());
        if (info == null) return "";
        String val = info.get(field.getName());
        if (val == null) return "";
        return val;
    }


    //读取所有url模板中的表达式
    ArrayList<ReptileUrlExpression> getUrlFields(String urlTemplate) {
        Matcher matcher = regUrlField.matcher(urlTemplate);
        ArrayList<ReptileUrlExpression> ues = new ArrayList<>();

        //循环所有模板字段
        while (matcher.find()) {
            ReptileUrlExpression ue = new ReptileUrlExpression();
            //原匹配字符串
            String match = matcher.group(1);
            ue.setSearch("${" + match + "}");
            //匹配字段列表
            ArrayList<ReptileUrlField> ufs = new ArrayList<>();
            String[] fields = match.split(",");//字段
            for (int i = 0; i < fields.length; i++) {
                String field = fields[i];
                String[] fns = field.split("\\.");
                int len = fns.length, level = len - 1;
                String name = fns[len - 1], parent;
                if (len < 2) {
                    parent = "parent.";
                    level = 1;
                } else {
                    parent = field.replace("." + name, "") + ".";
                }
                //保存
                ReptileUrlField uf = new ReptileUrlField();
                uf.setParent(parent);
                uf.setName(name);
                uf.setLevel(level);
                ufs.add(uf);
            }
            ue.setFields(ufs);
            ues.add(ue);
        }
        return ues;
    }

    Pattern regUrlField = Pattern.compile("\\$\\{([^}]+)\\}");


    List<Map<String, String>> execute(ReptileConfig config, String url) {
        List<Map<String, String>> maps = new ArrayList<>();
        if (!config.isSave()) return maps;
        try {
            String html = ComHttp.request(url, config.getMethod(), config.getParams());
            Document htmDom = ComHtml.Load(html);

            //读取列表项
            String selItems = config.getSelect();
            Elements elements;
            if (ComStr.isEmpty(selItems)) {
                elements = new Elements();
                elements.add(htmDom);
            } else {
                elements = htmDom.select(config.getSelect());
            }
            for (Element emt : elements) {
                Map<String, String> fieldmap = new HashMap<>();
                ComLog.debug("#列表项#######################################################\n");
                for (ReptileField field : config.getFields()) {
                    if (ComStr.isEmpty(field.getSelect())) {
                        setFieldMap(fieldmap, emt, field);
                        ComLog.debug("字段:" + field.getRemark() + ":" + fieldmap.get(field.getName()) + "\n");
                        continue;
                    }
                    Element select = emt.select(field.getSelect()).first();
                    if (select == null) {
                        fieldmap.put(field.getName(), "未找到内容");
                        ComLog.debug("字段:" + field.getRemark() + ":未找到选择器\n" + emt.html() + "\n");
                        continue;
                    }
                    setFieldMap(fieldmap, select, field);
                    ComLog.debug("字段:" + field.getRemark() + ":" + fieldmap.get(field.getName()) + "\n");
                }
                maps.add(fieldmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }

    //设置字段值
    private void setFieldMap(Map<String, String> map, Element emt, ReptileField field) {
        String value, attrName = field.getSelectAttr();
        switch (attrName) {
            case "html":
                value = emt.html();
                break;
            case "text":
                value = emt.text();
                break;
            case "class":
                value = emt.className();
                break;
            default:
                value = emt.attr(attrName);
                break;
        }
        map.put(field.getName(), ComStr.isEmpty(value) ? "" : value);
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
