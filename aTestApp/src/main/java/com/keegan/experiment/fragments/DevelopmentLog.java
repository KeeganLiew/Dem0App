package com.keegan.experiment.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.google.inject.Inject;
import com.keegan.experiment.R;
import com.keegan.experiment.activities.MainActivity;
import com.keegan.experiment.customs.CustomExpandableListAdapter;
import com.keegan.experiment.utilities.DevelopmentLogParser;
import com.keegan.experiment.utilities.ExpandableListDataPump;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by Keegan on 28/01/16.
 */
public class DevelopmentLog extends RoboFragment implements OnClickListener {

    private final String TAG = DevelopmentLog.class.getSimpleName();

    @Inject
    Activity mActivity;

    @InjectView(R.id.expandableListView)
    private ExpandableListView expandableListView;
    //findViewById injects
    //private ExpandableListView expandableListView;

    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private Snackbar preLollipopErrorSnackBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_development, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        initializeViewObjects();
    }

    private void initializeViewObjects() {
        //expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        DevelopmentLogParser.getDevelopmentLog();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(mActivity, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expandableListView.setNestedScrollingEnabled(true);
        } else {
            LayoutParams params = (LayoutParams) mActivity.findViewById(R.id.Activity_Main_AppBarLayout).getLayoutParams();
            params.bottomMargin = 256;
            mActivity.findViewById(R.id.Activity_Main_AppBarLayout).setLayoutParams(params);
            preLollipopErrorSnackBar = MainActivity.createSnackBar(mActivity.getString(R.string.scroll_error), Snackbar.LENGTH_INDEFINITE);
        }

        expandableListView.setOnGroupExpandListener(new expandableLvGroupExpandListener());
        expandableListView.setOnGroupCollapseListener(new expandableLvGroupCollapseListener());
        expandableListView.setOnChildClickListener(new expandableLvChildListener());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (preLollipopErrorSnackBar != null) {
            preLollipopErrorSnackBar.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            ////TODO: 11/02/16 when click on item, go to page of feature
        }
    }

    //Listeners ////TODO: 16/12/16 redo listener toasts
    private class expandableLvGroupExpandListener implements OnGroupExpandListener {
        @Override
        public void onGroupExpand(int groupPosition) {
            Toast.makeText(mActivity, expandableListTitle.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
        }
    }

    private class expandableLvGroupCollapseListener implements OnGroupCollapseListener {
        @Override
        public void onGroupCollapse(int groupPosition) {
            Toast.makeText(mActivity,
                    expandableListTitle.get(groupPosition) + " List Collapsed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class expandableLvChildListener implements OnChildClickListener {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
            Toast.makeText(
                    mActivity,
                    expandableListTitle.get(groupPosition)
                            + " -> "
                            + expandableListDetail.get(
                            expandableListTitle.get(groupPosition)).get(
                            childPosition), Toast.LENGTH_SHORT
            ).show();
            return false;
        }
    }

}
