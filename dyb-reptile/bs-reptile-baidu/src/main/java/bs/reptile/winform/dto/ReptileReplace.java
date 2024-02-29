package bs.reptile.winform.dto;

import lombok.Data;

//字段信息
@Data
public class ReptileReplace {
    //查找正则表达式
    private String search;
    //替换字符串
    private String replace;
}