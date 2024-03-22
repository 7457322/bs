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
 * @since 2024-03-22 10:25:23
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("bs_user")
public class User extends Model<User> {

      /**
     * id
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 创建时间
     */
      @TableField("create_time")
    private Date createTime;

      /**
     * 说明
     */
      @TableField("`description`")
    private String description;

      /**
     * 邮箱
     */
      @TableField("email")
    private String email;

      /**
     * 1启用0禁用
     */
      @TableField("`enable`")
    private Boolean enable;

      /**
     * 名称
     */
      @TableField("`name`")
    private String name;

      /**
     * 密码
     */
      @TableField("`password`")
    private String password;

      /**
     * 图片
     */
      @TableField("pic")
    private String pic;

    @TableField("salt")
    private String salt;

      /**
     * 更新时间
     */
      @TableField("update_time")
    private Date updateTime;

      /**
     * 用户名
     */
      @TableField("username")
    private String username;

    @Override
    public Serializable pkVal() {
          return this.id;
      }
}
