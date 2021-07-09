package cn.com.codeleejj.lib_log.contract;

public interface ILogPrinter {
    void print( @LogLevel.LEVEL int level,String tag, String content);
}
