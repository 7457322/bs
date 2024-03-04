package bs.common;

import bs.common.lambda.Func;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ComStr {
    //region 读取

    /**
     * 读取来自输入流的文本
     * @param input 输入字节流
     * @return 字符串
     */
    @SuppressWarnings("All")
    public static String readByInputStream(InputStream input) {
        return readByInputStream(input, StandardCharsets.UTF_8);
    }

    //读取来自输入流的文本
    public static String readByInputStream(InputStream input, Charset cs) {
        @SuppressWarnings("All")
        String text = new BufferedReader(
                new InputStreamReader(input, cs))
                .lines()
                .collect(Collectors.joining("\n"));
        return text;
    }
    //endregion

    //region 转化

    /**
     * 转换成 InputStream 对象
     * @param input 输入字符串
     * @return 输入字节流
     */
    @SuppressWarnings("All")
    public static InputStream toInputStream(String input) {
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        return stream;
    }

    /**
     * 转换成 标准路径 字符串（带\结尾）
     *
     * @param absoluteDir 文件夹绝对路径
     * @return 标准文件夹绝对路径（带\结尾）
     */
    public static String toStandardPath(String absoluteDir) {
        if (isEmpty(absoluteDir)) return absoluteDir;
        String strEnd = absoluteDir.substring(absoluteDir.length() - 1);
        if (strEnd.equals("\\") || strEnd.equals("/")) return absoluteDir;
        return absoluteDir + "\\";
    }
    //endregion

    //region 替换
    public static String replace(String regex, String input, Func<Matcher, String> fn) {
        Pattern pattern = Pattern.compile(regex);
        @SuppressWarnings("All")
        String rst = replace(pattern, input, fn);
        return rst;
    }

    public static String replace(Pattern regex, String input, Func<Matcher, String> fn) {
        Matcher matcher = regex.matcher(input);
        @SuppressWarnings("All")
        String rst = replace(matcher, input, fn);
        return rst;
    }

    public static String replace(Matcher matcher, String input, Func<Matcher, String> fn) {
        ArrayList<String> splits = new ArrayList<>();
        List<String> ues = getMatches(matcher, input, fn, splits);
        int size = ues.size();
        StringBuilder sb = new StringBuilder();
        sb.append(splits.get(0));
        for (int i = 0; i < size; i++) {
            sb.append(ues.get(i));
            sb.append(splits.get(i + 1));
        }
        return sb.toString();
    }

    static List<String> getMatches(Matcher matcher, String input, Func<Matcher, String> fn, List<String> splits) {
        if (splits == null) splits = new ArrayList<>();
        ArrayList<String> rst = new ArrayList<>();
        matcher.reset();
        int start = 0, end = input.length();
        //循环所有模板字段
        while (matcher.find()) {
            splits.add(input.substring(start, matcher.start()));
            rst.add(fn.Run(matcher));
            start = matcher.end();
        }
        splits.add(start >= end ? "" : input.substring(start, end));
        return rst;
    }
    //endregion

    //region 判断
    //判断是否不为空（null值表示为空）
    @SuppressWarnings("All")
    public static Boolean isNotEmpty(String input) {
        return !isEmpty(input);
    }

    //判断是否为空（null值表示为空）
    public static Boolean isEmpty(String input) {
        if (input == null) return true;
        return input.length() == 0;
    }
    //endregion
}