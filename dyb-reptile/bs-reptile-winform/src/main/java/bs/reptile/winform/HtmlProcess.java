package bs.reptile.winform;

import bs.common.ComHtml;
import bs.common.ComLog;
import bs.common.ComStr;
import bs.reptile.winform.dto.ReptileConfig;
import bs.reptile.winform.dto.ReptileExpressionField;
import bs.reptile.winform.dto.ReptileField;
import lombok.Data;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class HtmlProcess {

    /**
     * 读取结果集
     *
     * @param config  配置
     * @param result  请求结果
     * @param context 上下文数据
     * @return 结果集
     */
    static List<Map<String, String>> readValues(
            ReptileConfig config,
            String result,
            Map<String, Map<String, String>> context) {
        List<Map<String, String>> maps = new ArrayList<>();
        try {
            Document htmDom = ComHtml.Load(result);

            //读取列表项
            String selItems = config.getSelect();
            Elements elements;
            if (ComStr.isEmpty(selItems)) {
                elements = new Elements();
                elements.add(htmDom);
            } else {
                elements = htmDom.select(config.getSelect());
            }
            //解析列表项
            for (Element emt : elements) {
                Map<String, String> fieldmap = new HashMap<>();
                ComLog.debug("#列表项#######################################################\n");
                for (ReptileField field : config.getSelectFields()) {
                    Element select;
                    if (ComStr.isEmpty(field.getSelect())) {
                        select = emt;
                    } else {
                        select = emt.select(field.getSelect()).first();
                        if (select == null) {
                            fieldmap.put(field.getName(), "");
                            ComLog.debug("字段:" + field.getRemark() + ":\n" + emt.html() + "\n");
                            continue;
                        }
                    }
                    String val = getFieldValue(select, field);
                    fieldmap.put(field.getName(), val);
                    ComLog.debug("字段:" + field.getRemark() + ":" + fieldmap.get(field.getName()) + "\n");
                }
                maps.add(fieldmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }

    /**
     * 获取字段对应值
     *
     * @param emt   Html对象
     * @param field 字段对象
     * @return 字符串
     */
    static String getFieldValue(Element emt, ReptileField field) {
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
        return ComStr.isEmpty(value) ? "" : value;
    }

}
