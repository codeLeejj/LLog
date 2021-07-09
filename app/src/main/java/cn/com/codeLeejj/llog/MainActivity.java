package cn.com.codeLeejj.llog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Date;

import cn.com.codeleejj.lib_log.appearance.FilePrinter;
import cn.com.codeleejj.lib_log.appearance.LLog;
import cn.com.codeleejj.lib_log.appearance.LogcatPrinter;
import cn.com.codeleejj.lib_log.contract.LogConfig;
import cn.com.codeleejj.lib_log.contract.LogLevel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.abtInitWithFile).setOnClickListener(v -> {
            LogcatPrinter logcatPrinter = new LogcatPrinter();
            FilePrinter filePrinter = new FilePrinter(this);
            LLog.updateConfig(new LogConfig.ConfigBuilder(logcatPrinter, LogLevel.I).addIPrinter(filePrinter, LogLevel.W).build());
        });

        findViewById(R.id.abtInitWithoutFile).setOnClickListener(v -> {
            LogcatPrinter logcatPrinter = new LogcatPrinter();
            LLog.updateConfig(new LogConfig.ConfigBuilder(logcatPrinter, LogLevel.I).build());
        });

        findViewById(R.id.abtPrint).setOnClickListener(v -> {
            LLog.v("v:" + new Date());
            LLog.d("D:" + new Date());
            LLog.i("I:" + new Date());
            LLog.w("w:" + new Date());
            LLog.e("e:" + new Date());
            LLog.a("a:" + new Date());
        });
    }
}