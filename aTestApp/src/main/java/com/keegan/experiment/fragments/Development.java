package com.keegan.experiment.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.keegan.experiment.R;
import com.keegan.experiment.activities.MainActivity;
import com.keegan.experiment.customs.CustomExpandableListAdapter;
import com.keegan.experiment.utilities.DevelopmentLogParser;
import com.keegan.experiment.utilities.ExpandableListDataPump;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Keegan on 28/01/16.
 */
public class Development extends Fragment implements View.OnClickListener {

    private final String TAG = Development.class.getSimpleName();
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_development, container, false);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        DevelopmentLogParser.getMultiCurrencyList();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expandableListView.setNestedScrollingEnabled(true);
        }else {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getActivity().findViewById(R.id.appBarLayout).getLayoutParams();
            params.bottomMargin = 256;
            getActivity().findViewById(R.id.appBarLayout).setLayoutParams(params);
            Snackbar.make(MainActivity.rootLayout, "Scrolling only works for Lollipop or above", Snackbar.LENGTH_INDEFINITE).show();
        }

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity(),                        expandableListTitle.get(groupPosition) + " List Expanded.",                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });

        return rootView;
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
    }

    @Override
    public void onClick(View v) {
    }
}
