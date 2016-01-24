package com.keegan.experiment.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.keegan.experiment.GlobalVariables;
import com.keegan.experiment.R;
import com.keegan.experiment.fragments.UnderConstructionFragment;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = LoginActivity.class.getSimpleName();

    EditText username_EditText;
    EditText pin_EditText;

    NavigationView navigation;
    DrawerLayout mDrawerLayout;

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

    LinearLayout login_new_user;
    LinearLayout login_forgot_pin;
    LinearLayout login_help;
    LinearLayout login_contact;

    TextView login_new_user_text;
    ImageView login_new_user_image;
    TextView login_forgot_pin_text;
    ImageView login_forgot_pin_image;
    TextView login_help_text;
    ImageView login_help_image;
    TextView login_contact_text;
    ImageView login_contact_image;


    Activity mActivity;
    public static Context mContext;
    String finalUsername;
    String finalPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_activity);
        initViewObjects();
    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String savedUsername = sharedPreferences.getString("Username", "");
        username_EditText.setText(savedUsername);
        pin_EditText.setText("");
        if (!savedUsername.equalsIgnoreCase("")) {
            pin_EditText.requestFocus();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }else{
            username_EditText.requestFocus();
            showKeyboard(mActivity, username_EditText);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
                    Log.d(TAG, "pin_EditText got focus");
                    toggleKeyboard(true);
                } else {
                    Log.d(TAG, "pin_EditText lost focus");
                    toggleKeyboard(false);
                }
            }
        });
        //pin_EditText.setOnTouchListener(otl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            pin_EditText.setShowSoftInputOnFocus(false);
        }

        username_EditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.d(TAG, "username_EditText got focus");
                    toggleKeyboard(false);
                }
            }
        });

        //nav draw
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            View lastFocused;

            @Override
            public void onDrawerSlide(View view, float v) {
                if (v * 100 > GlobalVariables.hide_keyboard_login_drawer_percentage) {
                    if (pin_EditText.hasFocus()) {
                        lastFocused = pin_EditText;
                    } else if (username_EditText.hasFocus()) {
                        hideKeyboard(mActivity);
                        lastFocused = username_EditText;
                    }
                }
            }

            @Override
            public void onDrawerOpened(View view) {
            }

            @Override
            public void onDrawerClosed(View view) {
                if (lastFocused != null) {
                    Log.d(TAG, lastFocused.toString());
                    lastFocused.requestFocus();
                }
            }

            @Override
            public void onDrawerStateChanged(int i) {
            }
        });
        navigation = (NavigationView) findViewById(R.id.navigation);

        login_new_user = (LinearLayout) findViewById(R.id.login_new_user);
        login_new_user_image = (ImageView) login_new_user.findViewById(R.id.login_navigation_icon);
        login_new_user_text = (TextView) login_new_user.findViewById(R.id.login_navigation_text);
        login_new_user_image.setBackgroundResource(R.drawable.name);
        login_new_user_text.setText("New User");

        login_forgot_pin = (LinearLayout) findViewById(R.id.login_forgot_pin);
        login_forgot_pin_image = (ImageView) login_forgot_pin.findViewById(R.id.login_navigation_icon);
        login_forgot_pin_text = (TextView) login_forgot_pin.findViewById(R.id.login_navigation_text);
        login_forgot_pin_image.setBackgroundResource(R.drawable.backspace);
        login_forgot_pin_text.setText("Forgot Pin");

        login_help = (LinearLayout) findViewById(R.id.login_help);
        login_help_image = (ImageView) login_help.findViewById(R.id.login_navigation_icon);
        login_help_text = (TextView) login_help.findViewById(R.id.login_navigation_text);
        login_help_image.setBackgroundResource(R.drawable.nav_header_bg);
        login_help_text.setText("Help");

        login_contact = (LinearLayout) findViewById(R.id.login_contact);
        login_contact_text = (TextView) login_contact.findViewById(R.id.login_navigation_text);
        login_contact_image = (ImageView) login_contact.findViewById(R.id.login_navigation_icon);
        login_contact_image.setBackgroundResource(R.drawable.header);
        login_contact_text.setText("Contact");

        login_new_user.setOnClickListener(this);
        login_forgot_pin.setOnClickListener(this);
        login_help.setOnClickListener(this);
        login_contact.setOnClickListener(this);

    }

    private View.OnClickListener numpad = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer[] data = {R.id.custom_numeric_keyboard_key_0,
                    R.id.custom_numeric_keyboard_key_1, R.id.custom_numeric_keyboard_key_2, R.id.custom_numeric_keyboard_key_3,
                    R.id.custom_numeric_keyboard_key_4, R.id.custom_numeric_keyboard_key_5, R.id.custom_numeric_keyboard_key_6,
                    R.id.custom_numeric_keyboard_key_7, R.id.custom_numeric_keyboard_key_8, R.id.custom_numeric_keyboard_key_9,
                    R.id.custom_numeric_keyboard_key_menu, R.id.custom_numeric_keyboard_key_backspace
            };

            Integer pinClicked = Arrays.asList(data).indexOf(v.getId());
            if (pinClicked >= 0 && pinClicked <= 9) {
                pin_EditText.append(pinClicked.toString());
            } else if (pinClicked == 10) {
                openDrawer();
            } else if (pinClicked == 11 && pin_EditText.getText().length() > 0) {
                int cursorPosition = pin_EditText.getSelectionStart();
                //Log.d(TAG, "Pin Cursor: " + cursorPosition);
                if (cursorPosition > 0) {
                    pin_EditText.getText().delete(cursorPosition - 1, cursorPosition);
                }
            } else {
                Log.d(TAG, "Somehow pressed a non-numpad key");
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

        if (finalPin.equalsIgnoreCase("9090")) {
            //if correct credentials
            savePreferences("Username", finalUsername);
            //start main activity with extra info
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Bundle b = new Bundle();
            b.putString("Username", finalUsername); //Your id
            b.putString("Pin", finalPin); //Your id
            intent.putExtras(b); //Put your id to your next Intent
            startActivity(intent);
            //finish();
            //startActivity(new Intent(CustomerLoginActivity.this, ApplicationActivity.class).putExtra(INTENT.EXTRA_ACCOUNT_BALANCE.toString(), accountBalance));
        }
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
        mActivity = this;
        mContext = getApplicationContext();
        loadSavedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void toggleKeyboard(boolean numpad) {
        int visibility = numericKeypad.getVisibility();
        if (numpad && visibility == View.GONE) {
            hideKeyboard(mActivity);
            numericKeypad.setVisibility(View.VISIBLE);
        } else if (!numpad) {
            showKeyboard(mActivity, username_EditText);
            if (visibility == View.VISIBLE) {
                numericKeypad.setVisibility(View.GONE);
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity activity, EditText editText) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_new_user:
                closeDrawer();
                break;
            case R.id.login_contact:
                closeDrawer();
                break;
            case R.id.login_forgot_pin:
                closeDrawer();
                break;
            case R.id.login_help:
                closeDrawer();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void startFragment(Fragment mFragment) {
        if (mFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.FragmentContainer, mFragment);
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.addToBackStack(null);
            ft.commit();
            //setFunctionLayout(View.VISIBLE);
        }
    }
}


