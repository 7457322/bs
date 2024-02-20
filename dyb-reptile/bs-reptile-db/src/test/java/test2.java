import bs.common.ComDate;
import bs.common.ComDb;
import bs.reptile.db.entity.User;
import bs.reptile.db.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class test2 {

    SqlSession session;

    @Test
    public void test(){
        testInsert();
        System.out.println("########################");
        testSelectList();
        System.out.println("########################");
        testInsert();
    }

    @Test
    public void testSelectList() {
        ComDb.run(session->{
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<User> users = mapper.selectList(new QueryWrapper<>());
            System.out.println(users);
        });
    }

    @Test
    public void testInsert(){
        ComDb.run(session-> {
            User user = new User();
            user.setCreateTime(new Date());
            user.setDescription("test");
            user.setPassword("123456");
            user.setUsername("test" + ComDate.toString("yyyyMMddHHmmssSSS"));
            user.setEnable(true);
            user.setEmail("xxx@qq.com");
            user.setUpdateTime(new Date());
            UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.insert(user);
            System.out.println(user);
        });
    }

    @Test
    public void testSelectPage(){
        ComDb.run(session->{
            UserMapper mapper = session.getMapper(UserMapper.class);
            Page<User> pageResult = mapper.selectPage(new Page<>(1, 1), new QueryWrapper<>());
            System.out.println(pageResult.getTotal());
            System.out.println(pageResult.getPages());
            System.out.println(pageResult.getRecords());
        });
    }

    @Test
    public void testUpdate(){
        ComDb.run(session->{
            UserMapper mapper = session.getMapper(UserMapper.class);
            User user = mapper.selectById(6);
            user.setName("test");
            user.setUpdateTime(new Date());
            user.setPassword("654321");
            user.setDescription("update");
            mapper.updateById(user);
            System.out.println(user);
        });
    }


    @Test
    public void testDelete(){
        ComDb.run(session->{
            UserMapper mapper = session.getMapper(UserMapper.class);
            int result = mapper.deleteById(15);
            System.out.println(result);
            this.testSelectList();
        });
    }

    @Test
    public void testFindAll(){
        ComDb.run(session->{
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<User> list = mapper.findAll();
            System.out.println(list);
        });
    }

    @Test
    public void testFindAllPage(){
        ComDb.run(session->{
            UserMapper mapper = session.getMapper(UserMapper.class);
            Page<User> pageResult = mapper.findAllPage(new Page<>(1, 1));
            System.out.println(pageResult.getTotal());
            System.out.println(pageResult.getPages());
            System.out.println(pageResult.getRecords());
        });
    }
}
