package com.mersens.themeskin.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;

import com.mersens.themeskin.loader.ISkinChangeListener;
import com.mersens.themeskin.loader.SkinFactory;
import com.mersens.themeskin.loader.SkinHelper;

/**
 * Created by Mersens on 2017/1/20
 * Email:626168564@qq.com
 */

public class BaseSkinActivity extends AppCompatActivity implements ISkinChangeListener {
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
    public void onChanged() {

    }
}
