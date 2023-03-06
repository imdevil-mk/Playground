package com.imdevil.playground.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

class MyViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        val log = when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> "MotionEvent.ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "MotionEvent.ACTION_MOVE"
            MotionEvent.ACTION_UP -> "MotionEvent.ACTION_UP"
            else -> "else"
        }
        Log.d("TAG-MyViewGroup", "dispatchTouchEvent: $log")
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val log = when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> "MotionEvent.ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "MotionEvent.ACTION_MOVE"
            MotionEvent.ACTION_UP -> "MotionEvent.ACTION_UP"
            else -> "else"
        }
        Log.d("TAG-MyViewGroup", "onInterceptTouchEvent: $log")

        return when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> false
            MotionEvent.ACTION_MOVE -> true
            else -> super.onInterceptTouchEvent(ev)
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val log = when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> "MotionEvent.ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "MotionEvent.ACTION_MOVE"
            MotionEvent.ACTION_UP -> "MotionEvent.ACTION_UP"
            else -> "else"
        }
        Log.d("TAG-MyViewGroup", "onTouchEvent: $log")
        return super.onTouchEvent(event)
    }
}