package bs.reptile.winform.dto;

import bs.common.ComStr;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReptileUrlField {
    //字段名称
    private String name;
    //树层级
    private  Integer level;
    //父级名称
    private String parent;
}
