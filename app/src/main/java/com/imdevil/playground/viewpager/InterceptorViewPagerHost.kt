package com.imdevil.playground.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import kotlin.math.absoluteValue

class InterceptorViewPagerHost : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f


    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        return handleInterceptTouchEvent(e)
    }

    private fun handleInterceptTouchEvent(e: MotionEvent): Boolean {
        if (scrollAvailable()) return false

        val intercept = if (e.action == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
            false
        } else if (e.action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - initialX
            val dy = e.x - initialX

            val intercept = if (dy.absoluteValue > dx.absoluteValue) {
                false
            } else {
                dx.absoluteValue > touchSlop
            }

            intercept
        } else {
            false
        }

        return intercept
    }

    private fun scrollAvailable(): Boolean {
        return if (findChildViewPager() == null) true else !StateHolder.checking
    }

    private fun findChildViewPager(): ViewPager? {
        var v: ViewPager? = null
        for (i in 0 until childCount) {
            if (getChildAt(i) is ViewPager) {
                v = getChildAt(i) as ViewPager
                break
            }
        }
        return v
    }
}