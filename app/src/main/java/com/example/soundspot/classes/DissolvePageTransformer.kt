package com.example.soundspot.classes

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class DissolvePageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        if(position <= 0) {
            page.alpha = 1 - position
        }
        else {
            page.alpha = 0f
        }
    }
}