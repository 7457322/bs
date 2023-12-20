package bs.reptile.mapper;

import bs.reptile.domain.Image;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageMapper extends BaseMapper<Image> {
    //public List<Admin> selectList1(Admin admin);
}
