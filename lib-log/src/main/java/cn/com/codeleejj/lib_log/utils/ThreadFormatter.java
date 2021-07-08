package cn.com.codeleejj.lib_log.utils;

import cn.com.codeleejj.lib_log.contract.IFormatter;

public class ThreadFormatter implements IFormatter<Thread> {
    @Override
    public String format(Thread data) {
        return "---> Thread:" + data.getName();
    }
}
