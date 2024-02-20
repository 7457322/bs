package bs.reptile.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import bs.reptile.db.entity.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    List<User> findAll();

    Page<User> findAllPage(Page<User> page);
}

