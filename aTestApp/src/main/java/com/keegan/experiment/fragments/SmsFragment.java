package com.keegan.experiment.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.keegan.experiment.Global;
import com.keegan.experiment.Intents;
import com.keegan.experiment.R;
import com.keegan.experiment.services.SmsReceiver;
import com.keegan.experiment.utilities.SmsSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keegan on 22/01/16.
 */
public class SmsFragment extends Fragment implements OnClickListener {

    private final String TAG = SmsFragment.class.getSimpleName();

    //message type
    private Spinner messageTypeSpinner;
    private TextView messageTypeTV;
    //phone number/contact
    private EditText phoneNumberET;
    private ImageView contactsIV;
    //message
    private TextInputLayout messageTIL;
    private EditText messageET;
    //send/cancel buttons
    private Button sendButton;
    private Button cancelButton;
    //switch
    private Switch smsReceiverSwitch;

    private TextView resultTV; //not used
    private ProgressBar progressBar; //not used

    private Activity mActivity;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sms, container, false);
        mActivity = getActivity();
        initializeViewObjects(rootView);
        return rootView;
    }

    private void initializeViewObjects(View rootView) {
        //message type
        messageTypeSpinner = (Spinner) rootView.findViewById(R.id.Fragment_Sms_Spinner_MessageType);
        List<String> spinnerList = new ArrayList<>();
        spinnerList.add(getString(R.string.message_type_sms));
        spinnerList.add(getString(R.string.message_type_mms));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mActivity, R.layout.spinner_layout, R.id.Spinner_TextView, spinnerList);
        messageTypeSpinner.setAdapter(spinnerAdapter);
        messageTypeSpinner.setSelection(0);
        messageTypeSpinner.setOnItemSelectedListener(new messageTypeOnItemSelectedListener()); //set listener
        messageTypeTV = (TextView) rootView.findViewById(R.id.Fragment_Sms_TextView_MessageType);
        messageTypeTV.setOnClickListener(this); //set listener
        //phone number/contact
        phoneNumberET = (EditText) rootView.findViewById(R.id.Fragment_Sms_EditText_PhoneNumber);
        contactsIV = (ImageView) rootView.findViewById(R.id.Fragment_Sms_ImageView_Contacts);
        contactsIV.setOnClickListener(this); //set listener
        //message
        messageTIL = (TextInputLayout) rootView.findViewById(R.id.Fragment_Sms_TextInputLayout_Message);
        messageET = (EditText) rootView.findViewById(R.id.Fragment_Sms_EditText_Message);
        messageET.addTextChangedListener(new messageEditTextTextChangedListener()); //set listener
        //send/cancel buttons
        sendButton = (Button) rootView.findViewById(R.id.Fragment_Sms_Button_Send);
        cancelButton = (Button) rootView.findViewById(R.id.Fragment_Sms_Button_Cancel);
        sendButton.setOnClickListener(this); //set listener
        cancelButton.setOnClickListener(this); //set listener
        // Switch
        smsReceiverSwitch = (Switch) rootView.findViewById(R.id.Fragment_Sms_Switch_receiverToggle);
        smsReceiverSwitch.setOnCheckedChangeListener(new smsReceiverSwitchListener()); //set listener
        Boolean switchS = Global.loadSavedPreferences(mActivity, Global.sharedPref_SmsReceiverToggle, Global.SMS_SWITCH_DEFAULT);
        smsReceiverSwitch.setChecked(switchS);
        //Global.setComponent(mActivity, SmsReceiver.class, switchS);
        //not used
        progressBar = (ProgressBar) rootView.findViewById(R.id.Fragment_Sms_Progressbar);
        resultTV = (TextView) rootView.findViewById(R.id.Fragment_Sms_TextView_Result);
    }

    @Override
    public void onResume() {
        super.onResume();
        //broadcast listener
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(TAG, "Received intent: " + action);
                if (action.equalsIgnoreCase(Intents.PICKED_CONTACT_INFO.toString())) { //received picked contact
                    String name = intent.getStringExtra(Intents.PICKED_CONTACT_INFO_EXTRA_NAME.toString());
                    String phoneNumber = intent.getStringExtra(Intents.PICKED_CONTACT_INFO_EXTRA_PHONE_NUMBER.toString());
                    Log.d(TAG, "Received name & phoneNumber: " + name + " & " + phoneNumber);
                    phoneNumberET.setText(phoneNumber);
                    messageTIL.setHint(mActivity.getResources().getString(R.string.message_to) + name);
                }
            }
        };
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(broadcastReceiver, new IntentFilter(Intents.PICKED_CONTACT_INFO.toString()));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Fragment_Sms_Button_Send:
                //mandatory fields check
                if (phoneNumberET.getText().toString().isEmpty()) {
                    Global.createAndShowToast(mActivity, mActivity.getString(R.string.phone_number_is_mandatory), Toast.LENGTH_LONG);
                    return;
                }
                if (messageET.getText().toString().isEmpty()) {
                    messageTILShowError(true, getString(R.string.message_is_mandatory));
                    return;
                }
                //send sms
                SmsSender.sendSms(phoneNumberET.getText().toString(), messageET.getText().toString());
                break;
            case R.id.Fragment_Sms_Button_Cancel:
                Intent intent = new Intent(Intents.FRAGMENT_ITEM_CANCELLED.toString());
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                Log.d(TAG, "Sending Intent: " + intent.getAction());
                break;
            case R.id.Fragment_Sms_ImageView_Contacts:
                //Pick contact from Contacts
                Intent contactPickerIntent = new Intent(Intents.PICK_CONTACT.toString());
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(contactPickerIntent);
                Log.d(TAG, "Sending Intent: " + contactPickerIntent.getAction());
                break;
            case R.id.Fragment_Sms_TextView_MessageType:
                messageTypeSpinner.performClick();
                break;
        }
    }

    //listeners
    private class smsReceiverSwitchListener implements OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Global.setComponent(mActivity, SmsReceiver.class, isChecked);
            Global.savePreferences(mActivity, Global.sharedPref_SmsReceiverToggle, isChecked);
            Global.checkComponent(mActivity, TAG, SmsReceiver.class);
        }
    }

    private class messageEditTextTextChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG, "Text length: " + messageET.length());
            if (messageET.length() >= Global.sms_text_limit) {
                messageTILShowError(true, getString(R.string.message_limit_error));
            } else {
                messageTILShowError(false, Global.EMPTY_STRING);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private class messageTypeOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Log.d("OnItemSelected(Type)", "Selected " + pos);
            messageTypeTV.setText(parent.getSelectedItem().toString());
            messageTypeTV.setTextColor(ContextCompat.getColor(mActivity, R.color.app_main_text_color));

            if (pos == 0) {
                messageTIL.setHint(mActivity.getString(R.string.hint_message_sms));
                messageTIL.setEnabled(true);
                messageET.setEnabled(true);
            } else if (pos == 1) {
                messageTIL.setHint(mActivity.getString(R.string.hint_message_mms));
                messageTIL.setEnabled(false);
                messageET.setEnabled(false);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    //private methods
    private void messageTILShowError(boolean bool, String errorMessage) {
        messageTIL.setErrorEnabled(bool);
        messageTIL.setError(errorMessage);
    }

    private void showProgressBar() {
        sendButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        resultTV.setVisibility(View.GONE);
    }

    private void hideProgressBar(String response) {
        sendButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        resultTV.setText(response);
        resultTV.setVisibility(View.VISIBLE);
    }

}
