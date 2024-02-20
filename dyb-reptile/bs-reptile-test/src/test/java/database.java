import bs.common.ComCfg;
import bs.reptile.database.CodeGenerator;
import bs.reptile.database.entity.Image;
import bs.reptile.database.mapper.ImageMapper;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

public class database {
    @Test
    public void test2() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(ComCfg.get("mysql.url"));
        dataSource.setUser(ComCfg.get("mysql.username"));
        dataSource.setPassword(ComCfg.get("mysql.password"));
        // 创建session实列
        SqlSessionFactory sqlSessionFactory = MapperFactory.createSqlSessionFactory(dataSource);
        if (sqlSessionFactory == null) return;
//
//        SqlSession session = sqlSessionFactory.openSession();
//        //返回mapper
//        ImageMapper mapper = session.getMapper(ImageMapper.class);
//        Image image = new Image();
//        image.setName("test");
//        image.setType(1);
//        image.setRemark("测试");
//        mapper.insert(image);
//        // 提交事务
//        session.commit();
//        // 关闭session
//        session.close();

        assert true;
    }

    @Test
    public void test() {
        // 创建session实列
        SqlSession session = MapperFactory.createSession();
        if (session == null) return;
        //返回mapper
        ImageMapper mapper = session.getMapper(ImageMapper.class);
        Image image = new Image();
        image.setName("test");
        image.setType(1);
        image.setRemark("测试");
        mapper.insert(image);
        // 提交事务
        session.commit();
        // 关闭session
        session.close();

        assert true;
    }

    @Test
    public void 生成mapper_dto() {
        CodeGenerator.init();
        assert true;
    }
}