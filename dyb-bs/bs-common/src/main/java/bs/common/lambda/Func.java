package bs.common.lambda;

/*
介绍 函数（有返回值）
作者 戴雁冰
日期 2023/2/22 16:44
*/
@FunctionalInterface
public interface Func<T extends Object, Rtn extends Object> {
    Rtn Run(T rst);
}
