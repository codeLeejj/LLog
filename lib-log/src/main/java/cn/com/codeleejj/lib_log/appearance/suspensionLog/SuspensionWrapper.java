package cn.com.codeleejj.lib_log.appearance.suspensionLog;

import android.view.View;
import android.view.WindowManager;

/**
 * author:Lee
 * date:2021/7/12
 * Describe:
 */
public abstract class SuspensionWrapper implements ISuspension {
    WindowManager.LayoutParams layoutParams;
    View mView;

    /**
     * initialize the view LayoutParams,or create the first LayoutParams.
     *
     * @return the LayoutParams of view
     */
    protected abstract WindowManager.LayoutParams createLayoutParams();

    /**
     * initialize the view LayoutParams,or create the first view.
     *
     * @return view to show
     */
    protected abstract View createView();

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        if (layoutParams == null)
            layoutParams = createLayoutParams();
        return layoutParams;
    }

    @Override
    public View getView() {
        if (mView == null)
            mView = createView();
        return mView;
    }

    /**
     * update LayoutParams
     *
     * @param layoutParams
     */
    public void updateLayoutParams(WindowManager.LayoutParams layoutParams) {
        if (layoutParams == null)
            throw new NullPointerException("LayoutParams not be null!");
        this.layoutParams = layoutParams;

    }

    /**
     * update View
     *
     * @param view
     */
    public void updateView(View view) {
        if (view == null)
            throw new NullPointerException("View not be null!");
        mView = view;
    }
}