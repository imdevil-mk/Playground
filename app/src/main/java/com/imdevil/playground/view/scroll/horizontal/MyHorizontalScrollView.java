package com.imdevil.playground.view.scroll.horizontal;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.imdevil.playground.R;

import java.util.List;

/*
 * https://blog.csdn.net/shensky711/article/details/115624628
 * https://juejin.cn/post/6844903922671288333
 * https://juejin.cn/post/7447843704495849498
 */
public class MyHorizontalScrollView extends FrameLayout {
    private static final String TAG = "MyHorizontalScrollView";
    private static final int ANIMATED_SCROLL_GAP = 250;
    private int mLastMotionX;
    private boolean mIsBeingDragged = false;

    private long mLastScroll;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int mTouchSlop;
    private int mOverscrollDistance;
    private int mOverflingDistance;
    private int mMinimumVelocity;
    private int mMaximumVelocity;

    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;

    public MyHorizontalScrollView(@NonNull Context context) {
        this(context, null);
    }

    public MyHorizontalScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.myHorizontalScrollViewStyle);
    }

    public MyHorizontalScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyHorizontalScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initScrollView();
    }

    private void initScrollView() {
        mScroller = new OverScroller(getContext());

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mOverscrollDistance = configuration.getScaledOverscrollDistance();
        mOverflingDistance = configuration.getScaledOverflingDistance();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int horizontalPadding = getPaddingLeft() + getPaddingRight();
        int verticalPadding = getPaddingTop() + getPaddingBottom();

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - horizontalPadding),
                MeasureSpec.UNSPECIFIED
        );

        int heightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, verticalPadding, lp.height);

        child.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        int horizontalPadding = getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin;
        int verticalPadding = getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin;

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - horizontalPadding - widthUsed),
                MeasureSpec.UNSPECIFIED
        );

        int heightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, verticalPadding + heightUsed, lp.height);

        child.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_MOVE && mIsBeingDragged) {
            return true;
        }

        if (super.onInterceptTouchEvent(ev)) {
            return true;
        }

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final int x = (int) ev.getX();
                if (!inChild((int) ev.getX(), (int) ev.getY())) {
                    mIsBeingDragged = false;
                    recycleVelocityTracker();
                    break;
                }

                mLastMotionX = x;
                mActivePointerId = ev.getPointerId(0);

                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);

                mIsBeingDragged = !mScroller.isFinished();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    break;
                }

                final int activePointerIndex = ev.findPointerIndex(activePointerId);
                if (activePointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + activePointerId
                            + " in onInterceptTouchEvent");
                    break;
                }

                final int x = (int) ev.getX(activePointerIndex);
                final int xDiff = Math.abs(x - mLastMotionX);
                if (xDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                    mLastMotionX = x;
                    initVelocityTrackerIfNotExists();
                    mVelocityTracker.addMovement(ev);

                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;

                break;
            }
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);

        final int action = event.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (getChildCount() == 0) {
                    return false;
                }
                if (!mScroller.isFinished()) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                mLastMotionX = (int) event.getX();
                mActivePointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = event.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
                    break;
                }
                final int x = (int) event.getX(activePointerIndex);
                int deltaX = mLastMotionX - x;
                if (!mIsBeingDragged && Math.abs(deltaX) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    mLastMotionX = x;

                    final int oldX = getScrollX();
                    final int range = getScrollRange();
                    final int overScrollMode = getOverScrollMode();
                    final boolean canOverScroll = overScrollMode == OVER_SCROLL_ALWAYS ||
                            (overScrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);
                    Log.d(TAG, "onTouchEvent: " + MotionEvent.actionToString(action) + " canOverScroll = " + canOverScroll + " delta = [" + deltaX + ",0] scroll = [" + getScrollX() + ",0] scrollRange = [" + range + ",0]  maxOverScroll = [" + mOverscrollDistance + ",0]");
                    overScrollBy(deltaX, 0, getScrollX(), 0, range, 0, getMaxOverScrollRange(), 0, true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getXVelocity(mActivePointerId);
                    if (getChildCount() > 0) {
                        if (Math.abs(initialVelocity) > mMinimumVelocity) {
                            Log.d(TAG, "onTouchEvent: " + MotionEvent.actionToString(action) + " filing initialVelocity = " + initialVelocity + " scroll = [" + getScrollX() + "," + getScrollY() + "]");
                            filing(-initialVelocity);
                        } else {
                            if (mScroller.springBack(getScrollX(), getScrollY(), 0, getScrollRange(), 0, 0)) {
                                Log.d(TAG, "onTouchEvent: " + MotionEvent.actionToString(action) + " springBack initialVelocity = " + initialVelocity + " scroll = [" + getScrollX() + "," + getScrollY() + "]");
                                postInvalidateOnAnimation();
                            } else {
                                Log.d(TAG, "onTouchEvent: " + MotionEvent.actionToString(action) + " initialVelocity = " + initialVelocity + " scroll = [" + getScrollX() + "," + getScrollY() + "]");
                            }
                        }
                    }

                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;
                    recycleVelocityTracker();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "onTouchEvent: " + MotionEvent.actionToString(action) + " scroll = [" + getScrollX() + "," + getScrollY() + "]");
                if (mIsBeingDragged && getChildCount() == 0) {
                    if (mScroller.springBack(getScrollX(), getScrollY(), 0, getScrollRange(), 0, 0)) {
                        postInvalidateOnAnimation();
                    }
                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;
                    recycleVelocityTracker();
                }
        }

        return true;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (!mScroller.isFinished()) {
            Log.d(TAG, "onOverScrolled: 1 scroll = [" + scrollX + "," + scrollY + "] " + clampedX + " " + clampedY);
            final int oldX = getScrollX();
            final int oldY = getScrollY();

            setScrollX(scrollX);
            setScrollY(scrollY);

            onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);
            if (clampedX) {
                mScroller.springBack(getScrollX(), getScrollY(), 0, getScrollRange(), 0, 0);
            }
        } else {
            Log.d(TAG, "onOverScrolled: 2 scrollX = " + scrollX + " scrollY = " + scrollY + " " + clampedX + " " + clampedY);
            super.scrollTo(scrollX, scrollY);
        }

        awakenScrollBars();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();

            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            if (oldX != x || oldY != y) {
                final int range = getScrollRange();
                final int overscrollMode = getOverScrollMode();
                final boolean canOverscroll = overscrollMode == OVER_SCROLL_ALWAYS ||
                        (overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);
                Log.d(TAG, "computeScroll: scroll = [" + oldX + "," + oldY + "] --> now = [" + x + "," + y + "] canOverscroll = " + canOverscroll);

                overScrollBy(x - oldX, y - oldY, oldX, oldY, range, 0,
                        getMaxOverScrollRange(), 0, false);
                onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);
            } else {
                Log.d(TAG, "computeScroll: scroll = [" + oldX + "," + oldY + "]");
            }

            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
            }
        } else {
            Log.d(TAG, "computeScroll: ");
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        /*if (getChildCount() > 0) {
            View child = getChildAt(0);
            int clampedX = MathUtils.clamp(x, -getMaxOverScrollRange(), getMaxOverScrollRange() + child.getWidth() + getWidth() - getPaddingLeft() - getPaddingRight());
            int clampedY = clamp(y, getHeight() - getPaddingTop() - getPaddingBottom(), child.getHeight());
            Log.d(TAG, "scrollTo: scrolled = [" + getScrollX() + "," + getScrollY() + "] [" + x + "," + y + "] --> [" + clampedX + "," + clampedY + "]");
            if (clampedX != getScrollX() || clampedY != getScrollY()) {
                super.scrollTo(clampedX, clampedY);
            }
        }*/

        super.scrollTo(x, y);
    }

    public void filing(int velocityX) {
        if (getChildCount() > 0) {
            int width = getWidth() - getPaddingLeft() - getPaddingRight();
            int right = getChildAt(0).getRight() - getPaddingLeft();

            int maxScroll = Math.max(0, right - width);

            Log.d(TAG, "filing: " + velocityX + " maxScroll = " + maxScroll + " scroll = [" + getScrollX() + "," + getScrollY() + "]");

            {
                mScroller.fling(getScrollX(), getScrollY(), velocityX, 0, 0, getScrollRange(), 0, 0, getMaxOverScrollRange() / 2, 0);

                final boolean movingRight = velocityX > 0;

                View currentFocused = findFocus();
                View newFocused = findFocusableViewInMyBounds(movingRight, mScroller.getFinalX(), currentFocused);
                if (newFocused == null) {
                    newFocused = this;
                }
                if (newFocused != currentFocused) {
                    newFocused.requestFocus(movingRight ? View.FOCUS_RIGHT : View.FOCUS_LEFT);
                }
            }

            postInvalidateOnAnimation();
        }
    }

    public final void smoothScrollBy(int dx, int dy) {
        if (getChildCount() == 0) {
            // Nothing to do.
            return;
        }
        long duration = AnimationUtils.currentAnimationTimeMillis() - mLastScroll;
        if (duration > ANIMATED_SCROLL_GAP) {
            final int width = getWidth() - getPaddingRight() - getPaddingLeft();
            final int right = getChildAt(0).getWidth();
            final int maxX = Math.max(0, right - width);
            final int scrollX = getScrollX();
            dx = Math.max(0, Math.min(scrollX + dx, maxX)) - scrollX;

            mScroller.startScroll(scrollX, getScrollY(), dx, 0);
            postInvalidateOnAnimation();
        } else {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            scrollBy(dx, dy);
        }
        mLastScroll = AnimationUtils.currentAnimationTimeMillis();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private boolean inChild(int x, int y) {
        if (getChildCount() > 0) {
            final int scrollX = getScrollX();
            final View child = getChildAt(0);
            return !(y < child.getTop()
                    || y >= child.getBottom()
                    || x < child.getLeft() - scrollX
                    || x >= child.getRight() - scrollX);
        }
        return false;
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0, child.getWidth() - (getWidth() - getPaddingLeft() - getPaddingRight()));
        }
        return scrollRange;
    }

    private int getMaxOverScrollRange() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private View findFocusableViewInMyBounds(final boolean leftFocus,
                                             final int left, View preferredFocusable) {
        /*
         * The fading edge's transparent side should be considered for focus
         * since it's mostly visible, so we divide the actual fading edge length
         * by 2.
         */
        final int fadingEdgeLength = getHorizontalFadingEdgeLength() / 2;
        final int leftWithoutFadingEdge = left + fadingEdgeLength;
        final int rightWithoutFadingEdge = left + getWidth() - fadingEdgeLength;

        if ((preferredFocusable != null)
                && (preferredFocusable.getLeft() < rightWithoutFadingEdge)
                && (preferredFocusable.getRight() > leftWithoutFadingEdge)) {
            return preferredFocusable;
        }

        return findFocusableViewInBounds(leftFocus, leftWithoutFadingEdge,
                rightWithoutFadingEdge);
    }

    private View findFocusableViewInBounds(boolean leftFocus, int left, int right) {

        List<View> focusables = getFocusables(View.FOCUS_FORWARD);
        View focusCandidate = null;

        /*
         * A fully contained focusable is one where its left is below the bound's
         * left, and its right is above the bound's right. A partially
         * contained focusable is one where some part of it is within the
         * bounds, but it also has some part that is not within bounds.  A fully contained
         * focusable is preferred to a partially contained focusable.
         */
        boolean foundFullyContainedFocusable = false;

        int count = focusables.size();
        for (int i = 0; i < count; i++) {
            View view = focusables.get(i);
            int viewLeft = view.getLeft();
            int viewRight = view.getRight();

            if (left < viewRight && viewLeft < right) {
                /*
                 * the focusable is in the target area, it is a candidate for
                 * focusing
                 */

                final boolean viewIsFullyContained = (left < viewLeft) &&
                        (viewRight < right);

                if (focusCandidate == null) {
                    /* No candidate, take this one */
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    final boolean viewIsCloserToBoundary =
                            (leftFocus && viewLeft < focusCandidate.getLeft()) ||
                                    (!leftFocus && viewRight > focusCandidate.getRight());

                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToBoundary) {
                            /*
                             * We're dealing with only fully contained views, so
                             * it has to be closer to the boundary to beat our
                             * candidate
                             */
                            focusCandidate = view;
                        }
                    } else {
                        if (viewIsFullyContained) {
                            /* Any fully contained view beats a partially contained view */
                            focusCandidate = view;
                            foundFullyContainedFocusable = true;
                        } else if (viewIsCloserToBoundary) {
                            /*
                             * Partially contained view beats another partially
                             * contained view if it's closer
                             */
                            focusCandidate = view;
                        }
                    }
                }
            }
        }

        return focusCandidate;
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private static int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            return 0;
        }
        if ((my + n) > child) {
            return child - my;
        }
        return n;
    }
}
