package cn.com.codeLeejj.llog;

import androidx.appcompat.app.AppCompatActivity;

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

            checkSetting();
        });

        findViewById(R.id.abtPrint).setOnClickListener(v -> {
            LLog.v("V:" + date);
            LLog.d("D:" + date);
            LLog.i("I:" + date);
            LLog.w("W:" + date);
            LLog.e("E:" + date);
            LLog.a("A:" + date);
        });
    }

    String date = "早在 Java 2 中之前，Java 就提供了特设类。比如：Dictionary, Vector, Stack, 和 Properties 这些类用来存储和操作对象组。\n" +
            "虽然这些类都非常有用，但是它们缺少一个核心的，统一的主题。由于这个原因，使用 Vector 类的方式和使用 Properties 类的方式有着很大不同。";

    SuspensionPrinter suspensionPrinter;
    public static final int REQUEST_CODE = 714;

    private void checkSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), REQUEST_CODE);
            } else {
                suspensionPrinter.showLauncher();
            }
        } else {
            suspensionPrinter.showLauncher();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
                } else {
                    suspensionPrinter.showLauncher();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}