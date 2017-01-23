package com.mersens.themeskin.loader;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.View;

import com.mersens.themeskin.app.Constant;
import com.mersens.themeskin.app.SkinApplication;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mersens on 2017/1/20
 * Email:626168564@qq.com
 */

public class SkinFactory implements LayoutInflaterFactory {
    private AppCompatActivity mAppCompatActivity;
    private final Object[] mConstructorArgs = new Object[2];
    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."

    };
    private static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    private static final Map<String, Constructor<? extends View>> sConstructorMap
            = new ArrayMap<>();
    private Method mMethod = null;
    static final Class[] sCreatViewSingnature = new Class[]{View.class, String.class,
            Context.class, AttributeSet.class};
    private final Object[] mCreatViewArgs = new Object[4];
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        AppCompatDelegate delegate = mAppCompatActivity.getDelegate();
        //重写appcompat factory的初始化工作
        View view = null;
        List<SkinAttr> skinAttrs=null;
        try {
            if (mMethod == null) {
                mMethod = delegate.getClass().getMethod("createView", sCreatViewSingnature);
            }
            mCreatViewArgs[0] = parent;
            mCreatViewArgs[1] = name;
            mCreatViewArgs[2] = context;
            mCreatViewArgs[3] = attrs;
            view = (View) mMethod.invoke(delegate,mCreatViewArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        skinAttrs=getSkinAttrs(attrs,context);
        if(skinAttrs.isEmpty()){
            return null ;
        }
        if (view == null) {
            //构建View
            view = createViewFromTag(context, name, attrs);
        }
        if (view != null) {
            injectSkin(view,skinAttrs);
        }
        return view;
    }

    private void injectSkin(View view, List<SkinAttr> skinAttrs) {
        List<SkinView> skinViews = SkinHelper.getInstance().getSkinViews((ISkinChangeListener)mAppCompatActivity);
        if(skinViews==null){
            skinViews=new ArrayList<SkinView>();
            SkinHelper.getInstance().addSkinView((ISkinChangeListener)mAppCompatActivity,skinViews);
        }
        skinViews.add(new SkinView(view,skinAttrs));
        if(SkinHelper.getInstance().isNeedChangeSkin()){
            SkinHelper.getInstance().skinChange((ISkinChangeListener)mAppCompatActivity);
        }

    }

    private View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }

        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (-1 == name.indexOf('.')) {
                for (int i = 0; i < sClassPrefixList.length; i++) {
                    final View view = createView(context, name, sClassPrefixList[i]);
                    if (view != null) {
                        return view;
                    }
                }
                return null;
            } else {
                return createView(context, name, null);
            }
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    private View createView(Context context, String name, String prefix)
            throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }


    private List<SkinAttr> getSkinAttrs(AttributeSet attributeSet, Context context){
        List<SkinAttr> mSkinAttrs=new ArrayList<SkinAttr>();
        SkinAttrType mSkinAttrType=null;
        SkinAttr mSkinAttr=null;
        for(int i=0,n=attributeSet.getAttributeCount();i<n;i++){
            String attrName=attributeSet.getAttributeName(i);
            String attrVal=attributeSet.getAttributeValue(i);
            if(attrVal.startsWith("@")){
                int id=-1;
                try{
                    id=Integer.parseInt(attrVal.substring(1));
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(id==-1){
                    continue;
                }
                String resName=context.getResources().getResourceEntryName(id);
                if(resName.startsWith(Constant.SKIN_PREFIX)){
                    if(attrName.equals(SkinAttrType.BACKGROUND)){
                        mSkinAttrType=new SkinAttrType(attrName,SkinAttrType.BACKGROUND_COLOR);
                    }else if(attrName.equals(SkinAttrType.SRC)){
                        mSkinAttrType=new SkinAttrType(attrName,SkinAttrType.BACKGROUND_DRAWABLE);

                    }else if(attrName.equals(SkinAttrType.TEXTCOLOR)){
                        mSkinAttrType=new SkinAttrType(attrName,SkinAttrType.BACKGROUND_DRAWABLE);
                    }
                }

                if(mSkinAttrType==null) continue;
                mSkinAttr=new SkinAttr(resName,mSkinAttrType);
                mSkinAttrs.add(mSkinAttr);
            }
        }
        return mSkinAttrs;
    }



    public void dynamicAddView(ISkinChangeListener listener,View view, String attrname, int attrRes,boolean isColors){
        List<SkinAttr> mSkinAttrs=new ArrayList<SkinAttr>();
        SkinAttr mSkinAttr=null;
        SkinAttrType mSkinAttrType=null;
        String entryName = SkinApplication.getInstance().getResources().getResourceEntryName(attrRes);
        if(isColors){
            mSkinAttrType=new SkinAttrType(attrname,SkinAttrType.BACKGROUND_COLOR);
        }else {
            mSkinAttrType=new SkinAttrType(attrname,SkinAttrType.BACKGROUND_DRAWABLE);
        }
        if(mSkinAttrType!=null){
            mSkinAttr=new SkinAttr(entryName,mSkinAttrType);
            mSkinAttrs.add(mSkinAttr);
        }
        SkinView skinView=new SkinView(view,mSkinAttrs);
        List<SkinView> skinViews=new ArrayList<>();
        skinViews.add(skinView);
        List<SkinView> skinViewsAll = SkinHelper.getInstance().getSkinViews((ISkinChangeListener)mAppCompatActivity);
        if(skinViewsAll!=null){
            skinViewsAll.addAll(skinViews);
        }
        SkinHelper.getInstance().addSkinView((ISkinChangeListener)mAppCompatActivity,skinViewsAll);

        SkinHelper.getInstance().skinChange(listener);
    }
    public void setAppCompatActivity(AppCompatActivity activity){
        this.mAppCompatActivity=activity;

    }
}
