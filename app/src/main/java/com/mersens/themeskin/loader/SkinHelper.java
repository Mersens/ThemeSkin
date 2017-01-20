package com.mersens.themeskin.loader;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mersens on 2017/1/20 15:45
 * Email:626168564@qq.com
 */

public class SkinHelper {
    private Map<ISkinChangeListener, List<SkinView>> skinMaps = new HashMap<ISkinChangeListener, List<SkinView>>();
    private List<ISkinChangeListener> mListener = new ArrayList<ISkinChangeListener>();
    private static SkinHelper mSkinHelper;
    private Context mContext;
    private String suffix;
    private SkinHelper(){

    }

    public static SkinHelper getInstance(){
        if(mSkinHelper==null){
            synchronized (SkinHelper.class){
                if(mSkinHelper==null){
                    mSkinHelper=new SkinHelper();
                }
            }
        }
        return mSkinHelper;
    }


    public ResourceManager getResourceManager(){
        return new ResourceManager(mContext.getResources(),mContext.getPackageName(),suffix);
    }


    public void changeSkin(String suffix){
        this.suffix=suffix;
        notifyChangeListener();
    }
public void init(Context context){
    mContext=context.getApplicationContext();
}
    public List<SkinView> getSkinViews(ISkinChangeListener listener) {
        return skinMaps.get(listener);
    }

    public void addSkinView(ISkinChangeListener listener, List<SkinView> skinViews) {
        skinMaps.put(listener, skinViews);
    }

    public void registerListener(ISkinChangeListener listener) {
        mListener.add(listener);

    }

    public void unReigisterListener(ISkinChangeListener listener) {
        mListener.remove(listener);
        skinMaps.remove(listener);
    }

    private void notifyChangeListener() {
        for(ISkinChangeListener listener:mListener){
            skinChange(listener);
            listener.onChanged();

        }
    }

    public void skinChange(ISkinChangeListener listener) {
        List<SkinView> skinViews=skinMaps.get(listener);
        for(SkinView sv:skinViews){
            sv.apply();

        }
    }
}
