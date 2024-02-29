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
 * 抓取数据
 * </p>
 *
 * @author dyb
 * @since 2024-02-28 04:10:32
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("bs_reptile")
public class Reptile extends Model<Reptile> {

      /**
     * 主键
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 标题
     */
      @TableField("title")
    private String title;

      /**
     * 类型
     */
      @TableField("`type`")
    private String type;

      /**
     * 链接
     */
      @TableField("link")
    private String link;

      /**
     * 创建时间
     */
      @TableField("create_time")
    private Date createTime;

      /**
     * 来源
     */
      @TableField("`source`")
    private String source;

      /**
     * 详细
     */
      @TableField("content")
    private String content;

    @Override
    public Serializable pkVal() {
          return this.id;
      }
}
