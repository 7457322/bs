package bs.common.lambda;

import bs.common.ComLog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/*
介绍 操作（无返回值）
作者 戴雁冰
日期 2023/2/22 16:44
*/
@FunctionalInterface
public interface Action<T extends Object> {
    //执行方法
    void Run(T rst);
}
