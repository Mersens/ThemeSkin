package com.mersens.themeskin.loader;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.mersens.themeskin.listener.ISkinChangeCallBack;
import com.mersens.themeskin.listener.ISkinChangeListener;
import com.mersens.themeskin.utils.PrefUtils;

import java.io.File;
import java.lang.reflect.Method;
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
    private PrefUtils mPrefUtils;
    private Context mContext;
    private String suffix;
    private String mCurPath;
    private String mCurPkg;
    private int mColor = 0;
    private ResourceManager manager;
    private boolean isFromPlugin = false;

    private SkinHelper() {

    }

    public static SkinHelper getInstance() {
        if (mSkinHelper == null) {
            synchronized (SkinHelper.class) {
                if (mSkinHelper == null) {
                    mSkinHelper = new SkinHelper();
                }
            }
        }
        return mSkinHelper;
    }


    public ResourceManager getResourceManager() {
        if (!isFromPlugin) {

            return new ResourceManager(mContext.getResources(), mContext.getPackageName(), suffix);
        }
        return manager;
    }


    public void changeSkin(String suffix, int color) {
        isFromPlugin = false;
        clearPluginInfo();
        this.suffix = suffix;
        this.mColor = color;
        notifyChangeListener();
        mPrefUtils.saveSuffix(suffix);
        mPrefUtils.saveColor(color);
    }

    public void changeSkin(final String skin_plugin_path, final String skin_pkg, ISkinChangeCallBack iSkinChangeCallBack) {
        isFromPlugin = true;
        clearPluginInfo();
        final ISkinChangeCallBack finalCallBack = iSkinChangeCallBack;
        finalCallBack.onStart();
        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    loadPlugin(skin_plugin_path, skin_pkg);
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }

                return 0;
            }

            @Override
            protected void onPostExecute(Integer aVoid) {
                super.onPostExecute(aVoid);
                if (aVoid == -1) {
                    finalCallBack.onError(new Exception());
                    return;
                }
                notifyChangeListener();
                finalCallBack.onComplate();
                updatePluginInfo(skin_plugin_path, skin_pkg);
            }
        }.execute();

    }

    private void updatePluginInfo(String skin_plugin_path, String skin_pkg) {
        mPrefUtils.savePluginPath(skin_plugin_path);
        mPrefUtils.savePluginPkg(skin_pkg);
        mPrefUtils.saveColor(mColor);

    }

    private void loadPlugin(String skin_plugin_path, String skin_pkg) {
        if (skin_pkg.equals(mCurPkg) && skin_plugin_path.equals(mCurPath)) {
            return;
        }
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, skin_plugin_path);
            Resources resource = mContext.getResources();
            Resources mResource = new Resources(assetManager, resource.getDisplayMetrics(), resource.getConfiguration());
            manager = new ResourceManager(mResource, skin_pkg, null);
            mColor=manager.getColor("skin_item_color");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearPluginInfo() {
        mColor = 0;
        mCurPath=null;
        mCurPkg=null;
        suffix = null;
        mPrefUtils.clear();
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mPrefUtils = new PrefUtils(mContext);
        suffix = mPrefUtils.getSuffix();
        mColor = mPrefUtils.getColor();
        String path = mPrefUtils.getPluginPath();
        String pkg = mPrefUtils.getPluginPkg();
        File file = new File(path);
        if (file.exists()) {
            loadPlugin(path, pkg);
        }
        mCurPath = path;
        mCurPkg = pkg;
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
        for (ISkinChangeListener listener : mListener) {
            skinChange(listener);


        }
    }

    public void skinChange(ISkinChangeListener listener) {
        listener.onChanged(mColor);
        List<SkinView> skinViews = skinMaps.get(listener);
        for (SkinView sv : skinViews) {
            sv.apply();

        }
    }

    public boolean isNeedChangeSkin() {
        return usePlugin() || useSuffix();
    }

    private boolean usePlugin() {
        return mCurPath != null && !mCurPath.trim().equals("");
    }

    private boolean useSuffix() {
        return suffix != null && !suffix.trim().equals("");
    }

}
