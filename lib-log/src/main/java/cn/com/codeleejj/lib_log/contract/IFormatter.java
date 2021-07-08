package cn.com.codeleejj.lib_log.contract;

/**
 * author:Lee
 * date:2021/7/8
 * Describe:
 */
public interface IFormatter<T> {
    String format(T data);
}
