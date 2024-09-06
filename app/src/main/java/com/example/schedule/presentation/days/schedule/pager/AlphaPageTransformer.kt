package com.example.schedule.presentation.days.schedule.pager

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class AlphaPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            alpha = when {
                position < -1 -> 0f

                position <= 0 -> 1f

                position <= 1 -> 1 - position

                else -> 0f
            }
        }
    }
}