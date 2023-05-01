package com.imdevil.playground.view.offset

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout

class HeaderFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // measure header
        val header = getChildAt(0)
        measureChildWithMargins(header, widthMeasureSpec, 0, heightMeasureSpec, 0)
        val headerLp = header.layoutParams as LayoutParams
        val headerHeight = header.measuredHeight

        val width =
            header.measuredWidth + headerLp.leftMargin + headerLp.rightMargin + paddingLeft + paddingRight
        val height = MeasureSpec.getSize(heightMeasureSpec)

        // measure content
        val content = getChildAt(1)
        val contentLp = content.layoutParams as LayoutParams
        val contentHeight = height - headerHeight
        val contentSpecMode =
            if (contentLp.height == ViewGroup.LayoutParams.MATCH_PARENT) MeasureSpec.EXACTLY else MeasureSpec.AT_MOST
        val contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(contentHeight, contentSpecMode)
        measureChildWithMargins(content, widthMeasureSpec, 0, contentHeightMeasureSpec, 0)

        setMeasuredDimension(width, height)

        Log.d(
            TAG,
            "onMeasure: width = ${MeasureSpec.toString(widthMeasureSpec)}， height = ${
                MeasureSpec.toString(heightMeasureSpec)
            }, headerH = $headerHeight, contentH = $contentHeight"
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.d(TAG, "onLayout: left = $left, top = $top, right = $right, bottom = $bottom")
        val header = getChildAt(0)
        val content = getChildAt(1)


        val headerHeight = header.measuredHeight
        val contentHeight = content.measuredHeight

        header.layout(left, top, right, headerHeight)

        content.layout(left, headerHeight, right, contentHeight + headerHeight)
    }

    companion object {
        private const val TAG = "HeaderFrameLayout"
    }
}