import bs.common.ComLog;
import org.junit.Test;

import java.io.File;
import java.net.URI;

public class reptile {
    @Test
    public void test() {

        System.out.println("1aaaaaaaaaaa".indexOf("1"));

//        BigDecimal aa=new BigDecimal(0);
//        if(aa==null){
//            System.out.println(aa.toString());
//        }

//        test2 testx = new test2();
//        testx.setA(test.NOPRICE);
//        System.out.println(test.NOPRICE==testx.a);



//        StringBuilder sb = new StringBuilder();
//        sb.append(",222");
//        sb.append(",3333");
//        if (sb.length() == 0) return;
//        System.out.println(sb.substring(1));
//        BigDecimal a=new BigDecimal(1.22222);
//        System.out.println(BigDecimal.valueOf(10000).multiply(a).toString());
//        for (int i = 0; i < 3; i++) {
//            continue;
//        }

//        int a=0;
//        a=1;
//        System.out.println("########################");
//        System.out.println(a);
//
//        String replace = ComStr.replace("\\$\\{([^}]+)\\}", "${parent.link}#${1,2}", t ->
//        {
//            ComLog.debug(t.group(1));
//            return "$$$";
//        });
//        ComLog.debug(replace);
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