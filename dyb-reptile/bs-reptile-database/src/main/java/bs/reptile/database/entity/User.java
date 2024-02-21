package bs.reptile.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author dyb
 * @since 2024-02-21 03:21:39
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    @TableField("create_time")
    private Date createTime;

    @TableField("`description`")
    private String description;

    @TableField("email")
    private String email;

    @TableField("`enable`")
    private Boolean enable;

    @TableField("`name`")
    private String name;

    @TableField("`password`")
    private String password;

    @TableField("pic")
    private String pic;

    @TableField("salt")
    private String salt;

    @TableField("update_time")
    private Date updateTime;

    @TableField("username")
    private String username;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
