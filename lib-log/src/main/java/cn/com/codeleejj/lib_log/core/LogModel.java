package cn.com.codeleejj.lib_log.core;

/**
 * author:Lee
 * date:2021/7/9
 * Describe: 之所以创建这个类,是为了将日志用于更广泛的传递,如网络,文件,View等操作
 */
public class LogModel {
    public int level;
    public String tag;
    public String log;

    public LogModel(int level, String tag, String log) {
        this.level = level;
        this.tag = tag;
        this.log = log;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
