package bs.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ComStr {
    //region 读取
    //读取来自输入流的文本
    public static String readByInputStream(InputStream input) {
        return readByInputStream(input, StandardCharsets.UTF_8);
    }
    //读取来自输入流的文本
    public static String readByInputStream(InputStream input, Charset cs) {
        String text = new BufferedReader(
                new InputStreamReader(input, cs))
                .lines()
                .collect(Collectors.joining("\n"));
        return text;
    }
    //endregion

    //region 转化
    //转换成 InputStream 对象
    public static InputStream toInputStream(String input) {
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        return stream;
    }
    //转换成 标准路径 字符串（带\结尾）
    public static String toStandardPath(String absoluteDir) {
        String strEnd = absoluteDir.substring(absoluteDir.length() - 1);
        if (strEnd == "\\" || strEnd == "/") return absoluteDir;
        return absoluteDir + "\\";
    }
    //endregion

    //region 判断
    //判断是否不为空（null值表示为空）
    public static Boolean isNotEmpty(String input) {
        return !isEmpty(input);
    }
    //判断是否为空（null值表示为空）
    public static Boolean isEmpty(String input) {
        if (input == null) return true;
        if (input.length() == 0) return true;
        return false;
    }
    //endregion
}