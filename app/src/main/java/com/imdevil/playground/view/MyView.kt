package com.imdevil.playground.view

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class MyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var start: PointF = PointF()

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        ev.log("TAG-MyView", "dispatchTouchEvent++++++++++")

        val f = when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                start.x = ev.x
                start.y = ev.y
                parent.requestDisallowInterceptTouchEvent(true)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                val parentIntercept = ev.y - start.y > 30

                parent.requestDisallowInterceptTouchEvent(!parentIntercept)

                true
            }
            else -> super.dispatchTouchEvent(ev)
        }

        Log.d("TAG-MyView", "dispatchTouchEvent++++++++++: $f")
        return f
    }

    /*
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.log("TAG-MyView", "onTouchEvent")
        return super.onTouchEvent(event)
    }
    */
}