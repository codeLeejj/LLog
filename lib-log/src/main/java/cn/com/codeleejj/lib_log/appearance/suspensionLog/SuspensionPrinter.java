package cn.com.codeleejj.lib_log.appearance.suspensionLog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
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

public class SuspensionPrinter implements ILogPrinter {
    SuspensionHelper suspensionHelper;
    ISuspension launcher, logView;
    Context mContext;
    DateFormat dateFormat;

    public SuspensionPrinter(@NonNull Activity activity) {
        init(activity);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSSS", Locale.CHINA);
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
                layoutParams.width = 150;
                layoutParams.height = 150;
                return layoutParams;
            }

            @Override
            protected View createView() {
                Button button = new Button(activity.getApplicationContext());
                button.setAlpha(0.5f);
                button.setText("LOG");
                button.setBackgroundColor(Color.RED);
                button.setOnClickListener(v -> showLogView());
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

    private void showLogView() {
        suspensionHelper.close(launcher.getView());
        suspensionHelper.show(logView);
    }

    private void dismissLogView() {
        suspensionHelper.close(logView.getView());
        suspensionHelper.show(launcher);
    }

    @Override
    public void print(int level, String tag, String content) {
        addLog(new LogModel(level, tag, content));
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
}
