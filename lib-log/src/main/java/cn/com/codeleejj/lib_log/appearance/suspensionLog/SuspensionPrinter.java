package cn.com.codeleejj.lib_log.appearance.suspensionLog;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import cn.com.codeleejj.lib_log.R;
import cn.com.codeleejj.lib_log.contract.ILogPrinter;

public class SuspensionPrinter implements ILogPrinter {
    private Context mContext;
    SuspensionHelper suspensionHelper;
    ISuspension launcher, logView;

    public SuspensionPrinter(@NonNull Context context) {
        mContext = context;
        init();
    }

    private void init() {
        suspensionHelper = SuspensionHelper.getInstance(mContext);
        launcher = new ISuspension() {
            @Override
            public WindowManager.LayoutParams getLayoutParams() {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;

                layoutParams.width = 100;
                layoutParams.height = 100;

                return layoutParams;
            }

            @Override
            public View getView() {
                Button button = new Button(mContext.getApplicationContext());
                button.setAlpha(0.5f);
                button.setText("LOG");

                button.setOnClickListener(v -> showLogView());
                return button;
            }
        };

        logView = new ISuspension() {
            View view;

            @Override
            public WindowManager.LayoutParams getLayoutParams() {
                Display defaultDisplay = mContext.getDisplay();
                Point point = new Point();
                defaultDisplay.getSize(point);
                int x = point.x;
                int y = point.y;

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;

                layoutParams.width = x;
                layoutParams.height = y;
                return null;
            }

            @Override
            public View getView() {
                if (view == null) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.log_view, null, false);
                    view.findViewById(R.id.tvClose).setOnClickListener(v -> {
                        dismissLogView();
                    });
                }
                return view;
            }
        };
    }

    /**
     * 显示开启按钮
     */
    public void showLauncher() {
        suspensionHelper.show(launcher);
    }

    private void showLogView() {
        suspensionHelper.show(logView);
    }

    private void dismissLogView() {
        suspensionHelper.close(logView.getView());
    }

    @Override
    public void print(int level, String tag, String content) {

    }
}
