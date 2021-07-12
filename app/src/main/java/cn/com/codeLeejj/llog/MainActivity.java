package cn.com.codeLeejj.llog;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import java.util.Date;

import cn.com.codeleejj.lib_log.appearance.FilePrinter;
import cn.com.codeleejj.lib_log.appearance.LLog;
import cn.com.codeleejj.lib_log.appearance.LogcatPrinter;
import cn.com.codeleejj.lib_log.appearance.suspensionLog.SuspensionPrinter;
import cn.com.codeleejj.lib_log.contract.LogConfig;
import cn.com.codeleejj.lib_log.contract.LogLevel;

public class MainActivity extends AppCompatActivity {
    SuspensionPrinter suspensionPrinter;

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

        findViewById(R.id.abtSuspensionLog).setOnClickListener(v -> {
            suspensionPrinter = new SuspensionPrinter(this);
            LLog.updateConfig(new LogConfig.ConfigBuilder(suspensionPrinter, LogLevel.V).build());
            suspensionPrinter.showLauncher();
        });

        findViewById(R.id.abtPrint).setOnClickListener(v -> {
//            LLog.v("V:" + date);
//            LLog.d("D:" + date);
//            LLog.i("I:" + date);
//            LLog.w("W:" + date);
//            LLog.e("E:" + date);
            LLog.a("A:" + data);
        });

        findViewById(R.id.abtOpenOtherActivity).setOnClickListener(v -> {
            startActivity(new Intent(this, OtherActivity.class));
        });
    }

    String data = "早在 Java 2 中之前，Java 就提供了特设类。比如：Dictionary, Vector, Stack, 和 Properties 这些类用来存储和操作对象组。\n" +
            "虽然这些类都非常有用，但是它们缺少一个核心的，统一的主题。由于这个原因，使用 Vector 类的方式和使用 Properties 类的方式有着很大不同。";
}
