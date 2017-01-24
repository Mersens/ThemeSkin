package com.mersens.themeskin.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mersens.themeskin.listener.IDynamAddView;
import com.mersens.themeskin.listener.ISkinChangeListener;
import com.mersens.themeskin.loader.SkinFactory;
import com.mersens.themeskin.loader.SkinHelper;
import com.mersens.themeskin.statusbar.StatusBarUtil;

/**
 * Created by Mersens on 2017/1/20
 * Email:626168564@qq.com
 */

public  class BaseSkinActivity extends AppCompatActivity implements ISkinChangeListener ,IDynamAddView{
    private SkinFactory mSkinFactory;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSkinFactory=new SkinFactory();
        mSkinFactory.setAppCompatActivity(this);
        LayoutInflaterCompat.setFactory(getLayoutInflater(),mSkinFactory);
        SkinHelper.getInstance().registerListener(this);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinHelper.getInstance().unReigisterListener(this);
    }

    @Override
    public void onChanged(int color) {
            if(color!=0 ){
                changeStatusColor(color);

        }
    }

    protected void changeStatusColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusBarUtil statusBarBackground = new StatusBarUtil(
                    this, color);
            statusBarBackground.setStatusBarColor();
        }


    }

    @Override
    public void dynamicAddView(View view, String attrName, int resID,boolean isColors) {
        mSkinFactory.dynamicAddView(this,view,attrName,resID,isColors);
    }
}
