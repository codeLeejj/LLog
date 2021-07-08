package cn.com.codeleejj.lib_log.core;

import cn.com.codeleejj.lib_log.contract.LogConfig;
import cn.com.codeleejj.lib_log.contract.LogLevel;

/**
 * author:Lee
 * date:2021/7/8
 * Describe: 负责输出内容的生成
 */
public class LogFormat {
    public static String format(LogConfig config, @LogLevel.LEVEL int level, Object... objects) {
        StringBuilder builder = new StringBuilder();
        for (Object object : objects) {
            builder.append(object.toString()).append("\n");
        }
        return builder.toString();
    }
}