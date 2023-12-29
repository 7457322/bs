package bs.reptile.database.mapper;

import bs.reptile.database.entity.Image;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 图片 Mapper 接口
 * </p>
 *
 * @author dyb
 * @since 2023-12-21 05:11:55
 */
@Mapper
public interface ImageMapper extends BaseMapper<Image> {

}
