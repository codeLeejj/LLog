package cn.com.codeleejj.lib_log.appearance;

import android.util.Log;

import cn.com.codeleejj.lib_log.contract.ILogPrinter;

/**
 * author:Lee
 * date:2021/7/8
 * Describe:控制台日志输出
 */
public class LogcatPrinter implements ILogPrinter {
    @Override
    public void print( int level, String tag, String content) {
        Log.println(level, tag, content);
    }
}
