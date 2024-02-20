package bs.common.lambda;
/*
介绍 操作（无返回值）
作者 戴雁冰
日期 2023/2/22 16:44
*/
@FunctionalInterface
public interface Action<T extends Object> {
    void Run(T rst);
}
