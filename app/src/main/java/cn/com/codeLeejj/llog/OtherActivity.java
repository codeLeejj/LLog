package cn.com.codeLeejj.llog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        findViewById(R.id.btBack).setOnClickListener(v -> {
            finish();
        });
    }
}