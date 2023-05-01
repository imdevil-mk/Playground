package com.imdevil.playground.view.scroll.nestedScrollingParent

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import kotlin.math.abs


class NestedScrollingParentLayout @JvmOverloads constructor(
    private val mContext: Context,
    private val attributeSet: AttributeSet,
    private val flag: Int = 0,
) : LinearLayout(mContext, attributeSet, flag), NestedScrollingParent {

    private var helper: NestedScrollingParentHelper = NestedScrollingParentHelper(this)
    private val mValueAnimator: ValueAnimator by lazy {
        ValueAnimator().also {
            it.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                scrollTo(0, animatedValue)
            }
        }
    }

    private var headerHeight = 0

    /**
     * 当前View表明自己是否接受这次的滑动
     *
     * @param target   具体嵌套滑动的那个子类
     * @param child    当前View的子View
     * @param axes     当前滑动的方向
     */
    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    /**
     * 无需自己处理，交给 {@link androidx.core.view.NestedScrollingParentHelper} 处理
     */
    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        helper.onNestedScrollAccepted(child, target, axes)
    }

    /**
     * 在嵌套滑动的子View未滑动之前，判断父view是否优先与子view处理(也就是父view可以先消耗，然后给子view消耗）
     *
     * @param target   具体嵌套滑动的那个子类
     * @param dx       水平方向嵌套滑动的子View想要变化的距离
     * @param dy       垂直方向嵌套滑动的子View想要变化的距离 dy<0向下滑动 dy>0 向上滑动
     * @param consumed 这个参数要我们在实现这个函数的时候指定，回头告诉子View当前父View消耗的距离
     *                 consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离 好让子view做出相应的调整
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        val acceptedY = calculateAccepted(0, headerHeight, scrollY, dy)
        Log.d(
            TAG,
            "onNestedPreScroll: dx = $dy, dy = $dy, topHeight = $headerHeight, scrollY = $scrollY, acceptedY = $acceptedY"
        )
        consumed[1] = acceptedY
        if (acceptedY == 0) {
            //Log.d(TAG, "onNestedPreScroll: it`s on the max state")
            return
        }
        scrollBy(0, acceptedY)
    }

    /**
     * 嵌套滑动的子View在滑动之后，判断父view是否继续处理（也就是父消耗一定距离后，子再消耗，最后判断父消耗不）
     *
     * @param target       具体嵌套滑动的那个子类
     * @param dxConsumed   水平方向嵌套滑动的子View滑动的距离(消耗的距离)
     * @param dyConsumed   垂直方向嵌套滑动的子View滑动的距离(消耗的距离)
     * @param dxUnconsumed 水平方向嵌套滑动的子View未滑动的距离(未消耗的距离)
     * @param dyUnconsumed 垂直方向嵌套滑动的子View未滑动的距离(未消耗的距离)
     */
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        val acceptedY = calculateAccepted(0, headerHeight, scrollY, dyUnconsumed)
        Log.d(
            TAG,
            "onNestedScroll: dxConsumed = $dxConsumed, dyConsumed = $dyConsumed, dxUnconsumed = $dxUnconsumed, dyUnconsumed = $dyUnconsumed, acceptedY = $acceptedY"
        )
        if (acceptedY == 0) {
            //Log.d(TAG, "onNestedPreScroll: it`s on the max state")
            return
        }
        scrollBy(0, acceptedY)
    }

    override fun onStopNestedScroll(target: View) {
        helper.onStopNestedScroll(target)
    }

    /**
     * 当子view产生fling滑动时，判断父view是否处拦截fling，如果父View处理了fling，那子view就没有办法处理fling了。
     *
     * @param target    具体嵌套滑动的那个子类
     * @param velocityX 水平方向上的速度 velocityX > 0  向左滑动，反之向右滑动
     * @param velocityY 竖直方向上的速度 velocityY > 0  向上滑动，反之向下滑动
     * @return 父view是否拦截该fling,返回true则子View无法处理这次的Fling事件了
     */
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        //Log.d(TAG, "onNestedPreFling: velocityX = $velocityX, velocityY = $velocityY")
        return false
    }

    /**
     * 当父view不拦截该fling,那么子view会将fling传入父view
     *
     * @param target    具体嵌套滑动的那个子类
     * @param velocityX 水平方向上的速度 velocityX > 0  向左滑动，反之向右滑动
     * @param velocityY 竖直方向上的速度 velocityY > 0  向上滑动，反之向下滑动
     * @param consumed  子view是否可以消耗该fling，也可以说是子view是否消耗掉了该fling
     * @return 父view是否消耗了该fling
     */
    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        val scrolledY = abs(scrollY)
        val end: Int
        // bottom --> top [scrollY, headerHeight]
        val left = if (velocityY > 0) {
            end = headerHeight
            headerHeight - scrolledY
        } else {
            // top --> bottom [scrollY, 0]
            end = 0
            scrolledY
        }
        Log.d(
            TAG,
            "onNestedFling: velocityX = $velocityX, velocityY = $velocityY, consumed = $consumed, left = $left, [$scrollY, $end]"
        )
        if (left == 0) {
            return false
        }

        val duration = abs(left / velocityY).toLong()
        startFling(duration, scrollY, end)

        return true
    }

    override fun getNestedScrollAxes(): Int {
        return helper.nestedScrollAxes
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        headerHeight = getChildAt(0).measuredHeight
    }

    private fun calculateAccepted(min: Int, max: Int, now: Int, d: Int): Int {
        var accepted = d
        val predict = now + accepted
        if (predict < min) {
            accepted = -(now - min)
        } else if (predict > max) {
            accepted = max - now
        }
        return accepted
    }

    private fun startFling(duration: Long, startY: Int, endY: Int) {
        mValueAnimator.cancel()
        mValueAnimator.interpolator = DecelerateInterpolator()
        mValueAnimator.setIntValues(startY, endY)
        mValueAnimator.duration = duration
        mValueAnimator.start()
    }

    companion object {
        private const val TAG = "NestedScrollingParent"
    }
}