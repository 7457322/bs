import bs.common.ComDb;
import bs.common.ComLog;
import bs.reptile.database.entity.Reptile;
import bs.reptile.database.mapper.ReptileMapper;
import bs.reptile.winform.dto.ReptileConfig;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.sql.Array;
import java.util.*;

import static org.junit.Assert.*;

public class baidu {


    @Test
    public Integer test(Integer aa) {
        return 1;
    }


    @Test
    public void testPath_Url() {
        String path = "f:\\aa.txt";
        File file = new File(path);
        URI uri = file.toURI();
        String absolutePath = file.getAbsolutePath();

        ComLog.debug("路径###################################################");
        ComLog.debug("原路径：" + path);
        ComLog.debug("是否真实存在：" + (file.exists() ? "true" : "false"));
        ComLog.debug("转成Uri：" + uri.toString());
        ComLog.debug("转成绝对路径：" + absolutePath);


        ComLog.debug("网址###################################################");
        String url = "https://wwww.baidu.com/aa.html";
        URI uri1 = URI.create(url);
        ComLog.debug("原网址：" + url);
        ComLog.debug("转成Uri：" + uri.toString());
    }
}