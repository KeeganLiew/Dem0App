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
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.keegan.experiment.Global;
import com.keegan.experiment.R;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private final String TAG = LoginActivity.class.getSimpleName();

    private NavigationView navigationNV;
    private DrawerLayout mDrawerLayout;

    private LinearLayout newUserLL;
    private LinearLayout forgotPinLL;
    private LinearLayout helpLL;
    private LinearLayout contactLL;

    private EditText usernameET;
    private EditText pinET;

    private TableLayout numericKeypad;
    private TextView num1;
    private TextView num2;
    private TextView num3;
    private TextView num4;
    private TextView num5;
    private TextView num6;
    private TextView num7;
    private TextView num8;
    private TextView num9;
    private TextView numMenu;
    private TextView num0;
    private ImageView numBackspace;

    private Activity mActivity;
    private Context mContext;
    private String finalUsername;
    private String finalPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewObjectsInitializations();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void viewObjectsInitializations() {
        //EditText
        usernameET = (EditText) findViewById(R.id.Activity_Login_EditText_Name);
        pinET = (EditText) findViewById(R.id.Activity_Login_EditText_Pin);
        usernameET.setOnFocusChangeListener(new usernameEditTextOnFocusChange());
        pinET.setOnFocusChangeListener(new pinEditTextOnFocusChange());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            pinET.setShowSoftInputOnFocus(false);
        }

        //numpadListener
        numericKeypad = (TableLayout) findViewById(R.id.Activity_Login_NumericKeypad);
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

        TextView[] zeroToNineList = new TextView[]{num0, num1, num2, num3, num4, num5, num6, num7, num8, num9};
        for (TextView number : zeroToNineList) {
            number.setOnClickListener(numpadListener);
        }
        numMenu.setOnClickListener(numpadListener);
        numBackspace.setOnClickListener(numpadListener);

        //nav draw
        mDrawerLayout = (DrawerLayout) findViewById(R.id.Activity_Login_DrawerLayout);
        mDrawerLayout.setDrawerListener(new loginDrawerListener());
        navigationNV = (NavigationView) findViewById(R.id.Activity_Login_NavigationView_Navigation);

        newUserLL = (LinearLayout) findViewById(R.id.Activity_Login_NewUser);
        forgotPinLL = (LinearLayout) findViewById(R.id.Activity_Login_ForgotPin);
        helpLL = (LinearLayout) findViewById(R.id.Activity_Login_Help);
        contactLL = (LinearLayout) findViewById(R.id.Activity_Login_Contact);

        LinearLayout[] navMenuList = new LinearLayout[]{newUserLL, forgotPinLL, helpLL, contactLL};
        int[] navMenuImages = new int[]{R.drawable.name, R.drawable.backspace, R.drawable.nav_header_bg, R.drawable.header};
        int[] navMenuTexts = new int[]{R.string.new_user, R.string.forgot_pin, R.string.help, R.string.contact};

        for (int i = 0; i < navMenuList.length; i++) {
            ImageView tempIV = (ImageView) navMenuList[i].findViewById(R.id.login_navigation_icon);
            TextView tempTV = (TextView) navMenuList[i].findViewById(R.id.login_navigation_text);
            tempIV.setBackgroundResource(navMenuImages[i]);
            tempTV.setText(navMenuTexts[i]);
            navMenuList[i].setOnClickListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivity = this;
        mContext = getApplicationContext();
        loadSavedPreferences();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Activity_Login_NewUser:
                closeDrawer();
                break;
            case R.id.Activity_Login_ForgotPin:
                closeDrawer();
                break;
            case R.id.Activity_Login_Help:
                closeDrawer();
                break;
            case R.id.Activity_Login_Contact:
                closeDrawer();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Listeners
    private class pinEditTextOnFocusChange implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Log.d(TAG, "pinET got focus");
                changeToCustomNumpad(true);
            } else {
                Log.d(TAG, "pinET lost focus");
                changeToCustomNumpad(false);
            }
        }
    }

    private class usernameEditTextOnFocusChange implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Log.d(TAG, "usernameET got focus");
                changeToCustomNumpad(false);
            }
        }
    }

    private class loginDrawerListener implements DrawerListener {
        View lastFocused;

        @Override
        public void onDrawerSlide(View view, float v) {
            if (v * 100 > Global.hide_keyboard_login_drawer_percentage) {
                if (pinET.hasFocus()) {
                    lastFocused = pinET;
                } else if (usernameET.hasFocus()) {
                    hideKeyboard(mActivity);
                    lastFocused = usernameET;
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
    }

    private OnClickListener numpadListener = new OnClickListener() {
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
                pinET.append(pinClicked.toString());
            } else if (pinClicked == 10) {
                openDrawer();
            } else if (pinClicked == 11) {
                if (pinET.getText().length() > 0) {
                    int cursorPosition = pinET.getSelectionStart();
                    //Log.d(TAG, "Pin Cursor: " + cursorPosition);
                    if (cursorPosition > 0) {
                        pinET.getText().delete(cursorPosition - 1, cursorPosition);
                    }
                }
            } else {
                Log.w(TAG, "Somehow pressed a non-numpad key");
            }
            Log.d(TAG, "Current Pin: " + pinET.getText().toString());
            pinLoginChecker(pinET);
        }
    };

    //private methods
    private void pinLoginChecker(EditText pin_EditText) {
        String pin = pin_EditText.getText().toString();
        if (pin.length() == 4) {
            finalUsername = usernameET.getText().toString();
            finalPin = pin;
            //show pop up dialog

            //start logging in
            startLoginProcess();
        }
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
            //startActivity(new Intent(CustomerLoginActivity.this, ApplicationActivity.class).putExtra(Intents.EXTRA_ACCOUNT_BALANCE.toString(), accountBalance));
        }
    }

    private void changeToCustomNumpad(boolean numpad) {
        int visibility = numericKeypad.getVisibility();
        if (numpad && visibility == View.GONE) {
            hideKeyboard(mActivity);
            numericKeypad.postDelayed(new Runnable() {
                @Override
                public void run() {
                    numericKeypad.setVisibility(View.VISIBLE);
                }
            }, 222);
        } else if (!numpad) {
            if (visibility == View.VISIBLE) {
                numericKeypad.setVisibility(View.GONE);
            }
            usernameET.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showKeyboard(mActivity, usernameET);
                }
            }, 30);
        }
    }

    private void startFragment(Fragment mFragment) {
        if (mFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.Activity_Main_FragmentLayout_FragmentContainer, mFragment);
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.addToBackStack(null);
            ft.commit();
            //setFunctionLayout(View.VISIBLE);
        }
    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String savedUsername = sharedPreferences.getString("Username", "");
        usernameET.setText(savedUsername);
        pinET.setText("");
        if (!savedUsername.equalsIgnoreCase("")) {
            pinET.requestFocus();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else {
            usernameET.requestFocus();
            showKeyboard(mActivity, usernameET);
        }
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //public methods
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

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
}


