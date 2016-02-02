package com.keegan.experiment.customs;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by keegan on 29/01/16.
 */
public class CustomCoordinatorLayout extends CoordinatorLayout {

    private boolean allowForScroll = false;

    public CustomCoordinatorLayout(Context context) {
        super(context);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return allowForScroll && super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    public boolean isAllowForScroll() {
        return allowForScroll;
    }

    public void setAllowForScroll(boolean allowForScrool) {
        this.allowForScroll = allowForScrool;
    }
}