package com.keegan.experiment.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.keegan.experiment.R;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = LoginActivity.class.getSimpleName();

    EditText username_EditText;
    EditText pin_EditText;

    TableLayout numericKeypad;
    TextView num1;
    TextView num2;
    TextView num3;
    TextView num4;
    TextView num5;
    TextView num6;
    TextView num7;
    TextView num8;
    TextView num9;
    TextView numMenu;
    TextView num0;
    ImageView numBackspace;

    Activity mActivity;
    public static Context mContext;
    String finalUsername;
    String finalPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_activity);
        initViewObjects();

        loadSavedPreferences();

        mActivity = this;
        mContext = getApplicationContext();
    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String savedUsername = sharedPreferences.getString("username", "");
        if (!savedUsername.equalsIgnoreCase("")) {
            username_EditText.setText(savedUsername);
            pin_EditText.requestFocus();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    private void initViewObjects() {
        numericKeypad = (TableLayout) findViewById(R.id.keypad);

        //EditText
        username_EditText = (EditText) findViewById(R.id.Fragment_Payment_BumpToPay_EditText_Name);
        pin_EditText = (EditText) findViewById(R.id.Fragment_Payment_BumpToPay_EditText_Pin);

        num0 = (TextView) findViewById(R.id.custom_numeric_keyboard_key_0);
        num1 = (TextView) findViewById(R.id.custom_numeric_keyboard_key_1);
        num2 = (TextView) findViewById(R.id.custom_numeric_keyboard_key_2);
        num3 = (TextView) findViewById(R.id.custom_numeric_keyboard_key_3);
        num4 = (TextView) findViewById(R.id.custom_numeric_keyboard_key_4);
        num5 = (TextView) findViewById(R.id.custom_numeric_keyboard_key_5);
        num6 = (TextView) findViewById(R.id.custom_numeric_keyboard_key_6);
        num7 = (TextView) findViewById(R.id.custom_numeric_keyboard_key_7);
        num8 = (TextView) findViewById(R.id.custom_numeric_keyboard_key_8);
        num9 = (TextView) findViewById(R.id.custom_numeric_keyboard_key_9);
        numMenu = (TextView) findViewById(R.id.custom_numeric_keyboard_key_menu);
        numBackspace = (ImageView) findViewById(R.id.custom_numeric_keyboard_key_backspace);

        num0.setOnClickListener(numpad);
        num1.setOnClickListener(numpad);
        num2.setOnClickListener(numpad);
        num3.setOnClickListener(numpad);
        num4.setOnClickListener(numpad);
        num5.setOnClickListener(numpad);
        num6.setOnClickListener(numpad);
        num7.setOnClickListener(numpad);
        num8.setOnClickListener(numpad);
        num9.setOnClickListener(numpad);
        numMenu.setOnClickListener(numpad);
        numBackspace.setOnClickListener(numpad);

        pin_EditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                    toggleKeyboard();
                } else {
                    Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                    toggleKeyboard();
                }
            }
        });
        //pin_EditText.setOnTouchListener(otl);
        pin_EditText.setShowSoftInputOnFocus(false);
    }

    private View.OnClickListener numpad = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //String pinClicked = "";;

            Integer[] data = {R.id.custom_numeric_keyboard_key_0,
                    R.id.custom_numeric_keyboard_key_1, R.id.custom_numeric_keyboard_key_2, R.id.custom_numeric_keyboard_key_3,
                    R.id.custom_numeric_keyboard_key_4, R.id.custom_numeric_keyboard_key_5, R.id.custom_numeric_keyboard_key_6,
                    R.id.custom_numeric_keyboard_key_7, R.id.custom_numeric_keyboard_key_8, R.id.custom_numeric_keyboard_key_9,
                    R.id.custom_numeric_keyboard_key_menu, R.id.custom_numeric_keyboard_key_backspace
            };

            Log.d(TAG, "v.getId(): " + v.getId());
            Integer pinClicked = Arrays.asList(data).indexOf(v.getId());
            Log.d(TAG, "pinClicked: " + pinClicked);

            if (pinClicked >= 0 && pinClicked <= 9) {
                pin_EditText.append(pinClicked.toString());
            } else if (pinClicked == 10) {

            } else if (pinClicked == 11 && pin_EditText.getText().length() > 0) {
                Integer length = pin_EditText.getText().length();
                pin_EditText.getText().delete(length - 1, length);
            } else {

            }
            Log.d(TAG, pin_EditText.getText().toString());
            pinLoginChecker(pin_EditText);
        }
    };

    private void pinLoginChecker(EditText pin_EditText) {
        String pin = pin_EditText.getText().toString();
        if (pin.length() == 4) {
            finalUsername = username_EditText.getText().toString();
            finalPin = pin;
            //show pop up dialog

            //start logging in
            startLoginProcess();
        }
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void startLoginProcess() {

        //if correct credentials
        savePreferences("username", finalUsername);
        //start main activity with extra info
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Bundle b = new Bundle();
        b.putString("Username", finalUsername); //Your id
        b.putString("Pin", finalPin); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
        finish();
    }

    private View.OnTouchListener otl = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            return true; // the listener has consumed the event
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void toggleKeyboard() {
        if (numericKeypad.getVisibility() == View.VISIBLE) {
            numericKeypad.setVisibility(View.GONE);
        } else {
            //hideKeyboard(mActivity);
            hideKeyBoard();
            numericKeypad.setVisibility(View.VISIBLE);

        }
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                break;
            case R.id.nav_display_picture:
                break;
        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}


