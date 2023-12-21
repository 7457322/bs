package bs.reptile.domain;

import lombok.Data;

/*图片*/
@Data
public class Image extends Base {

    /**
     * 网址
     **/
    private String url;
    /**
     * 类型
     **/
    private String type;
    /**
     * 名称
     **/
    private String name;
    /**
     * 备注
     **/
    private String remark;
}
