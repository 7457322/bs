package bs.reptile.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Base {
    /**
     * 介绍 编号
     **/
    @TableId(type = IdType.AUTO)
    private Integer id;
}
