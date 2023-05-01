package com.imdevil.playground.view.scroll.coordinatorLayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

class NestedHeaderBehavior constructor(
    private val context: Context,
    private val attrs: AttributeSet,
) : CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
                && coordinatorLayout.height - directTargetChild.height <= child.height
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (dy == 0) return
        val currentTop = child.top
        var consumedY = dy
        val newTop = currentTop - consumedY
        if (dy < 0) {
            // We`re scrolling down && the scroll_view can`t scroll now
            if (!target.canScrollVertically(-1)) {
                if (newTop > 0) {
                    consumedY = currentTop
                }
            } else {
                consumedY = 0
            }
        } else {
            // We`re scrolling up
            if (newTop < -child.height) {
                consumedY = child.height + currentTop
            }
        }
        Log.d(
            TAG,
            "onNestedPreScroll: dy = $dy, type = $type, childHeight = ${child.height}, childTop = ${child.top}, consumedY = $consumedY"
        )
        consumed[1] = consumedY
        if (consumedY != 0) {
            ViewCompat.offsetTopAndBottom(child, -consumedY)
        }
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyUnconsumed == 0) return
        val currentTop = child.top
        var consumedY = dyUnconsumed
        val newTop = currentTop - consumedY
        if (dyUnconsumed < 0) {
            // We`re scrolling down && the scroll_view can`t scroll now
            if (!target.canScrollVertically(-1)) {
                if (newTop > 0) {
                    consumedY = currentTop
                }
            } else {
                consumedY = 0
            }
        } else {
            // We`re scrolling up
            if (newTop < -child.height) {
                consumedY = child.height + currentTop
            }
        }
        Log.d(
            TAG,
            "onNestedScroll: dyUnconsumed = $dyUnconsumed, type = $type, childHeight = ${child.height}, childTop = ${child.top}, consumedY = $consumedY"
        )
        consumed[1] = consumedY
        if (consumedY != 0) {
            ViewCompat.offsetTopAndBottom(child, -consumedY)
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        type: Int
    ) {
        Log.d(TAG, "onStopNestedScroll: ")
    }


    companion object {
        private const val TAG = "NestedHeaderBehavior"
    }
}