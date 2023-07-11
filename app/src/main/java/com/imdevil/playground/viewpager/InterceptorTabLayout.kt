package com.imdevil.playground.viewpager

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.android.material.tabs.TabLayout

private const val TAG = "InterceptorTabLayout"

class InterceptorTabLayout @JvmOverloads constructor(
    @NonNull context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {

    override fun selectTab(tab: Tab?, updateIndicator: Boolean) {
        if (StateHolder.checking) {
            Log.d(TAG, "selectTab: ")
        } else {
            super.selectTab(tab, updateIndicator)
        }
    }

    override fun setScrollPosition(
        position: Int,
        positionOffset: Float,
        updateSelectedText: Boolean,
        updateIndicatorPosition: Boolean
    ) {
        super.setScrollPosition(
            position,
            positionOffset,
            updateSelectedText,
            updateIndicatorPosition
        )
    }
}