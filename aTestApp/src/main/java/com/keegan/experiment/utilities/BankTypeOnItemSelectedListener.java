package com.keegan.experiment.utilities;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.keegan.experiment.R;

import java.text.DecimalFormat;

/**
 * Created by Keegan on 22/01/16.
 */
public class BankTypeOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    Activity mActivity;

    public BankTypeOnItemSelectedListener(Activity activity) {
        mActivity = activity;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.d("OnItemSelected(Type)", "Selected " + pos);
        final RelativeLayout extraFieldRL = (RelativeLayout) mActivity.findViewById(R.id.Fragment_Banks_RelativeLayout_Extra_Field);
        final TextView selectBankType = (TextView) mActivity.findViewById(R.id.Fragment_Banks_Type_TextView_AccountId);

        selectBankType.setText(parent.getSelectedItem().toString());
        selectBankType.setTextColor(mActivity.getResources().getColor(R.color.white));

        if (pos == 0) {
            extraFieldRL.setVisibility(View.GONE);
        } else if (pos == 1) {
            extraFieldRL.setVisibility(View.VISIBLE);
        }
        dismissSpinner();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        dismissSpinner();
    }

    private void dismissSpinner() {
        /*Spinner spinner = (Spinner) mActivity.findViewById(R.id.Fragment_Bank_Type_Spinner);
        TextView lineSep = (TextView) mActivity.findViewById(R.id.Fragment_Bank_Type_TextView_Sep);
        spinner.setVisibility(View.GONE);
        lineSep.setVisibility(View.GONE);*/
        ImageView arrow = (ImageView) mActivity.findViewById(R.id.Fragment_Banks_Type_ImageView_ListArrow);
        //arrow.setBackground(mActivity.getResources().getDrawable(R.drawable.down));
        arrow.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.down));
    }


}
