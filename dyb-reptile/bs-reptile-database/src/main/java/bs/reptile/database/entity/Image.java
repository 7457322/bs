package bs.reptile.database.entity;

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
 * @author dyb
 * @since 2024-03-22 10:25:22
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("bs_image")
public class Image extends Model<Image> {

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
