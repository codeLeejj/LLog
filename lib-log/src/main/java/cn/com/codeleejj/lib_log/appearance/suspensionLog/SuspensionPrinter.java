package cn.com.codeleejj.lib_log.appearance.suspensionLog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.com.codeleejj.lib_log.R;
import cn.com.codeleejj.lib_log.contract.ILogPrinter;
import cn.com.codeleejj.lib_log.core.LogModel;
import cn.com.codeleejj.lib_log.utils.LevelUtil;

/**
 * 这是一个悬浮日志的demo,具体实现可根据项目情况重新编写一份
 */
public class SuspensionPrinter implements ILogPrinter {
    static SuspensionPrinter suspensionPrinter;
    SuspensionHelper suspensionHelper;
    boolean autoBorder;
    ISuspension logView;
    SuspensionWrapper launcher;
    Context mContext;
    DateFormat dateFormat;

    public static SuspensionPrinter get(Activity activity) {
        if (suspensionPrinter == null) {
            suspensionPrinter = new SuspensionPrinter(activity, true);
        }
        return suspensionPrinter;
    }

    /**
     * 初始化
     *
     * @param activity
     * @param autoBorder 是否自动贴边
     */
    private SuspensionPrinter(@NonNull Activity activity, boolean autoBorder) {
        this.autoBorder = autoBorder;
        init(activity);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);
    }

    private void init(Activity activity) {
        mContext = activity.getApplicationContext();
        suspensionHelper = SuspensionHelper.getInstance(activity);

        launcher = new SuspensionWrapper() {
            @Override
            protected WindowManager.LayoutParams createLayoutParams() {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
                layoutParams.token = activity.getWindow().getDecorView().getWindowToken();
                layoutParams.width = 130;
                layoutParams.height = 130;
                if (autoBorder) {
                    int i = suspensionHelper.getWindowWidth() / 2;
                    layoutParams.x = -i;
                } else {
                    layoutParams.x = 0;
                }
                layoutParams.y = 0;
                return layoutParams;
            }

            @Override
            protected View createView() {
                Button button = new Button(activity.getApplicationContext());
                button.setAlpha(0.4f);
                button.setText("LOG");
                button.setTextColor(Color.WHITE);
                button.setBackgroundResource(R.drawable.sp_circle);
                button.setOnTouchListener(new MyTouchListener());
                return button;
            }
        };

        logView = new ISuspension() {
            View view;

            @Override
            public WindowManager.LayoutParams getLayoutParams() {
                Display defaultDisplay = activity.getDisplay();
                Point point = new Point();
                defaultDisplay.getSize(point);
                int x = point.x;
                int y = point.y;

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;

                layoutParams.width = x;
                layoutParams.height = y;
                return layoutParams;
            }

            @Override
            public View getView() {
                if (view == null) {
                    view = LayoutInflater.from(activity).inflate(R.layout.log_view, null, false);
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

    public void closeLauncher() {
        suspensionHelper.close(launcher.getView());
    }

    private void showLogView() {
        suspensionHelper.close(launcher.getView());
        suspensionHelper.show(logView);
    }

    private void dismissLogView() {
        suspensionHelper.close(logView.getView());
        suspensionHelper.show(launcher);
    }

    @Override
    public void print(int level, String tag, String content, Date date) {
        addLog(new LogModel(level, tag, content,date));
    }

    List<LogModel> logs;
    ListView listView;
    ListAdapter adapter;

    public void addLog(LogModel log) {
        if (logs == null) {
            logs = new ArrayList<>();
        }
        logs.add(0, log);
        if (listView == null) {
            listView = logView.getView().findViewById(R.id.lv);
            adapter = new LogAdapter(mContext, R.layout.item_log, logs);
            listView.setAdapter(adapter);
        }
        listView.deferNotifyDataSetChanged();
    }

    class LogAdapter extends ArrayAdapter<LogModel> {

        public LogAdapter(@NonNull Context context, int resource, @NonNull List<LogModel> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_log, parent, false);
            }
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);

            LogModel log = getItem(position);
            tvTitle.setText(dateFormat.format(log.getDate()) + "  " + LevelUtil.levelDescribe(log.getLevel()) + "  " + log.getTag() + ":");
            tvContent.setText(log.getLog());
            return convertView;
        }
    }

    class MyTouchListener implements View.OnTouchListener {
        float firstRawX, firstRawY;
        float lastRawX, lastRawY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    firstRawX = lastRawX = event.getRawX();
                    firstRawY = lastRawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float xX = event.getRawX() - lastRawX;
                    float xY = event.getRawY() - lastRawY;

                    WindowManager.LayoutParams layoutParams = launcher.getLayoutParams();
                    layoutParams.x += xX;
                    layoutParams.y += xY;
                    suspensionHelper.getWindowManager().updateViewLayout(launcher.getView(), launcher.getLayoutParams());

                    lastRawX = event.getRawX();
                    lastRawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    float xxX = event.getRawX() - firstRawX;
                    float xxY = event.getRawY() - firstRawY;
                    if (Math.abs(xxX) < 10 && Math.abs(xxY) < 10) {
                        showLogView();
                    } else {
                        if (autoBorder) {
                            int windowSize = suspensionHelper.getWindowWidth();
                            int centerX = windowSize / 2;
                            WindowManager.LayoutParams layoutParams2 = launcher.getLayoutParams();
                            layoutParams2.x = (layoutParams2.x > 0) ? centerX : -centerX;
                            layoutParams2.windowAnimations = android.R.anim.accelerate_interpolator;

                            suspensionHelper.getWindowManager().updateViewLayout(launcher.getView(), launcher.getLayoutParams());

                        }
                    }
                    break;
            }
            return false;
        }
    }
}