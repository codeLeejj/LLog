package cn.com.codeleejj.lib_log.contract;

import java.util.Date;

public interface ILogPrinter {
    /**
     * 日志输出协议
     *
     * @param level   日志级别
     * @param tag     日志tag
     * @param content 日志内容
     * @param date    日志时间,之所以需要传入,是为了保证每个输出者对于同一条的时间是一致的
     */
    void print(@LogLevel.LEVEL int level, String tag, String content, Date date);
}
