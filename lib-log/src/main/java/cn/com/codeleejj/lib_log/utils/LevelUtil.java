package cn.com.codeleejj.lib_log.utils;

import cn.com.codeleejj.lib_log.contract.LogLevel;

public class LevelUtil {
    public static String levelDescribe(@LogLevel.LEVEL int level) {
        switch (level) {
            case LogLevel.V:
                return "VERBOSE";
            case LogLevel.D:
                return "DEBUG";
            case LogLevel.I:
                return "INFO";
            case LogLevel.W:
                return "WARN";
            case LogLevel.E:
                return "ERROR";
            case LogLevel.A:
                return "ASSERT";
        }
        return "";
    }

}
