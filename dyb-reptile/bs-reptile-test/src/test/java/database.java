import bs.common.ComCfg;
import bs.common.ComDb;
import bs.reptile.database.ComDbCodeGenerator;
import bs.reptile.database.entity.Image;
import bs.reptile.database.mapper.ImageMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class database {
    @Test
    public void testx() {
//        List<Map<String, Object>> maps = ComDb.executeSql("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE()");
//        System.out.println(ComCfg.getRunPath());


            System.out.println(ComCfg.getRunPath());
        assert true;
    }

    @Test
    public void test() {
        ComDb.run(session -> {
//            ImageMapper mapper = session.getMapper(ImageMapper.class);
//            QueryWrapper<Image> wrapper = new QueryWrapper<>();
//            List<Image> images = mapper.selectList(wrapper);
//            System.out.println(images);
        });
        assert true;
    }

    @Test
    public void 生成mapper_dto() {
        ComDbCodeGenerator.output(ComCfg.getCallPath().replaceFirst("[^\\\\]+\\\\$","")+"bs-reptile-database\\src\\main\\java");
        assert true;
    }
}