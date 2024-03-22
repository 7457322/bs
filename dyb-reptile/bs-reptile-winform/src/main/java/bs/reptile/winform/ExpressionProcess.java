package bs.reptile.winform;

import bs.common.ComMap;
import bs.common.ComStr;
import bs.common.lambda.Func;
import bs.reptile.winform.dto.*;
import lombok.Data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 表达式处理
 *
 * @author 冰鼠
 * @version 1.0.1
 * @date 2024/3/1 13:43
 * @package bs.reptile.winform
 * @copyright 冰鼠
 */
@Data
public class ExpressionProcess {
    /**
     * 解析模板中的所有表达式
     *
     * @param urlTemplate 模板
     * @return 已解析的表达式
     */
    public static List<String> readValues(String urlTemplate, List<ReptileExpression> expressions, Map<String, Map<String, String>> context) {
        List<Map<String, String>> mapValues = readValues(expressions, context);
        List<String> results = replaceTemplate(urlTemplate, mapValues);
        return results;
    }

    /**
     * 读取所有表达式对应值（一般情况下只会产生一条，但有些表达式可能会产生多条记录，所以返回结果是列表）
     *
     * @param expressions
     * @param context
     * @return 多条 所有表达式对应值
     */
    public static List<Map<String, String>> readValues(List<ReptileExpression> expressions, Map<String, Map<String, String>> context) {
        List<Map<String, String>> results = new ArrayList<>();
        results.add(new HashMap<>());//默认返回1条结果
        for (ReptileExpression ue : expressions) {
            List<ReptileExpressionField> fields = ue.getFields();
            Integer lenFor = results.size();
            switch (fields.size()) {
                case 1:
                    String val = getFieldValue(context, fields.get(0));
                    for (Integer i = 0; i < lenFor; i++) {
                        Map<String, String> result = results.get(i);
                        result.put(ue.getSearch(), val);
                    }
                    break;
                case 2:
                    //分页数量，会生成多条结果
                    ReptileExpressionField ufMin = fields.get(0);
                    ReptileExpressionField ufMax = fields.get(1);
                    String minStr = getFieldValue(context, ufMin);
                    String maxStr = getFieldValue(context, ufMax);
                    Integer min = Integer.parseInt(ComStr.isEmpty(minStr) ? ufMin.getName() : minStr);
                    Integer max = Integer.parseInt(ComStr.isEmpty(minStr) ? ufMax.getName() : maxStr);
                    for (Integer i = 0; i < lenFor; i++) {
                        Map<String, String> result = results.get(i);
                        result.put(ue.getSearch(), min.toString());
                        for (Integer j = min + 1; j <= max; j++) {
                            Map<String, String> copyMap = ComMap.copy(result);
                            copyMap.put(ue.getSearch(), j.toString());
                            results.add(copyMap);
                        }
                    }
                    break;
            }
        }
        return results;
    }

    //读取Url字段值
    static String getFieldValue(Map<String, Map<String, String>> context, ReptileExpressionField field) {
        Map<String, String> info = context.get(field.getParent());
        if (info == null) return "";
        String val = info.get(field.getName());
        if (val == null) return "";
        return val;
    }


    /**
     * 替换带表达式的模板
     *
     * @param template    模板字符串
     * @param listContext 上下文列表（字典列表）
     * @return 替换结果
     */
    public static List<String> replaceTemplate(String template, List<Map<String, String>> listContext) {
        Matcher matcher = regUrlField.matcher(template);
        List<String> rsts = listContext.stream()
                .map(x -> ComStr.replace(matcher, template, t -> x.get(t.group(0))))
                .collect(Collectors.toList());
        return rsts;
    }

    /**
     * 解析配置字段中的所有表达式
     *
     * @param fields    字段列表
     * @param defPrefix 默认前缀(默认的对象)
     * @return 已解析的表达式
     */
    public static ArrayList<ReptileExpression> parseConfigFields(List<ReptileField> fields, String defPrefix) {
        StringBuilder template = new StringBuilder();
        for (ReptileField field : fields) template.append(field.getValue());
        ArrayList<ReptileExpression> expressions = parseTemplate(template.toString(), defPrefix);
        return expressions;
    }

    /**
     * 解析模板中的所有表达式
     *
     * @param template  模板
     * @param defPrefix 默认前缀(默认的对象)
     * @return 已解析的表达式
     */
    public static ArrayList<ReptileExpression> parseTemplate(String template, String defPrefix) {
        Matcher matcher = regUrlField.matcher(template);
        ArrayList<String> ues = new ArrayList<>();
        //循环所有模板字段
        while (matcher.find()) {
            String match = matcher.group(1);
            ues.add(match);
        }
        ArrayList<ReptileExpression> expressions = parseList(ues, defPrefix);
        return expressions;
    }

    public final static Pattern regUrlField = Pattern.compile("\\$\\{([^}]+)\\}");

    /**
     * 解析所有表达式
     *
     * @param expressions 表达式列表
     * @param defPrefix   默认前端
     * @return 已解析的表达式
     */
    public static ArrayList<ReptileExpression> parseList(List<String> expressions, String defPrefix) {
        ArrayList<ReptileExpression> ues = new ArrayList<>();
        HashSet<String> mapSearchStr = new HashSet<>();
        //循环所有模板字段
        for (String expression : expressions) {
            //去重相同的表达式
            if (mapSearchStr.contains(expression)) continue;
            else mapSearchStr.add(expression);
            ReptileExpression ue = parse(expression, defPrefix);
            ues.add(ue);
        }
        return ues;
    }

    /**
     * 解析单个表达式，特殊地方：defPrefix=“parent.”时，默认层级为1，否则为0
     *
     * @param expression 表达式
     * @param defPrefix  默认前缀
     * @return 表达式对象
     */
    static ReptileExpression parse(String expression, String defPrefix) {
        ReptileExpression ue = new ReptileExpression();
        ue.setSearch("${" + expression + "}");
        String parentPrefix = "parent.";
        int defLevel = defPrefix.equals(parentPrefix) ? 1 : 0;
        //匹配字段列表
        ArrayList<ReptileExpressionField> ufs = new ArrayList<>();
        String[] fields = expression.split(",");//字段
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            String[] fns = field.split("\\.");
            int len = fns.length, level = len - 1;
            String name = fns[level], parent;
            if (level == 0) {
                parent = defPrefix;
                level = defLevel;
            } else if (level == 1) {
                parent = field.replace("." + name, "") + ".";
                if (!parent.equals(parentPrefix)) level = 0;
            } else {
                parent = field.replace("." + name, "") + ".";
            }
            //保存
            ReptileExpressionField uf = new ReptileExpressionField();
            uf.setParent(parent);
            uf.setName(name);
            uf.setLevel(level);
            ufs.add(uf);
        }
        ue.setFields(ufs);
        return ue;
    }

    /**
     * 获取表达式列表层级
     *
     * @param expressions 表达式列表
     * @return 层级列表
     */
    public static HashSet<Integer> getLevels(List<ReptileExpression> expressions) {
        //层级数去重
        HashSet<Integer> levels = new HashSet<>();
        expressions.stream().forEach(t -> {
            t.getFields().stream().forEach(x -> {
                levels.add(x.getLevel());
            });
        });
        return levels;
    }
}
