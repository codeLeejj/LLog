package cn.com.codeleejj.lib_log.appearance;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.File;

import cn.com.codeleejj.lib_log.contract.ILogPrinter;
import cn.com.codeleejj.lib_log.contract.LogConfig;
import cn.com.codeleejj.lib_log.contract.LogLevel;

/**
 * author:Lee
 * date:2021/7/8
 * Describe:
 */
public class FilePrinter implements ILogPrinter {

    public static final String DIR_NAME = "llog";
    public String outDir;

    public FilePrinter(@NonNull Context context) {
        this(context, null);
    }

    public FilePrinter(@NonNull Context context, String path) {
        File file = null;
        if (!TextUtils.isEmpty(path))
            file = new File(path);

        if (file != null && file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && parent.exists()) {
                file = new File(parent, DIR_NAME);
            }
        } else {
            String externalStorageState = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(externalStorageState)) {
                File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                file = new File(dir, DIR_NAME);
            } else {
                file = new File(context.getCacheDir(), DIR_NAME);
            }
        }
        outDir = file.getAbsolutePath();
    }

    @Override
    public void print(LogConfig config, @LogLevel.LEVEL int level, String tag, String content) {
        int out2FileLevel = config.getOut2FileLevel();
        if (out2FileLevel > level) {
            return;
        }

    }
}
