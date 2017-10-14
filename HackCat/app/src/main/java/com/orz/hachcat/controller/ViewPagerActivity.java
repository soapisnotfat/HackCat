package com.orz.hachcat.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.orz.hachcat.R;
import com.orz.hachcat.controller.adapter.TestAdapter;


/**
 * Created by catherinezhuang on 10/14/17.
 */

public class ViewPagerActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        initViews();
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("title_activity_viewpager");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_demo);
        TestAdapter adapter = new TestAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }

}
