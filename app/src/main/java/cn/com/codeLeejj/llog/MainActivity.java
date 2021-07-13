package cn.com.codeLeejj.llog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioGroup;

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

        RadioGroup rg = findViewById(R.id.rg);
        CheckBox cbLogcat = findViewById(R.id.cbLogcat);
        CheckBox cbFile = findViewById(R.id.cbFile);
        CheckBox cbSuspension = findViewById(R.id.cbSuspension);
        findViewById(R.id.abtCreate).setOnClickListener(v -> {
            LogConfig.ConfigBuilder builder = new LogConfig.ConfigBuilder();
            if (cbLogcat.isChecked()) {
                LogcatPrinter logcatPrinter = new LogcatPrinter();
                builder.addIPrinter(logcatPrinter, LogLevel.I);
            }
            if (cbFile.isChecked()) {
                FilePrinter filePrinter = new FilePrinter(this);
                builder.addIPrinter(filePrinter, LogLevel.W);
            }
            if (cbSuspension.isChecked()) {
                suspensionPrinter = SuspensionPrinter.get(this);
                builder.addIPrinter(suspensionPrinter, LogLevel.I);
                suspensionPrinter.showLauncher();
            } else {
                if (suspensionPrinter != null) {
                    suspensionPrinter.closeLauncher();
                }
            }
            LLog.updateConfig(builder.build());
        });

        findViewById(R.id.abtPrint).setOnClickListener(v -> {
//            LLog.v("V:" + data);
//            LLog.d("D:" + data);
//            LLog.i("I:" + data);
//            LLog.w("W:" + data);
            LLog.e("E:" + data);
            LLog.a("A:" + data);
        });

        findViewById(R.id.abtOpenOtherActivity).setOnClickListener(v -> {
            startActivity(new Intent(this, OtherActivity.class));
        });
    }

    String data = "早在 Java 2 中之前，Java 就提供了特设类。比如：Dictionary, Vector, Stack, 和 Properties 这些类用来存储和操作对象组。\n" +
            "虽然这些类都非常有用，但是它们缺少一个核心的，统一的主题。由于这个原因，使用 Vector 类的方式和使用 Properties 类的方式有着很大不同。";
}
