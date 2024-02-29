package bs.reptile.winform.dto;

import bs.common.ComStr;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReptileConfig {
    //名称
    private String name;
    //是否将结果保存
    private boolean isSave;
    //请求网址：
    // 读取父结果字段：${parent.字段名称}或${字段名称}
    // 读取父父结果字段：${parent.parent.字段名称}
    // 生成网址集合，第1个字段名称为起始数值，第2个字段名称为结束数值：${字段名称,字段名称}
    private String url;
    //请求方式：get/post/put/delete
    private String method;
    //结果类型：html/xml/json
    private String resultType;
    //提交参数
    private String params;
    //列表项选择器
    private String select;
    //字段列表
    private List<ReptileField> fields;
    //抓取结果
    private List<Map<String, String>> result;
    //子配置列表
    private List<ReptileConfig> configs;
    //父级
    private ReptileConfig parent;

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
