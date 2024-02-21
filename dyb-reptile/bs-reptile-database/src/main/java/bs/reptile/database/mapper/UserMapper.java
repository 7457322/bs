package bs.reptile.database.mapper;

import bs.reptile.database.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author dyb
 * @since 2024-02-21 03:14:06
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
