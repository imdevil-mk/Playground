package com.imdevil.playground.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class MyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        val log = when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> "MotionEvent.ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "MotionEvent.ACTION_MOVE"
            MotionEvent.ACTION_UP -> "MotionEvent.ACTION_UP"
            MotionEvent.ACTION_CANCEL -> "MotionEvent.ACTION_CANCEL"
            else -> "else"
        }
        Log.d("TAG-MyView", "dispatchTouchEvent: $log")
        return false
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val log = when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> "MotionEvent.ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "MotionEvent.ACTION_MOVE"
            MotionEvent.ACTION_UP -> "MotionEvent.ACTION_UP"
            MotionEvent.ACTION_CANCEL -> "MotionEvent.ACTION_CANCEL"
            else -> "else"
        }
        Log.d("TAG-MyView", "onTouchEvent: $log")
        return true
    }
}