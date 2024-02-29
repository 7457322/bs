package bs.reptile.database.mapper;

import bs.reptile.database.entity.Reptile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 抓取数据 Mapper 接口
 * </p>
 *
 * @author dyb
 * @since 2024-02-28 03:02:12
 */
@Mapper
public interface ReptileMapper extends BaseMapper<Reptile> {

}
