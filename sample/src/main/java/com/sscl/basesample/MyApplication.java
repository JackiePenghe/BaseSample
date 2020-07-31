package com.sscl.basesample;

import android.app.Application;
import android.util.Log;

import com.sscl.baselibrary.files.FileUtil;
import com.sscl.baselibrary.utils.CrashHandler;
import com.sscl.baselibrary.utils.DebugUtil;
import com.sscl.baselibrary.utils.Tool;

/**
 * @author alm
 * @date 2017/11/22 0022
 * Application类
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;

    public static void initCrashListener() {
        CrashHandler.getInstance().setOnExceptionListener(ex -> {
            Log.e("MyApplication", "onException: 自己对异常做的额外处理！！！！--------------------\n--------------------\n--------------------\n--------------------\n--------------------\n--------------------");
            Tool.restartApplication(myApplication);
        });
    }

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        DebugUtil.setDebugFlag(true);
        FileUtil.init(this);
    }

}
