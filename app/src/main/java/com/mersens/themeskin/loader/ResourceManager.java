package com.mersens.themeskin.loader;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * Created by Mersens on 2017/1/19
 * Email:626168564@qq.com
 */

public class ResourceManager {

    private Resources mResource;
    private String mPkgName;
    private String mSuffix;

    public ResourceManager( Resources resources, String pkg, String suffix){
        this.mResource=resources;
        this.mPkgName=pkg;
        if(suffix==null){
            suffix="";
        }
        this.mSuffix=suffix;
    }
    private String appendSufix(String name) {
        if(!TextUtils.isEmpty(mSuffix)){
            name+="_"+mSuffix;
        }
        return  name;
    }
    public Drawable getDrawableByName(String name){
        try {
            name=appendSufix(name);
            return mResource.getDrawable(mResource.getIdentifier(name,"drawable",mPkgName));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public ColorStateList getColorByName(String name){
        try {
            name=appendSufix(name);
            return  mResource.getColorStateList(mResource.getIdentifier(name,"color",mPkgName));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    public int getColor(String name){
        try {
            name=appendSufix(name);
            return mResource.getIdentifier(name,"color",mPkgName) ;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }
}
