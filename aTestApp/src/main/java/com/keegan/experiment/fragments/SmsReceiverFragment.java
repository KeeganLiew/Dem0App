package com.keegan.experiment.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.keegan.experiment.INTENT;
import com.keegan.experiment.R;
import com.keegan.experiment.services.SMSReceiver;
import com.keegan.experiment.utilities.BankTypeOnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keegan on 22/01/16.
 */
public class SmsReceiverFragment extends Fragment implements View.OnClickListener {

    private final String TAG = SmsReceiverFragment.class.getSimpleName();
    TextView selectBankTypeTV;
    TextView resultTV;
    EditText amountET;
    EditText bankAccountET;
    Button payButton;
    Button cancelButton;
    ProgressBar progressBar;
    BroadcastReceiver broadcastReceiver;
    Spinner bankTypeSpinner;
    Switch mySwitch;

    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sms, container, false);

        mContext = getActivity();

        amountET = (EditText) rootView.findViewById(R.id.Fragment_Banks_EditText_Amount);
        bankAccountET = (EditText) rootView.findViewById(R.id.Fragment_Banks_EditText_BankAccount);
        selectBankTypeTV = (TextView) rootView.findViewById(R.id.Fragment_Banks_Type_TextView_AccountId);
        resultTV = (TextView) rootView.findViewById(R.id.Fragment_Banks_TextView_Result);
        payButton = (Button) rootView.findViewById(R.id.Fragment_Banks_Button_Pay);
        cancelButton = (Button) rootView.findViewById(R.id.Fragment_Banks_Button_Cancel);
        progressBar = (ProgressBar) rootView.findViewById(R.id.Fragment_Banks_Progressbar);
        bankTypeSpinner = (Spinner) rootView.findViewById(R.id.Fragment_Bank_Type_Spinner);

        List<String> spinnerList = new ArrayList<String>();
        spinnerList = new ArrayList<String>();
        spinnerList.add(getString(R.string.DOMESTIC));
        spinnerList.add(getString(R.string.INTERNATIONAL));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.Spinner_TextView, spinnerList);
        bankTypeSpinner.setAdapter(spinnerAdapter);
        bankTypeSpinner.setSelection(0);
        bankTypeSpinner.setOnItemSelectedListener(new BankTypeOnItemSelectedListener(getActivity()));
        selectBankTypeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bankTypeListArrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.up));
                bankTypeSpinner.performClick();
            }
        });

        payButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        //Switch
        mySwitch = (Switch) rootView.findViewById(R.id.smsToggleSwitch);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //sms stuff
                ComponentName smsReceiverComponent = new ComponentName(mContext, SMSReceiver.class);
                if (isChecked) {
                    //Log.d(TAG, "Switch is currently ON"); //Enable
                    mContext.getPackageManager().setComponentEnabledSetting(smsReceiverComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                    componentChecker(smsReceiverComponent);
                } else {
                    //Log.d(TAG, "Switch is currently OFF"); //Disable
                    mContext.getPackageManager().setComponentEnabledSetting(smsReceiverComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    componentChecker(smsReceiverComponent);
                }

            }
        });
        mySwitch.setChecked(true); //set the switch to ON

        return rootView;
    }

    public void componentChecker(ComponentName componentToCheck) {
        int status = mContext.getPackageManager().getComponentEnabledSetting(componentToCheck);
        if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            Log.d(TAG, componentToCheck.getShortClassName() + " is enabled");
        } else if (status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            Log.d(TAG, componentToCheck.getShortClassName() + " is disabled");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase(INTENT.BANK.toString())) {
                } else if (intent.getAction().equalsIgnoreCase("EXCHANGE_QUOTATION")) {

                }
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(INTENT.BANK.toString()));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(INTENT.EXCHANGE_QUOTATION.toString()));
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        broadcastReceiver = null;
    }

    @Override
    public void onDetach() {
        if (mMenu != null) {
            mMenu.clear();
        }
        super.onDetach();
    }

    Menu mMenu;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenu = menu;
        //inflater.inflate(R.menu.menu_add_payee, mMenu);
        super.onCreateOptionsMenu(mMenu, inflater);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Fragment_Banks_Button_Pay:
                break;
            case R.id.Fragment_Banks_Button_Cancel:
                Intent intent = new Intent(INTENT.FRAGMENT_ITEM_CANCELLED.toString());
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                Log.d(TAG, "SENDING INTENT: " + intent.getAction());
                break;
        }
    }

    private void showProgressBar() {
        amountET.setEnabled(false);
        bankAccountET.setEnabled(false);
        payButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        resultTV.setVisibility(View.GONE);
    }

    private void hideProgressBar(String response) {
        amountET.setEnabled(true);
        bankAccountET.setEnabled(true);
        payButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        resultTV.setText(response);
        resultTV.setVisibility(View.VISIBLE);
    }

}
