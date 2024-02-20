package bs.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

public class ComDate {
    //region 转化
    //将日期转化成字符串
    public static String toString(Date inputDate) {
        return toString(inputDate,"yyyy-MM-dd HH:mm:ss");
    }
    //将日期转化成字符串
    public static String toString(String formatString) {
        return toString(new Date(),formatString);
    }
    //将日期转化成字符串
    public static String toString(Date inputDate,String formatString) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        String format = sdf.format(inputDate);
        return format;
    }
    //endregion
}