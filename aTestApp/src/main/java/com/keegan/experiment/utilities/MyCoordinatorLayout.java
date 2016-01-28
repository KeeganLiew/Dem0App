package com.keegan.experiment.utilities;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by keegan on 29/01/16.
 */
public class MyCoordinatorLayout extends CoordinatorLayout {

    private boolean allowForScrool = false;

    public MyCoordinatorLayout(Context context) {
        super(context);
    }

    public MyCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return allowForScrool && super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    public boolean isAllowForScroll() {
        return allowForScrool;
    }

    public void setAllowForScroll(boolean allowForScrool) {
        this.allowForScrool = allowForScrool;
    }
}