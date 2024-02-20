package ${package.Other}.po;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 图片 数据表 image 数据库表映射实体驼峰名称
 *      基础设施层请求实体
 * </p>
 *
 * @author dyb
 * @since 2024-02-18 10:10:08
 */
@Getter
@Setter
public class ImagePO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer id;

    private Integer type;

    /**
     * 名称
     */
    private String name;

    private String remark;

    /**
     * 当前页
     */
    private Integer page = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 20;

}