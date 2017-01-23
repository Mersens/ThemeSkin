package com.mersens.themeskin.loader;

import android.view.View;

/**
 * Created by Mersens on 2017/1/19
 * Email:626168564@qq.com
 */

public class SkinAttr {
    private String resName;
    private SkinAttrType mType;

    public SkinAttr(String resName, SkinAttrType mType) {
        this.resName = resName;
        this.mType = mType;
    }

    public SkinAttrType getmType() {
        return mType;
    }

    public void setmType(SkinAttrType mType) {
        this.mType = mType;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }
    public void apply(View view) {
        mType.apply(view,resName);
    }

}
