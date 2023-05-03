package com.imdevil.playground.view.track

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.imdevil.playground.view.log

class TrackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val circlePaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }
    private val downPoints: MutableMap<Int, PointF> = mutableMapOf()

    private val linePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }
    private val pointerPath: MutableMap<Int, Path> = mutableMapOf()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (point in downPoints.values) {
            canvas.drawCircle(point.x, point.y, 10f, circlePaint)
        }
        pointerPath.values.forEach {
            canvas.drawPath(it, linePaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        event.log("TrackView", "onTouchEvent")
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                val pointerId = event.getPointerId(index)
                val point = PointF(event.getX(index), event.getY(index))
                Log.d(TAG, "onTouchEvent: $pointerId DOWN")
                if (pointerPath.containsKey(pointerId)) {
                    pointerPath.remove(pointerId)
                }
                downPoints[pointerId] = point
                Path().also {
                    pointerPath[pointerId] = it
                    it.moveTo(point.x, point.y)
                }
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                val pointerCount = event.pointerCount

                for (i in 0 until pointerCount) {
                    val pointerId = event.getPointerId(i)
                    val point = PointF(event.getX(i), event.getY(i))
                    pointerPath[pointerId]?.lineTo(point.x, point.y)
                }

                invalidate()
            }
            /*/
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                val index = event.actionIndex
                val pointerId = event.getPointerId(index)
                val point = PointF(event.getX(index), event.getY(index))
                pointerPath[pointerId]?.lineTo(point.x, point.y)
                invalidate()
                Log.d(
                    TAG,
                    "onTouchEvent: ${MotionEvent.actionToString(event.action)} index = $index, pointerId = $pointerId"
                )
            }
            */
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                val index = event.actionIndex
                val pointerId = event.getPointerId(index)
                downPoints.remove(pointerId)
                pointerPath.remove(pointerId)
                invalidate()
            }
            else -> {
                super.onTouchEvent(event)
            }
        }
        return true
    }

    companion object {
        private const val TAG = "TrackView"
    }
}
