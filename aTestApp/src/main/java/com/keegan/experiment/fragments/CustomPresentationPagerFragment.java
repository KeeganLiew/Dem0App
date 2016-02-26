package com.keegan.experiment.fragments;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;

import com.cleveroad.slidingtutorial.PageFragment;
import com.cleveroad.slidingtutorial.PresentationPagerFragment;
import com.keegan.experiment.R;

/**
 * Created by keegan on 26/02/16.
 */
public class CustomPresentationPagerFragment extends PresentationPagerFragment {

    @Override
    public int getLayoutResId() {
        // layout id of fragment
        return R.layout.fragment_presentation;
    }

    @Override
    public int getViewPagerResId() {
        // id of view pager
        return R.id.viewPager;
    }

    @Override
    public int getIndicatorResId() {
        // id of circular indicator
        return R.id.indicator;
    }

    @Override
    public int getButtonSkipResId() {
        // id of skip button
        return R.id.tvSkip;
    }

    @Override
    protected int getPagesCount() {
        // total number of pages
        return 3;
    }

    @Override
    protected PageFragment getPage(int position) {
        // get page for position
        if (position == 0)
            return new FirstCustomPageFragment();
        if (position == 1)
            return new FirstCustomPageFragment();
        if (position == 2)
            return new FirstCustomPageFragment();
        throw new IllegalArgumentException("Unknown position: " + position);
    }

    @ColorInt
    @Override
    protected int getPageColor(int position) {
        // get color of page
        if (position == 0)
            return ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);
        if (position == 1)
            return ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
        if (position == 2)
            return ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
        return Color.TRANSPARENT;
    }

    @Override
    protected boolean isInfiniteScrollEnabled() {
        // enable/disable infinite scroll behavior
        return true;
    }

    /*@Override
    protected boolean onSkipButtonClicked() {
        // your own behavior goes here
        // ...
        // return true to consume click event, false otherwise
        return true;
    }*/
}
