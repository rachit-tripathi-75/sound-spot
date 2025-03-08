package com.rachit.tripathi75.soundspot.classes;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class DissolvePageTransformer implements ViewPager2.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        if (position <= 0) {
            page.setAlpha(1 - position);
        } else {
            page.setAlpha(0f);
        }
    }
}