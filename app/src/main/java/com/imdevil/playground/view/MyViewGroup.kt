package com.imdevil.playground.view

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

class MyViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.log("TAG-MyViewGroup", "dispatchTouchEvent---------------")
        val f = super.dispatchTouchEvent(ev)
        Log.d("TAG-MyViewGroup", "dispatchTouchEvent---------------: $f")
        return f
    }

    private var start: PointF = PointF()

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev?.log("TAG-MyViewGroup", "onInterceptTouchEvent------")

        val f = when (ev?.actionMasked) {
            // 外部拦截法
            /*
            MotionEvent.ACTION_DOWN -> {
                start.x = ev.x
                start.y = ev.y
                false
            }
            MotionEvent.ACTION_MOVE -> {
                ev.y - start.y > 30
            }
            MotionEvent.ACTION_UP -> {
                false
            }
            */
            else -> super.onInterceptTouchEvent(ev)
        }

        Log.d("TAG-MyViewGroup", "onInterceptTouchEvent------: $f")
        return f
    }

    /*
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.log("TAG-MyViewGroup", "onTouchEvent")
        return super.onTouchEvent(event)
    }
    */
}

/*

1, ViewGroup默认不会拦截事件， View会
2，如果View拦截了DOWN事件，那么ViewGroup是有机会再次拦截事件的,取决于ViewGroup的onInterceptTouchEvent()方法和
   FLAG_DISALLOW_INTERCEPT标记。如果ViewGroup拦截中间的事件，那么本次处理的事件会变成CANCEL传递给View, 同时由于
   事件被ViewGroup拦截了，ViewGroup的onInterceptTouchEvent()不会被调用了。View也不会接收后续的事件了。
3，只要拦截了DOWN事件，对MOVE事件返回false,仍然可以接收到后续事件，因为mFirstTouchTarget != null
4，View拦截DOWN事件并请求parent.requestDisallowInterceptTouchEvent(true)，而后在View在处理某个MOVE时请求
   parent.requestDisallowInterceptTouchEvent(false),则ViewGroup的onInterceptTouchEvent()可以接收到接下
   来的事件
5，View拦截了DOWN事件，ViewGroup的onTouchEvent()不会接收到DOWN事件，而后ViewGroup在onInterceptTouchEvent()
   中拦截了MOVE事件，ViewGroup的onTouchEvent()不会接受到这个MOVE事件，从下个MOVE开始接受。
 */