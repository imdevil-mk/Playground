package com.imdevil.playground.seekbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ProgressBar;

public class VerticalSeekBar extends ProgressBar {
    /**
     * A callback that notifies clients when the progress level has been
     * changed. This includes changes that were initiated by the user through a
     * touch gesture or arrow key/trackball as well as changes that were initiated
     * programmatically.
     */
    public interface OnSeekBarChangeListener {

        /**
         * Notification that the progress level has changed. Clients can use the fromUser parameter
         * to distinguish user-initiated changes from those that occurred programmatically.
         *
         * @param seekBar  The SeekBar whose progress has changed
         * @param progress The current progress level. This will be in the range min..max where min
         *                 and max were set by {@link ProgressBar#setMin(int)} and
         *                 {@link ProgressBar#setMax(int)}, respectively. (The default values for
         *                 min is 0 and max is 100.)
         * @param fromUser True if the progress change was initiated by the user.
         */
        void onProgressChanged(VerticalSeekBar seekBar, int progress, boolean fromUser);

        /**
         * Notification that the user has started a touch gesture. Clients may want to use this
         * to disable advancing the seekbar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        void onStartTrackingTouch(VerticalSeekBar seekBar);

        /**
         * Notification that the user has finished a touch gesture. Clients may want to use this
         * to re-enable advancing the seekbar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        void onStopTrackingTouch(VerticalSeekBar seekBar);
    }

    private int mScaledTouchSlop;
    private float mTouchDownY;
    private boolean mIsDragging;
    float mTouchProgressOffset;

    private OnSeekBarChangeListener mOnSeekBarChangeListener;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getCurrentDrawable();

        int dw = 0;
        int dh = 0;
        if (d != null) {
            dw = Math.max(getMinWidth(), Math.min(getMaxWidth(), d.getIntrinsicWidth()));
            dh = Math.max(getMinHeight(), Math.min(getMaxHeight(), d.getIntrinsicHeight()));
        }
        dw += getPaddingLeft() + getPaddingRight();
        dh += getPaddingTop() + getPaddingBottom();

        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        setMeasuredDimension(resolveSizeAndState(dw, widthMeasureSpec, 0),
                resolveSizeAndState(dh, heightMeasureSpec, 0));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInScrollingContainer()) {
                    mTouchDownY = event.getY();
                } else {
                    startDrag(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsDragging) {
                    trackTouchEvent(event);
                } else {
                    final float y = event.getY();
                    if (Math.abs(y - mTouchDownY) > mScaledTouchSlop) {
                        startDrag(event);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    // Touch up when we never crossed the touch slop threshold should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                if (mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate(); // see above explanation
                break;
        }
        return true;
    }

    private void startDrag(MotionEvent event) {
        setPressed(true);

        onStartTrackingTouch();
        trackTouchEvent(event);
        attemptClaimDrag();
    }

    private void setHotspot(float x, float y) {
        final Drawable bg = getBackground();
        if (bg != null) {
            bg.setHotspot(x, y);
        }
    }

    private void trackTouchEvent(MotionEvent event) {
        final int x = Math.round(event.getX());
        final int y = Math.round(event.getY());
        final int height = getHeight();
        final int availableHeight = height - getPaddingTop() - getPaddingBottom();

        final float scale;
        float progress = 0.0f;

        if (y > height - getPaddingBottom()) {
            scale = 0.0f;
        } else if (y < getPaddingTop()) {
            scale = 1.0f;
        } else {
            scale = (height - y - getPaddingBottom()) / (float) availableHeight;
            progress = mTouchProgressOffset;
        }

        final int range = getMax() - getMin();
        progress += scale * range + getMin();

        setHotspot(x, y);
        //setProgressInternal(Math.round(progress), true, false);
        setProgress(Math.round(progress), false);
        onProgressChanged(this, Math.round(progress), true);
    }

    /**
     * Tries to claim the user's drag motion, and requests disallowing any
     * ancestors from stealing events in the drag.
     */
    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        mOnSeekBarChangeListener = l;
    }

    void onProgressChanged(VerticalSeekBar seekBar, int progress, boolean fromUser) {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onProgressChanged(this, progress, fromUser);
        }
    }

    /**
     * This is called when the user has started touching this widget.
     */
    void onStartTrackingTouch() {
        mIsDragging = true;

        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    /**
     * This is called when the user either releases their touch or the touch is
     * canceled.
     */
    void onStopTrackingTouch() {
        mIsDragging = false;

        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return VerticalSeekBar.class.getName();
    }

    public boolean isInScrollingContainer() {
        ViewParent p = getParent();
        while (p != null && p instanceof ViewGroup) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) {
                return true;
            }
            p = p.getParent();
        }
        return false;
    }
}
