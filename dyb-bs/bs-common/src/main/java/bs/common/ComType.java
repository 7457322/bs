package bs.common;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.*;
import java.util.Map;

public class ComType {
    //获取泛型类中T的类型(未测试)
    public static <T> Class getGenericType(Class<T> clazz) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        if (genericInterfaces.length == 0) return null;

        Type genericInterface = genericInterfaces[0];
        if(genericInterface instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericInterface).getActualTypeArguments();
            if (actualTypeArguments.length == 0) return null;
            return (Class)actualTypeArguments[0];
        }else{
            Method method = clazz.getDeclaredMethods()[0];
            method.getParameterTypes();
        }

        return null;
    }
}