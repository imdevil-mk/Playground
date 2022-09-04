package com.imdevil.playground.view

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import com.imdevil.interview.view.MyView
import com.imdevil.interview.view.MyViewGroup
import com.imdevil.playground.R
import com.imdevil.playground.base.LogActivity

class TouchActivity : LogActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch)

        findViewById<MyViewGroup>(R.id.parent).setOnClickListener {
            Log.d(getLogTag(), "onCreate: parent")
        }

        findViewById<MyView>(R.id.child).setOnClickListener {
            Log.d(getLogTag(), "onCreate: child")
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val log = when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> "MotionEvent.ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "MotionEvent.ACTION_MOVE"
            MotionEvent.ACTION_UP -> "MotionEvent.ACTION_UP"
            else -> "else"
        }
        Log.d("TAG-${getLogTag()}", "dispatchTouchEvent: $log")
        return super.dispatchTouchEvent(ev)
    }
}