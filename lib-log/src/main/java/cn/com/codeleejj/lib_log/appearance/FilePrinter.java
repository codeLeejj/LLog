package cn.com.codeleejj.lib_log.appearance;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import cn.com.codeleejj.lib_log.contract.ILogPrinter;
import cn.com.codeleejj.lib_log.contract.LogLevel;
import cn.com.codeleejj.lib_log.core.LogModel;
import cn.com.codeleejj.lib_log.utils.LevelUtil;

/**
 * author:Lee
 * date:2021/7/8
 * Describe:
 */
public class FilePrinter implements ILogPrinter {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    public static final String DIR_NAME = "llog";
    public String outDir;
    /**
     * 输出日志的文件名,这个文件是根据日期动态生成的
     */
    public String outFile;

    PrintWorker worker;
    LogWriter writer;

    /**
     * 标记初始化是否成功
     */
    boolean isAvailability;

    /**
     * 创建日志的文件输出者
     *
     * @param context
     */
    public FilePrinter(@NonNull Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param dirPath
     */
    public FilePrinter(@NonNull Context context, String dirPath) {
        initOutFile(context, dirPath);
        worker = new PrintWorker();
        writer = new LogWriter();

        isAvailability = writer.prepare(outDir + "/" + outFile);
    }

    @Override
    public void print(@LogLevel.LEVEL int level, String tag, String content) {
        if (!isAvailability) {
            return;
        }
        LogModel model = new LogModel(level, tag, content);
        if (!worker.isRunning()) {
            worker.start();
        }

        //入队,等待被读取
        worker.deque.add(model);


//        writer.append(tag + ":" + content);
    }

    /**
     * 初始化输出文件
     *
     * @param context 上下文,以便在path不可用的时候获取默认路径
     * @param dirPath 指定文件的输出目录,如果这个目录无效则使用默认目录(保证默认目录的一致性).
     */
    private void initOutFile(Context context, String dirPath) {
        File file = null;
        if (!TextUtils.isEmpty(dirPath))
            file = new File(dirPath);

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

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        outFile = dateFormat.format(new Date()) + ".log";
    }

    private class PrintWorker implements Runnable {
        private BlockingDeque<LogModel> deque = new LinkedBlockingDeque<>();
        private volatile boolean isRunning;

        public synchronized boolean isRunning() {
            return isRunning;
        }

        public void start() {
            synchronized (this) {
                if (isRunning) {
                    return;
                }
                EXECUTOR.execute(this);
                isRunning = true;
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    LogModel take = deque.take();
                    writer.append(take);
                } catch (InterruptedException e) {
                } catch (IOException e) {
                    writer.close();
                }
            }
        }
    }

    /**
     * 实际写入文件的类
     */
    private static class LogWriter {
        private File logFile;
        private BufferedWriter bufferedWriter;
        DateFormat dateFormat;

        public LogWriter() {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSSS", Locale.CHINA);
        }

        public boolean prepare(@NonNull String filePath) {
            logFile = new File(filePath);
            if (!logFile.exists()) {
                File parentFile = logFile.getParentFile();
                if (!parentFile.exists())
                    parentFile.mkdir();
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    logFile = null;
                    return false;
                }
            }
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));
            } catch (IOException e) {
                logFile = null;
                return false;
            }
            return true;
        }

        public void append(LogModel log) throws IOException {
            //附加信息
            bufferedWriter.write(dateFormat.format(new Date()));
            bufferedWriter.write("  ");
            bufferedWriter.write(LevelUtil.levelDescribe(log.level));
            bufferedWriter.write("  ");
            bufferedWriter.write(log.tag + ":");
            bufferedWriter.newLine();
            //日志正文
            bufferedWriter.write(log.log);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        public boolean close() {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    return false;
                } finally {
                    logFile = null;
                    bufferedWriter = null;
                }
            }
            return true;
        }
    }
}
