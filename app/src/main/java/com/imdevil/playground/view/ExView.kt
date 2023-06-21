package com.imdevil.playground.view

import android.util.Log
import android.view.MotionEvent
import android.view.View
import okhttp3.internal.toHexString

fun MotionEvent.log(clzTag: String = "", methodTag: String = "") {
    val s = StringBuilder("")
    s.append(action.toHexString())
    s.append(" ${MotionEvent.actionToString(action)}")
    s.append(" pointerCount = $pointerCount")

    for (i in 0 until pointerCount) {
        s.append(" id[$i] = ${getPointerId(i)} x[$i] = ${getX(i)} y[$i] = ${getY(i)}")
    }

    Log.d(clzTag, "${methodTag}: $s")
}

fun View.goneOrVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleOrElse(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}