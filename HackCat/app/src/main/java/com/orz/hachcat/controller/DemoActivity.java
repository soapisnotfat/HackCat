package com.orz.hachcat.controller;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.orz.hachcat.R;

/**
 * Created by catherinezhuang on 10/14/17.
 */

public class DemoActivity extends Activity {

    private ProgressBar progressBar;
    private SwipeBackLayout swipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initViews();
    }

    private void initViews() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        swipeBackLayout = (SwipeBackLayout) findViewById(R.id.swipe_layout);
        swipeBackLayout.setEnableFlingBack(false);

        swipeBackLayout.setOnPullToBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                progressBar.setProgress((int) (progressBar.getMax() * fractionAnchor));
            }
        });
    }

}
