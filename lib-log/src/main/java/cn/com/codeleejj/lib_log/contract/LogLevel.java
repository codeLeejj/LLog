package cn.com.codeleejj.lib_log.contract;


import android.util.Log;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface LogLevel {
    @IntDef({V, D, I, W, E, A})
    @Retention(RetentionPolicy.SOURCE)
    @interface LEVEL {
    }

    int V = Log.VERBOSE;
    int D = Log.DEBUG;
    int I = Log.INFO;
    int W = Log.WARN;
    int E = Log.ERROR;
    int A = Log.ASSERT;
}
