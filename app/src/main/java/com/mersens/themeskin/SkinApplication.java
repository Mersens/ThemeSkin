package com.mersens.themeskin;

import android.app.Application;

import com.mersens.themeskin.loader.SkinHelper;

/**
 * Created by Mersens on 2017/1/12 09:41
 * Email:626168564@qq.com
 */

public class SkinApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SkinHelper.getInstance().init(this);

    }
}
