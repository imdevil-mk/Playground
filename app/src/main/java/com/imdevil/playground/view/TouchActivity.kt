package com.imdevil.playground.view

import android.os.Bundle
import android.util.Log
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

    /*
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.log(getLogTag(), "dispatchTouchEvent")
        return super.dispatchTouchEvent(ev)
    }
    */
}