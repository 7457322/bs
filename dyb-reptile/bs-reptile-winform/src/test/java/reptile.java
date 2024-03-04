import bs.common.ComLog;
import bs.common.ComStr;
import bs.reptile.winform.ExpressionProcess;
import org.junit.Test;

import java.io.File;
import java.net.URI;

public class reptile {
    @Test
    public void test() {
        int a=0;
        a=1;
        System.out.println("########################");
        System.out.println(a);

        String replace = ComStr.replace("\\$\\{([^}]+)\\}", "${parent.link}#${1,2}", t ->
        {
            ComLog.debug(t.group(1));
            return "$$$";
        });
        ComLog.debug(replace);
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