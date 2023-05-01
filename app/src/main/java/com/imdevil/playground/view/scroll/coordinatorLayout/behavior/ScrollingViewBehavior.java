package com.imdevil.playground.view.scroll.coordinatorLayout.behavior;

import static androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD;
import static androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import java.util.List;

public class ScrollingViewBehavior extends HeaderScrollingViewBehavior {
    private static final String TAG = "ScrollingViewBehavior";

    public ScrollingViewBehavior() {
    }

    public ScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        // We depend on any AppBarLayouts
        return dependency instanceof TextView;
    }

    @Override
    public boolean onDependentViewChanged(
            @NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        Log.d(TAG, "onDependentViewChanged: ");
        offsetChildAsNeeded(child, dependency);
        return false;
    }

    @Override
    public void onDependentViewRemoved(
            @NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        Log.d(TAG, "onDependentViewRemoved: ");
        if (dependency instanceof TextView) {
            ViewCompat.removeAccessibilityAction(parent, ACTION_SCROLL_FORWARD.getId());
            ViewCompat.removeAccessibilityAction(parent, ACTION_SCROLL_BACKWARD.getId());
            ViewCompat.setAccessibilityDelegate(parent, null);
        }
    }

    private void offsetChildAsNeeded(@NonNull View child, @NonNull View dependency) {
        ViewCompat.offsetTopAndBottom(child, dependency.getBottom() - child.getTop());
    }

    @Nullable
    @Override
    TextView findFirstDependency(@NonNull List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (view instanceof TextView) {
                return (TextView) view;
            }
        }
        return null;
    }
}
