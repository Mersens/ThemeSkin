package com.mersens.themeskin.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.mersens.themeskin.R;
import com.mersens.themeskin.app.Constant;
import com.mersens.themeskin.loader.SkinAttrType;
import com.mersens.themeskin.loader.SkinHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseSkinActivity implements ColorChooserDialog.ColorCallback {
    private DrawerLayout drawer;

    private LayoutInflater inflater;
    private Toolbar toolbar;
    private ListView listview;
    private ListView main_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        dynamicAddView(toolbar, SkinAttrType.BACKGROUND, R.color.skin_item_color, true);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.e("TAG", "onDrawerClosed=========");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.e("TAG", "onDrawerOpened=========");
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        listview = (ListView) findViewById(R.id.listView);
        main_listView = (ListView) findViewById(R.id.main_listView);
        final List<String> list = new ArrayList<>();
        list.add("应用内换肤");
        list.add("插件式换肤");

        listview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ViewHolder holder=null;
                if(view==null){
                    view=inflater.inflate(R.layout.layout_item,viewGroup,false);
                    holder=new ViewHolder();
                    holder.tv=(TextView) view.findViewById(R.id.tv_name);
                    view.setTag(holder);

                }else{
                    holder=(ViewHolder)view.getTag();
                }
                holder.tv.setText(list.get(i));
                return view;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        new ColorChooserDialog.Builder(MainActivity.this, R.string.theme)
                                .customColors(R.array.arraycolors, null)
                                .doneButton(R.string.done)
                                .cancelButton(R.string.cancel)
                                .allowUserColorInput(false)
                                .allowUserColorInput(false)
                                .show();
                        break;
                    case 1:
                        break;
                }

            }
        });
        final List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("Item " + i);
        }

        main_listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return datas.size();
            }

            @Override
            public Object getItem(int position) {
                return datas.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ViewHolder holder=null;
                if(view==null){
                    view=inflater.inflate(R.layout.layout_item,viewGroup,false);
                    holder=new ViewHolder();
                    holder.tv=(TextView) view.findViewById(R.id.tv_name);
                    view.setTag(holder);

                }else{
                    holder=(ViewHolder)view.getTag();
                }
                holder.tv.setText(datas.get(i));
                return view;
            }
        });
    }
    static class ViewHolder{
        TextView tv;
    }
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        String selectColor = null;

        if (selectedColor == 0) {
            return;
        } else if (selectedColor == getResources().getColor(R.color.skin_item_color_red)) {
            selectColor = Constant.RED;

        } else if (selectedColor == getResources().getColor(R.color.skin_item_color_purple)) {
            selectColor = Constant.PURPLE;

        } else if (selectedColor == getResources().getColor(R.color.skin_item_color_deep_purple)) {
            selectColor = Constant.DEEP_PURPLE;
        } else if (selectedColor == getResources().getColor(R.color.skin_item_color_blue)) {
            selectColor = Constant.BLUE;

        } else if (selectedColor == getResources().getColor(R.color.skin_item_color_light_blue)) {
            selectColor = Constant.LIGHT_BLUE;

        } else if (selectedColor == getResources().getColor(R.color.skin_item_color_teal)) {
            selectColor = Constant.TEAL;

        } else if (selectedColor == getResources().getColor(R.color.skin_item_color_green)) {
            selectColor = Constant.GREEN;
        } else if (selectedColor == getResources().getColor(R.color.skin_item_color_lime)) {
            selectColor = Constant.LIME;
        } else if (selectedColor == getResources().getColor(R.color.skin_item_color_brown)) {
            selectColor = Constant.BROWN;
        } else if (selectedColor == getResources().getColor(R.color.skin_item_color_yellow)) {
            selectColor = Constant.YELLOW;
        }
        if (selectColor != null) {
           if (selectedColor != 0) {
                changeStatusColor(selectedColor);
            }
            SkinHelper.getInstance().changeSkin(selectColor,selectedColor);

        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
