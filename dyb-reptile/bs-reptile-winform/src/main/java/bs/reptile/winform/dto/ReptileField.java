package bs.reptile.winform.dto;

import lombok.Data;

import java.util.List;

//字段信息
@Data
public class ReptileField {
    //字段名称
    private String name;
    //字段说明
    private String remark;
    //值(父字段或当前抓取属性，为空则使用select属性读取)
    private String value;
    //CSS选择器(默认根对象)
    private String select;
    //对象属性名称(默认为text)
    private String selectAttr;
    //查找替换正则
    private List<ReptileReplace> replaces;
}