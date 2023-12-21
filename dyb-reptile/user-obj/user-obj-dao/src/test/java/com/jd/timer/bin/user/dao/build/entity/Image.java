package com.jd.timer.bin.user.dao.build.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 图片
 * </p>
 *
 * @author timerbin
 * @since 2023-12-21 10:54:19
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("image")
public class Image extends Model<Image> {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private Integer id;

    @TableField("`type`")
    private Integer type;

    /**
     * 名称
     */
    @TableField("`name`")
    private String name;

    @TableField("remark")
    private String remark;

    @Override
    public Serializable pkVal() {
        return null;
    }
}