package com.imdevil.playground.view.scroll.coordinatorLayout

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat


class NestedScrollingBehavior constructor(
    private val context: Context,
    private val attrs: AttributeSet,
) : CoordinatorLayout.Behavior<View>(context, attrs) {

    private val mTempRect1 = Rect()
    private val mTempRect2 = Rect()

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is TextView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        Log.d(TAG, "onDependentViewChanged: ")
        offsetChildAsNeeded(child, dependency)
        return false
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: View, dependency: View) {
        Log.d(TAG, "onDependentViewRemoved: ")
    }

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        val childLpHeight = child.layoutParams.height
        if (childLpHeight == LayoutParams.MATCH_PARENT || childLpHeight == LayoutParams.WRAP_CONTENT) {
            val dependencyHeader = parent.getDependencies(child)[0]
            if (ViewCompat.getFitsSystemWindows(dependencyHeader)
                && !ViewCompat.getFitsSystemWindows(child)
            ) {
                child.fitsSystemWindows = true
                if (ViewCompat.getFitsSystemWindows(child)) {
                    child.requestLayout()
                    return true
                }
            }
            var availableHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec)
            if (availableHeight == 0) {
                availableHeight = parent.height
            }
            Log.d(
                TAG,
                "onMeasureChild: availableHeight = $availableHeight, heightUsed = $heightUsed, parentHeight = ${parent.height}"
            )
            val specMode =
                if (childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT) View.MeasureSpec.EXACTLY else View.MeasureSpec.AT_MOST
            val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(availableHeight, specMode)
            parent.onMeasureChild(
                child,
                parentWidthMeasureSpec,
                widthUsed,
                heightMeasureSpec,
                heightUsed
            )
            return true
        }
        return false
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        val dependencyHeader = parent.getDependencies(child)[0]

        val childLp = child.layoutParams as CoordinatorLayout.LayoutParams
        val available = mTempRect1
        available.set(
            parent.paddingLeft + childLp.leftMargin,
            dependencyHeader.bottom + childLp.topMargin,
            parent.width - parent.paddingRight - childLp.rightMargin,
            parent.height + dependencyHeader.bottom - parent.paddingBottom - childLp.bottomMargin
        )

        val out = mTempRect2
        GravityCompat.apply(
            resolveGravity(childLp.gravity), child.measuredWidth,
            child.measuredHeight, available, out, layoutDirection
        )

        child.layout(out.left, out.top, out.right, out.bottom)

        Log.d(
            TAG,
            "onLayoutChild: header = [${dependencyHeader.left}, ${dependencyHeader.top}, ${dependencyHeader.right}, ${dependencyHeader.bottom}]; child = ${out.toString()}"
        )
        return true
    }

    private fun resolveGravity(gravity: Int): Int {
        return if (gravity == Gravity.NO_GRAVITY) GravityCompat.START or Gravity.TOP else gravity
    }

    private fun offsetChildAsNeeded(child: View, dependency: View) {
        ViewCompat.offsetTopAndBottom(child, dependency.bottom - child.top)
    }

    companion object {
        private const val TAG = "NestedScrollingBehavior"
    }
}