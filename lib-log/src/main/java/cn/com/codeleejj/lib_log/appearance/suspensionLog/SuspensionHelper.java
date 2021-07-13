package cn.com.codeleejj.lib_log.appearance.suspensionLog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

public class SuspensionHelper {
    static SuspensionHelper helper;
    private Context mContext;

    private WindowManager windowManager;
    private int windowWidth;

    private ISuspension suspension;
    private Handler handler;

    private SuspensionHelper(Context context) {
        mContext = context;
        if (windowManager == null)
            // 获取WindowManager服务
            windowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
        //没有这方法
//        WindowMetrics metrics = windowManager.getCurrentWindowMetrics();
//        windowBounds = metrics.getBounds();
        Display display = windowManager.getDefaultDisplay();
        windowWidth = display.getWidth();
    }

    public static SuspensionHelper getInstance(Context context) {
        if (helper == null) {
            helper = new SuspensionHelper(context);
            helper.handler = new Handler();
        } else {
            helper.mContext = context;
        }
        return helper;
    }

    /**
     * 根据 {@suspension} 创建窗口
     *
     * @param suspension 悬浮窗的定义¬
     */
    public void show(ISuspension suspension) {
        this.suspension = suspension;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(mContext)) {
                create();
            }
        } else {
            create();
        }
    }

    List<View> showingViews;

    private void create() {
        if (suspension == null) return;
        if (showingViews == null) {
            showingViews = new ArrayList<>();
        }
        // 新建悬浮窗控件
        View showingView = suspension.getView();
        showingViews.add(showingView);
        // 将悬浮窗控件添加到WindowManager
        if (showingView.getParent() != null) {
            windowManager.updateViewLayout(showingView, suspension.getLayoutParams());
        } else {
            windowManager.addView(showingView, suspension.getLayoutParams());
        }
    }

    /**
     * 是否有正在显示的悬浮窗
     *
     * @return true 表示有悬浮显示,false 表示没有显示的悬浮窗
     */
    public boolean isShowing() {
        return showingViews != null && showingViews.size() > 0;
    }

    /**
     * 关闭窗口
     */
    public void closeAll() {
        if (showingViews != null) {
            for (View showingView : showingViews) {
                close(showingView);
            }
        }
    }

    /**
     * 关闭窗口
     */
    public void close(View view) {
        if (showingViews.contains(view)) {
            showingViews.remove(view);
            windowManager.removeViewImmediate(view);
        }
    }

    public void updateAdjoin(Activity activity) {
        if (suspension == null) return;
        close(suspension.getView());
        if (suspension instanceof SuspensionWrapper) {
            handler.postDelayed(() -> {
                WindowManager.LayoutParams layoutParams = suspension.getLayoutParams();
                layoutParams.token = activity.getWindow().getDecorView().getWindowToken();
                ((SuspensionWrapper) suspension).updateLayoutParams(layoutParams);

                windowManager = activity.getWindowManager();

                if (suspension.getView().getParent() != null) {
                    windowManager.removeView(suspension.getView());
                }
                windowManager.addView(suspension.getView(), suspension.getLayoutParams());
            }, 300);
        }
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }
}
