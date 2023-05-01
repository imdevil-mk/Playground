package com.imdevil.playground.view.scroll.nestedScrollingParent2Demo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat

class NestedScrollingParent2CollapsingLayout @JvmOverloads constructor(
    private val mContext: Context,
    private val attributeSet: AttributeSet,
    private val flag: Int = 0,
) : LinearLayout(mContext, attributeSet, flag), NestedScrollingParent2 {

    private var helper: NestedScrollingParentHelper = NestedScrollingParentHelper(this)
    private var headerHeight = 0
    private var updateListener: UpdateListener? = null

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        helper.onNestedScrollAccepted(child, target, axes)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        val acceptedY = calculateAccepted(target, 0, headerHeight, scrollY, dy)
        Log.d(
            TAG,
            "onNestedPreScroll: dx = $dy, dy = $dy, type = $type, topHeight = $headerHeight, scrollY = $scrollY, acceptedY = $acceptedY"
        )
        consumed[1] = acceptedY
        if (acceptedY == 0) {
            //Log.d(TAG, "onNestedPreScroll: it`s on the max state")
            return
        }
        scrollBy(0, acceptedY)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        val acceptedY = calculateAccepted(target, 0, headerHeight, scrollY, dyUnconsumed)
        Log.d(
            TAG,
            "onNestedScroll: dxConsumed = $dxConsumed, dyConsumed = $dyConsumed, dxUnconsumed = $dxUnconsumed, dyUnconsumed = $dyUnconsumed, type = $type, acceptedY = $acceptedY"
        )
        if (acceptedY == 0) {
            //Log.d(TAG, "onNestedPreScroll: it`s on the max state")
            return
        }
        scrollBy(0, acceptedY)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        helper.onStopNestedScroll(target)
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        Log.d(TAG, "onNestedPreFling: velocityX = $velocityX, velocityY = $velocityY")
        return false
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        Log.d(
            TAG,
            "onNestedFling: velocityX = $velocityX, velocityY = $velocityY, consumed = $consumed"
        )
        return false
    }

    override fun getNestedScrollAxes(): Int {
        return helper.nestedScrollAxes
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        headerHeight = getChildAt(0).measuredHeight - getChildAt(1).measuredHeight
    }

    override fun scrollBy(x: Int, y: Int) {
        super.scrollBy(x, y)
        updateListener?.update(scrollY / headerHeight.toFloat())
    }

    fun setUpdateListener(listener: UpdateListener) {
        updateListener = listener
    }

    private fun calculateAccepted(target: View, min: Int, max: Int, now: Int, d: Int): Int {
        var accepted = d
        val predict = now + accepted
        if (d < 0 && target.canScrollVertically(-1)) {
            accepted = 0
        } else if (predict < min) {
            accepted = -(now - min)
        } else if (predict > max) {
            accepted = max - now
        }
        return accepted
    }

    interface UpdateListener {
        fun update(ratio: Float)
    }

    companion object {
        private const val TAG = "NestedScrollingCollapse"
    }
}