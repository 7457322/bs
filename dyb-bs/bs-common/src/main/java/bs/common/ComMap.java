package bs.common;

import bs.common.lambda.Action;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class ComMap {
    //region 转化
    //转换成任务对象
    public static <T extends Object> T toObject(Map map, T obj) {
        if (map == null) return obj;
        Class cls = obj.getClass();
        for (Field field : cls.getDeclaredFields()) {
            String name = field.getName();
            Object value = map.get(name);
            if (value != null) {
                field.setAccessible(true);
                try {
                    field.set(obj, value);
                } catch (Exception e) {

                }
            }
        }
        return obj;
    }

    public static String toJson(Map map) {
//        if (map == null) return "";
        return JSONObject.toJSONString(map);
    }
    //endregion

    //region 复制

    /**
     * 浅度复制对象
     *
     * @param map 原map对象
     * @param <T> Key类型
     * @param <V> Value类型
     * @return 新map对象
     */
    public static <T, V> Map<T, V> copy(Map<T, V> map) {
        Map<T, V> rst = new HashMap<>();
        map.forEach((k, v) -> rst.put(k, v));
        return rst;
    }

    /**
     * 深度复制对象
     *
     * @param map 原map对象
     * @param <T> Key类型
     * @param <V> Value类型
     * @return 新map对象
     */
    public static <T, V> Map<T, V> copyAll(Map<T, V> map) {
        Map<T, V> rst = new HashMap<>();
        rst.putAll(map);
        return rst;
    }
    //endregion

    //region 执行
    //    @Test
    //    public void eachCall() {
    //        Map<String, List<String>> stringListMap = new HashMap<>();
    //        List<String> s1 = new ArrayList<>();
    //        s1.add("1");s1.add("2");
    //        List<String> s2 = new ArrayList<>();
    //        s2.add("3");s2.add("4");s2.add("8");
    //        List<String> s3 = new ArrayList<>();
    //        s3.add("5");s3.add("6");s3.add("7");s3.add("9");
    //        stringListMap.put("a.a.a.",s1);
    //        stringListMap.put("a.a.",s2);
    //        stringListMap.put("a.",s3);
    //        call(stringListMap,t-> ComLog.debug(t.get("a.a.a.")+":"+t.get("a.a.")+":"+t.get("a.")));
    //    }

    // 多个数据集元素组合调用
    // 集合：a[1,2],b[3,4],c[5,6]
    // 调用总次数：a.数量*b.数量*c.数量
    // 函数接收参数分别{a=1,b=3,c=5}、{a=1,b=3,c=6}、{a=1,b=4,c=5}....{a=2,b=4,c=6}
    public static <T> void eachCall(Map<String, List<T>> map, Action<Map<String, T>> fn) {
        if (map == null || map.size() == 0) return;
        List<String> keys = new ArrayList<>();
        List<List<T>> vals = new ArrayList<>();
        Map<String, T> path = new HashMap<>();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            keys.add(key);
            vals.add(map.get(key));
        }
        eachCall(keys, vals, fn, path);
    }

    //多个数据集元素组合调用递归
    static <T> void eachCall(List<String> keys, List<List<T>> vals, bs.common.lambda.Action<Map<String, T>> fn, Map<String, T> path) {
        int level = path.size(), maxLevel = vals.size();
        if (level + 1 >= maxLevel) {
            String key = keys.get(level);
            List<T> value = vals.get(level);
            for (T v : value) {
                path.put(key, v);
                fn.Run(path);
            }
            path.remove(key);
        } else {
            String key = keys.get(level);
            List<T> value = vals.get(level);
            for (T v : value) {
                path.put(key, v);
                eachCall(keys, vals, fn, path);
                path.remove(key);
            }
        }
    }
    //endregion
}