package com.mersens.themeskin.loader;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mersens on 2017/1/10 16:27
 * Email:626168564@qq.com
 */

public class   SkinAttrType {
    public static final String BACKGROUND="background";
    public static final String TEXTCOLOR="textColor";
    public static final String SRC="src";

    public static final String BACKGROUND_DRAWABLE="drawable";
    public static final String BACKGROUND_COLOR="color";

    private String resType;
    private String backgroundType;
    public SkinAttrType(String type,String bgType){
        resType=type;
        backgroundType=bgType;
    }

    public  void apply(View view, String resName) {
        if (resType.equals(BACKGROUND)) {
            if (backgroundType.equals(BACKGROUND_DRAWABLE)) {
                Drawable drawable = SkinHelper.getInstance().getResourceManager().getDrawableByName(resName);
                if (drawable != null) {
                    view.setBackgroundDrawable(drawable);
                }
            } else if (backgroundType.equals(BACKGROUND_COLOR)) {
                {
/*                    int color = SkinHelper.getInstance().getResourceManager().getColorByName(resName);
                    if (color != -1) {
                        view.setBackgroundColor(color);

                    }*/
                }

            } else if (resType.equals(TEXTCOLOR)) {
                ColorStateList color = SkinHelper.getInstance().getResourceManager().getColorByName(resName);
                if (color != null) {
                    if (view instanceof TextView) {
                        TextView textView = (TextView) view;
                        textView.setTextColor(color);
                    }
                }

            } else if (resType.equals(SRC)) {
                Drawable drawable = SkinHelper.getInstance().getResourceManager().getDrawableByName(resName);
                if (drawable != null) {
                    if (view instanceof ImageView) {
                        ImageView imageView = (ImageView) view;
                        imageView.setImageDrawable(drawable);
                    }
                }

            }

        }


    }

}
