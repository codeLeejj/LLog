package cn.com.codeleejj.lib_log.utils;

import cn.com.codeleejj.lib_log.contract.LogLevel;

public class LevelUtil {
    public static String levelDescribe(@LogLevel.LEVEL int level) {
        switch (level) {
            case LogLevel.V:
                return "verbose";
            case LogLevel.D:
                return "debug";
            case LogLevel.I:
                return "info";
            case LogLevel.W:
                return "warm";
            case LogLevel.E:
                return "error";
            case LogLevel.A:
                return "assert";
        }
        return "";
    }

}
