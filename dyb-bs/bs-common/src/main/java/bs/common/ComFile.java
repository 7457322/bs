package bs.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

//文件管理类
public class ComFile {
    //创建目录
    public static boolean createDir(String path) {
        // 创建一个表示新目录的File对象
        File newDirectory = new File(path);

        // 判断目录是否存在，如果不存在则尝试创建
        if (!newDirectory.exists()) {
            // 创建目录
            return newDirectory.mkdir();
        }
        return true;
    }
}