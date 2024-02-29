package bs.reptile.winform.dto;

import lombok.Data;

import java.util.List;

//字段信息
@Data
public class ReptileUrlExpression {
    //匹配字符串
    private String search;
    //字段列表
    private  List<ReptileUrlField> fields;
}