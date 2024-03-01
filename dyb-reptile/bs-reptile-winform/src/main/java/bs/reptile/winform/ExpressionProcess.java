package bs.reptile.winform;

import bs.common.ComStr;
import bs.reptile.winform.dto.*;
import lombok.Data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * @param defPrefix   默认前端
     * @return 已解析的表达式
     */
    public static ArrayList<Map<String, String>> readValues(List<ReptileExpression> expressions, Map<String, Map<String, String>> context) {
        ArrayList<Map<String, String>> mapValues=new ArrayList<>();
        List<String> urls = new ArrayList<>();
        urls.add(urlTemplate);
        for (ReptileExpression ue : expressions) {
            List<ReptileExpressionField> fields = ue.getFields();
            switch (fields.size()) {
                case 1:
                    String val = getUrlFieldValue(context, fields.get(0));
                    for (Integer i = 0; i < urls.size(); i++) {
                        String url = urls.get(i);
                        urls.set(i, url.replace(ue.getSearch(), val));
                    }
                    break;
                case 2:
                    ReptileExpressionField ufMin = fields.get(0);
                    ReptileExpressionField ufMax = fields.get(1);
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
    }

    //读取Url字段值
    static String getUrlFieldValue(Map<String, Map<String, String>> path, ReptileExpressionField field) {
        Map<String, String> info = path.get(field.getParent());
        if (info == null) return "";
        String val = info.get(field.getName());
        if (val == null) return "";
        return val;
    }

    /**
     * 解析模板中的所有表达式
     *
     * @param urlTemplate 模板
     * @param defPrefix   默认前端
     * @return 已解析的表达式
     */
    public static ArrayList<ReptileExpression> parseTemplate(String urlTemplate, String defPrefix) {
        Matcher matcher = regUrlField.matcher(urlTemplate);
        ArrayList<String> ues = new ArrayList<>();
        //循环所有模板字段
        while (matcher.find()) {
            String match = matcher.group(1);
            ues.add(match);
        }
        ArrayList<ReptileExpression> expressions = parseList(ues, defPrefix);
        return expressions;
    }

    static Pattern regUrlField = Pattern.compile("\\$\\{([^}]+)\\}");

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
     * 解析单个表达式
     *
     * @param expression 表达式
     * @param defPrefix  默认前缀
     * @return 表达式对象
     */
    static ReptileExpression parse(String expression, String defPrefix) {
        ReptileExpression ue = new ReptileExpression();
        ue.setSearch("${" + expression + "}");
        //匹配字段列表
        ArrayList<ReptileExpressionField> ufs = new ArrayList<>();
        String[] fields = expression.split(",");//字段
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            String[] fns = field.split("\\.");
            int len = fns.length, level = len - 1;
            String name = fns[len - 1], parent;
            if (len < 2) {
                parent = defPrefix;
                level = 1;
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
}
