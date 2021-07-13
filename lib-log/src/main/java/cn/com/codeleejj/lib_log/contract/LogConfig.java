package cn.com.codeleejj.lib_log.contract;

import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.com.codeleejj.lib_log.appearance.LogcatPrinter;

/**
 * author:Lee
 * date:2021/7/8
 * Describe: 日志输出的配置,作用于:日志开关
 */

public class LogConfig {
    /**
     * true 启动日志输出,false 关闭
     */
    private boolean enable;
    /**
     * 默认的tag
     */
    private String defaultTag;
    /**
     * 是否输出到文件
     */
    private boolean out2File;
    /**
     * 输出到文件的最低等级
     */
    @LogLevel.LEVEL
    private int out2FileLevel;

    /**
     * 日志输出者集合(可以自定义输出到控制台,)
     */
    Map<ILogPrinter, Integer> printers;

    /**
     * 不建议直接创建
     */
    private LogConfig() {
    }

    public static LogConfig getDefault() {
        return new ConfigBuilder().setEnable(true).closeFileOut().build();
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getDefaultTag() {
        return defaultTag;
    }

    public void setDefaultTag(String defaultTag) {
        this.defaultTag = defaultTag;
    }

    public void setOut2File(boolean out2File) {
        this.out2File = out2File;
    }

    public void setOut2FileLevel(int out2FileLevel) {
        this.out2FileLevel = out2FileLevel;
    }

    public Map<ILogPrinter, Integer> getPrinters() {
        return printers;
    }

    public int getOut2FileLevel() {
        return out2FileLevel;
    }

    public void setPrinters(Map<ILogPrinter, Integer> printers) {
        this.printers = printers;
    }

    static public class ConfigBuilder {
        /**
         * true 启动日志输出,false 关闭
         */
        private boolean enable;
        /**
         * 默认的tag
         */
        private String defaultTag = "LLOG";
        private String defaultTimeFormat = "yyyy-MM-dd HH:mm:ss ";
        /**
         * 是否输出到文件
         */
        private boolean out2File;
        /**
         * 输出到文件的最低等级
         */
        @LogLevel.LEVEL
        private int out2FileLevel = LogLevel.W;
        /**
         * 日志输出者集合(可以自定义输出到控制台,)
         * Integer 记录这个输出者允许的最低日志等级
         */
        Map<ILogPrinter, Integer> printers;

        /**
         * 创建时会默认控制台输出,同时默认输出级别
         */
        public ConfigBuilder() {
            printers = new ArrayMap<>();
        }

        public ConfigBuilder defaultConfig() {
            LogcatPrinter logcatPrinter = new LogcatPrinter();
            printers.put(logcatPrinter, LogLevel.V);
            return this;
        }

        /**
         * 创建时传入 自定义的日志输出者(默认输出级别) 集合
         */
        public ConfigBuilder(ILogPrinter... logPrinters) {
            printers = new ArrayMap<>();
            for (ILogPrinter printer : logPrinters) {
                printers.put(printer, LogLevel.V);
            }
        }

        /**
         * 创建时传入 自定义的日志输出者 集合
         *
         * @param logPrinter 自定义日志输出者
         * @param level      输出者的最低级别
         */
        public ConfigBuilder(ILogPrinter logPrinter, @LogLevel.LEVEL int level) {
            printers = new ArrayMap<>();
            printers.put(logPrinter, level);
        }

        /**
         * 添加 自定义的日志输出者 集合
         */
        public ConfigBuilder addIPrinters(ILogPrinter... logPrinters) {
            for (ILogPrinter printer : logPrinters) {
                printers.put(printer, LogLevel.V);
            }
            return this;
        }

        public ConfigBuilder addIPrinter(ILogPrinter logPrinter, @LogLevel.LEVEL int minLevel) {
            printers.put(logPrinter, minLevel);
            return this;
        }

        /**
         * 移除 自定义的日志输出者 集合
         */
        public ConfigBuilder removeIPrinter(ILogPrinter... logPrinters) {
            for (ILogPrinter printer : logPrinters) {
                printers.remove(printer);
            }
            return this;
        }

        /**
         * 设置日志输出是否可用
         *
         * @param enable true 输出日志,false 不输出日志
         */
        public ConfigBuilder setEnable(boolean enable) {
            this.enable = enable;
            return this;
        }

        /**
         * 设置默认的TAG值
         */
        public ConfigBuilder setDefaultTag(String tag) {
            this.defaultTag = tag;
            return this;
        }

        /**
         * 开启文件输出
         *
         * @param printLevel 设置输出的最低级别
         */
        public ConfigBuilder openFileOut(@LogLevel.LEVEL int printLevel) {
            out2File = true;
            out2FileLevel = printLevel;
            return this;
        }

        /**
         * 关闭文件输出
         */
        public ConfigBuilder closeFileOut() {
            out2File = false;
            return this;
        }

        public LogConfig build() {
            LogConfig config = new LogConfig();
            config.setEnable(enable);
            config.setDefaultTag(defaultTag);
            config.setOut2File(out2File);
            config.setOut2FileLevel(out2FileLevel);
            config.setPrinters(printers);
            return config;
        }
    }
}

