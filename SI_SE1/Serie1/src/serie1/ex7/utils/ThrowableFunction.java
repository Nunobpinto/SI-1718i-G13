package serie1.ex7.utils;

@FunctionalInterface
public interface ThrowableFunction<T,R> {
    R apply(T t)throws Exception;
}
