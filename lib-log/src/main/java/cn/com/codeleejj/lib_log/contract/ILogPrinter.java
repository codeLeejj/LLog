package cn.com.codeleejj.lib_log.contract;

public interface ILogPrinter {
    void print(final LogConfig config,@LogLevel.LEVEL int level,String tag, String content);
}
