import bs.ComDbCodeGenerator;
import bs.common.ComCfg;
import bs.common.ComDb;
import bs.common.ComStr;
import bs.reptile.database.entity.User;
import bs.reptile.database.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;

import java.util.List;

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
            UserMapper mapper = session.getMapper(UserMapper.class);
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            List<User> images = mapper.selectList(wrapper);
            System.out.println(images);
        });
        assert true;
    }

    @Test
    public void 生成mapper_dto() {
        String projectPath = ComCfg.getCallPath().replaceFirst("[^\\\\]+\\\\$", "")
                + "bs-reptile-database\\src\\main\\";
        projectPath="E:\\projects\\Tests\\Java\\MybitisCodes";
        ComDbCodeGenerator
                .setPrefix("bs")
                .outputDatabase(projectPath);
        assert true;
    }
}