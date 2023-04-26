package com.imdevil.playground.view.scroll

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.LinearLayout
import com.imdevil.playground.view.log
import kotlin.math.abs

private const val TAG = "ScrollableLinearLayout"

class ScrollableLinearLayout @JvmOverloads constructor(
    private val mContext: Context,
    private val attributeSet: AttributeSet,
    private val flag: Int = 0,
) : LinearLayout(mContext, attributeSet, flag) {
    private var mTouchSlop = 0
    private var startY = 0f
    private var mFirstChildHeight = 0

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledPagingTouchSlop
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        ev.log("ScrollableLinearLayout", "onInterceptTouchEvent")
        return when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startY = ev.y
                false
            }
            MotionEvent.ACTION_MOVE -> {
                val dY = ev.y - startY
                val intercept =
                    if (abs(dY) > mTouchSlop / 4) canVerticalScrollable(dY.toInt()) else false
                intercept
            }
            else -> {
                super.onInterceptTouchEvent(ev)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //event.log("ScrollableLinearLayout", "onTouchEvent")
        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                var dY = -(event.y - startY).toInt()
                Log.d(
                    TAG,
                    "onTouchEvent: ${MotionEvent.actionToString(event.actionMasked)} x[0] = ${
                        event.getX(0)
                    } y[0] = ${event.getY(0)} dY = ${-dY} scrollY = $scrollY"
                )
                startY = event.y
                if (dY + scrollY > mFirstChildHeight) {
                    dY = mFirstChildHeight - scrollY
                } else if (dY + scrollY < -mFirstChildHeight) {
                    dY = scrollY - mFirstChildHeight
                }
                if (dY != 0) {
                    scrollBy(0, dY)
                }
            }
        }

        return super.onTouchEvent(event)
    }

    override fun scrollTo(x: Int, y: Int) {
        val finalY = if (y < 0) 0 else y
        super.scrollTo(x, finalY)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mFirstChildHeight = getChildAt(0).measuredHeight
    }

    private fun canVerticalScrollable(dY: Int): Boolean {
        Log.d(
            TAG,
            "canVerticalScrollable: dY = $dY , scrollY = $scrollY , mTouchSlop = $mTouchSlop, mFirstChildHeight = $mFirstChildHeight"
        )
        return if (dY <= 0) {
            mFirstChildHeight > scrollY
        } else {
            scrollY > 0
        }
    }
}