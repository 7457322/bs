package bs.reptile.winform.dto;

import lombok.Data;

@Data
public class ReptileExpressionField {
    //字段名称
    private String name;
    //树层级
    private  Integer level;
    //父级名称
    private String parent;
}
