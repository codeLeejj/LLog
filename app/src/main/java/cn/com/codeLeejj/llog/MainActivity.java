package cn.com.codeLeejj.llog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Date;

import cn.com.codeleejj.lib_log.appearance.LLog;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.abtInitWithFile).setOnClickListener(v -> {

        });
        findViewById(R.id.abtInitWithoutFile).setOnClickListener(v -> {

        });
        findViewById(R.id.abtPrint).setOnClickListener(v -> {
            LLog.w(new Date());
        });

    }
}