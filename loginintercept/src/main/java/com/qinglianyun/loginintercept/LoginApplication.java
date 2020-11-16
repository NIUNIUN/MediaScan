package com.qinglianyun.loginintercept;

import android.app.Application;

/**
 * Created by tang_xqing on 2020/11/12.
 */
public class LoginApplication extends Application {
    private static boolean sLoginFlag = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static boolean getLoginStatus() {
        return sLoginFlag;
    }

    public static void setLoginStatus(boolean loginStatus) {
        sLoginFlag = loginStatus;
    }
}

