package com.keegan.experiment.fragments;

import com.cleveroad.slidingtutorial.PageFragment;
import com.cleveroad.slidingtutorial.TransformItem;
import com.keegan.experiment.R;

/**
 * Created by keegan on 26/02/16.
 */
public class FirstCustomPageFragment extends PageFragment {

    @Override
    protected int getLayoutResId() {
        // layout id of fragment
        return R.layout.fragment_page_first;
    }

    @Override
    protected TransformItem[] provideTransformItems() {
        // list of transformation items
        return new TransformItem[]{
                new TransformItem(R.id.ivFirstImage, true, 20),
                new TransformItem(R.id.ivSecondImage, false, 6),
                new TransformItem(R.id.ivThirdImage, true, 8),
                new TransformItem(R.id.ivFourthImage, false, 10),
                new TransformItem(R.id.ivFifthImage, false, 3),
                new TransformItem(R.id.ivSixthImage, false, 9),
                new TransformItem(R.id.ivSeventhImage, false, 14),
                new TransformItem(R.id.ivEighthImage, false, 7)
        };
    }
}