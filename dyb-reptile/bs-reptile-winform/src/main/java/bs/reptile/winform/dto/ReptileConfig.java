package bs.reptile.winform.dto;

import bs.common.ComStr;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 爬虫配置
 *
 * @author 冰鼠
 * @version 1.0.1
 * @date 2024/3/1 13:43
 * @package bs.reptile.winform.dto
 * @copyright 冰鼠
 */
@Data
public class ReptileConfig {
    //名称
    private String name;
    //是否已经初使化
    private boolean isInit;
    //是否需要保存结果
    private boolean isSave;
    //请求网址：
    // 读取父结果字段：${parent.字段名称}或${字段名称}
    // 读取父父结果字段：${parent.parent.字段名称}
    // 生成网址集合，第1个字段名称为起始数值，第2个字段名称为结束数值：${字段名称,字段名称}
    private String url;
    //请求方式：get/post/put/delete
    private String method = "get";
    //结果类型：html/xml/json
    private String resultType = "html";
    //提交参数
    private String params;
    //列表项选择器
    private String select;
    //字段列表
    private List<ReptileField> fields;
    //子配置列表
    private List<ReptileConfig> configs;
    //父级(初使化产生)
    private ReptileConfig parent;
    //url表达式(初使化产生)
    private List<ReptileExpression> urlExpressions;
    //url使用到的父层级数列表(初使化产生)
    private HashSet<Integer> urlLevels;
    //常量字段列表(初使化产生)
    private List<ReptileField> constFields;
    //Url字段列表(初使化产生)
    private List<ReptileField> selectFields;
    //Url字段列表(初使化产生)
    private List<ReptileField> urlFields;
    //非Url字段列表(初使化产生)
    private List<ReptileField> unUrlFields;
    //Url字段表达式列表(初使化产生)
    private List<ReptileExpression> urlFieldExpressions;
    //非Url字段表达式列表(初使化产生)
    private List<ReptileExpression> unUrlFieldExpressions;
    //抓取结果(初使化产生)
    private List<Map<String, String>> result;

    //构造函数
    public ReptileConfig() {
        this("");
    }

    //构造函数
    public ReptileConfig(String name) {
        this(name, "", "", null);
    }

    //构造函数
    public ReptileConfig(String name, String url, String select, List<ReptileField> fields) {
        this(name, url, select, fields, true);
    }

    //构造函数
    public ReptileConfig(String name, String url, String select, List<ReptileField> fields, boolean isSave) {
        this.name = name;
        this.url = url;
        this.select = select;
        this.fields = fields;
        Boolean hasValue = !(ComStr.isEmpty(name) || ComStr.isEmpty(url) || ComStr.isEmpty(select) || fields == null || fields.size() == 0);
        this.isSave = hasValue ? isSave : false;
        result = null;
        configs = null;
    }

    //默认输出
    public String toString() {
        return name;
    }
}
