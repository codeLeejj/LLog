package cn.com.codeleejj.lib_log.appearance;

import java.util.Date;
import java.util.Map;

import cn.com.codeleejj.lib_log.contract.ILogPrinter;
import cn.com.codeleejj.lib_log.contract.LogConfig;
import cn.com.codeleejj.lib_log.contract.LogLevel;
import cn.com.codeleejj.lib_log.core.LogFormat;

public class LLog {
    /**
     * 初始化一个默认的
     */
    private static LogConfig mLogConfig = LogConfig.getDefault();

    /**
     * 更新输出配置
     *
     * @param mLogConfig
     */
    public static void updateConfig(LogConfig mLogConfig) {
        LLog.mLogConfig = mLogConfig;
    }

    public static void v(Object... contents) {
        log(LogLevel.V, contents);
    }

    public static void vt(String tag, Object... contents) {
        log(LogLevel.V, tag, contents);
    }

    public static void d(Object... contents) {
        log(LogLevel.D, contents);
    }

    public static void dt(String tag, Object... contents) {
        log(LogLevel.D, tag, contents);
    }

    public static void i(Object... contents) {
        log(LogLevel.I, contents);
    }

    public static void it(String tag, Object... contents) {
        log(LogLevel.I, tag, contents);
    }

    public static void w(Object... contents) {
        log(LogLevel.W, contents);
    }

    public static void wt(String tag, Object... contents) {
        log(LogLevel.W, tag, contents);
    }

    public static void e(Object... contents) {
        log(LogLevel.E, contents);
    }

    public static void et(String tag, Object... contents) {
        log(LogLevel.E, tag, contents);
    }

    public static void a(Object... contents) {
        log(LogLevel.A, contents);
    }

    public static void at(String tag, Object... contents) {
        log(LogLevel.A, tag, contents);
    }

    private static void log(@LogLevel.LEVEL int level, Object... contents) {
        log(level, mLogConfig.getDefaultTag(), contents);
    }

    /**
     * 遍历输出者,根据配置(日志级别,文件输出)去输出日志
     *
     * @param level    日志级别
     * @param tag      日志标签
     * @param contents 日志内容
     */
    private static void log(@LogLevel.LEVEL int level, String tag, Object... contents) {
        Date date = new Date();
        String content = LogFormat.format(mLogConfig, level, contents);
        Map<ILogPrinter, Integer> printers = mLogConfig.getPrinters();
        for (ILogPrinter printer : printers.keySet()) {
            Integer allowLevel = printers.get(printer);
            if (allowLevel == null || allowLevel <= level) {
                printer.print(level, tag, content, date);
            }
        }
    }
}