package com.example.user.myyandextranslate.fragments;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by User on 24.04.2017.
 */

public class NavigationViewPager extends ViewPager implements View.OnTouchListener{

    public NavigationViewPager(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public NavigationViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


}
