package bs.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

//应用配置
public class ComCfg {

    //region 静态成员
    //读取配置文件名
    static String filename = "config.properties";
    //属性列表
    static Properties props = new Properties();

    //当前实例
    static ComCfg current = new ComCfg();
    //运行目录
    static String runPath = "";
    //调用目录
    static String callPath = "";

    //静态构造函数
    static {
        //运行目录
        URL resource = current.getClass().getClassLoader().getResource("");
        runPath = resource.getPath()
                .replaceFirst("\\/", "")
                .replaceAll("\\\\/", "\\");
        //调用目录
        callPath= System.getProperty("user.dir")+"\\";
        //加载配置
        load(runPath + filename);
    }

    //加载配置文件
    static void load(String fileName) {
        try {
            // 从文件中读取配置信息
            FileInputStream fis = new FileInputStream(fileName);
            props.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //保存配置属性
    public static ComCfg save(String fileName) {
        try {
            // 将属性写回到文件中
            FileOutputStream fos = new FileOutputStream(fileName);
            props.store(fos, "Updated Config");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return current;
    }

    //保存配置属性
    public static ComCfg save() {
        return save(runPath + filename);
    }

    //读取配置属性
    public static String get(String name) {
        return props.getProperty(name);
    }

    //设置配置属性
    public static ComCfg set(String name, String value) {
        props.setProperty(name, value);
        return current;
    }

    //读取运行路径(结尾带\\)
    public static String getRunPath() {
        return runPath;
    }

    //读取调用路径(结尾带\\)，比如：shell命令文件所在目录等等
    public static String getCallPath() {
        return callPath;
    }
    //endregion

    //region 实例成员

    //endregion
}
