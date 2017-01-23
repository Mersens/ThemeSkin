package com.mersens.themeskin.app;

import android.app.Application;
import android.content.Context;

import com.mersens.themeskin.loader.SkinHelper;

/**
 * Created by Mersens on 2017/1/12 09:41
 * Email:626168564@qq.com
 */

public class SkinApplication extends Application {
    private static SkinApplication mApp = null;
    private Context mContext;

    public SkinApplication() {

    }

    public static SkinApplication getInstance() {
        if (mApp == null) {
            synchronized (SkinApplication.class) {
                if (mApp == null) {
                    mApp = new SkinApplication();
                }
            }

        }
        return mApp;

    }




    @Override
    public void onCreate() {
        super.onCreate();
        mApp=this;
        SkinHelper.getInstance().init(this);

    }
}
