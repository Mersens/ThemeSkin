package com.mersens.themeskin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mersens.themeskin.app.Constant;

/**
 * Created by Mersens on 2017/1/12 09:58
 * Email:626168564@qq.com
 */

public class PrefUtils {
    private Context mContext;
    public PrefUtils(Context context){
        this.mContext=context;
    }

    public void saveSuffix(String pkg){
        SharedPreferences sp= mContext.getSharedPreferences(Constant.PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putString(Constant.SUFFIX,pkg).apply();

    }

    public String getSuffix(){
        SharedPreferences sp= mContext.getSharedPreferences(Constant.PREF_NAME,Context.MODE_PRIVATE);
        return sp.getString(Constant.SUFFIX,"");

    }
    public void saveColor(int color){
        SharedPreferences sp= mContext.getSharedPreferences(Constant.PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(Constant.COLOR,color).apply();

    }

    public int getColor(){
        SharedPreferences sp= mContext.getSharedPreferences(Constant.PREF_NAME,Context.MODE_PRIVATE);
        return sp.getInt(Constant.COLOR,0);

    }
    public void clear() {
        SharedPreferences sp= mContext.getSharedPreferences(Constant.PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

}
