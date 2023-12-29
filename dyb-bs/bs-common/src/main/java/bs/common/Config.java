package bs.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

//应用配置
@SuppressWarnings("unused")
public class Config {
    //region 静态
    //当前启动配置
    static Config current = new Config("config.properties");

    //当前启动配置
    public static Config Current() {
        return current;
    }
    //endregion

    //region 实例
    //在resources目录下的，配置文件名，如："config.properties"
    String fileName;
    //属性列表
    Properties props = new Properties();

    //构造函数
    public Config(String fileName) {
        this.fileName = fileName;
        try {
            // 从文件中读取配置信息
            FileInputStream fis = new FileInputStream(fileName);
            props.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取配置属性
    public String get(String name) {
        return props.getProperty(name);
    }

    //设置配置属性
    public Config set(String name, String value) {
        props.setProperty(name, value);
        return this;
    }

    //保存配置属性
    public Config save() {
        try {
            // 将属性写回到文件中
            FileOutputStream fos = new FileOutputStream(fileName);
            props.store(fos, "Updated Config");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
    //endregion
}
