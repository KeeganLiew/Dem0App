package com.keegan.experiment.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.keegan.experiment.Global;
import com.keegan.experiment.Intents;
import com.keegan.experiment.R;
import com.keegan.experiment.utilities.DisplayPictureUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private final String TAG = LoginActivity.class.getSimpleName();

    //navigation drawer
    private NavigationView navigationNV;
    private DrawerLayout mDrawerLayout;
    //navigation drawer items
    private LinearLayout navDrawerItemNewUserLL;
    private LinearLayout navDrawerItemAuthOptionLL;
    private LinearLayout navDrawerItemForgotPinLL;
    private LinearLayout navDrawerItemHelpLL;
    private LinearLayout navDrawerItemContactLL;

    //display picture and username
    private ImageView displayPictureIV;
    private EditText usernameET;

    //pin authentication
    private EditText pinET;
    //password authentication
    private RelativeLayout passwordRL;
    private EditText passwordET;
    private ImageView passwordLoginButtonIV;
    //gesture authentication
    private GestureOverlayView loginGestureGOV;

    //authentication option
    private Button authenticationOptionPinB;
    private Button authenticationOptionPasswordB;
    private Button authenticationOptionGestureB;

    //login pop up dialog
    private RelativeLayout loginProgressDialogRL;
    private TextView loginProgressDialogTextTV;
    //login error pop up dialog
    private RelativeLayout loginResultDialogRL;
    private TextView loginResultDialogTextTV;
    private Button loginResultDialogB;

    //custom numpad
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
    private TextView numBackspace;
    private View currentlyPressedKey;

    //non-view object variables
    private String finalUsername;
    private String finalPin;
    private TextView[] numpadList;
    private Button authenticationOption[];
    private int currentAuthenticationOption;
    private GestureLibrary loginGestureLibrary;
    private double loginGestureScore;

    private Activity mActivity;
    private Context mContext;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewObjectsInitializations();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void viewObjectsInitializations() {
        //navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.Activity_Login_DrawerLayout);
        mDrawerLayout.setDrawerListener(new loginDrawerListener()); //set listener
        navigationNV = (NavigationView) findViewById(R.id.Activity_Login_NavigationView_Navigation);
        //navigation drawer items
        navDrawerItemNewUserLL = (LinearLayout) findViewById(R.id.Activity_Login_NewUser);
        navDrawerItemForgotPinLL = (LinearLayout) findViewById(R.id.Activity_Login_ForgotPin);
        navDrawerItemAuthOptionLL = (LinearLayout) findViewById(R.id.Activity_Login_AuthenticationOption);
        navDrawerItemHelpLL = (LinearLayout) findViewById(R.id.Activity_Login_Help);
        navDrawerItemContactLL = (LinearLayout) findViewById(R.id.Activity_Login_Contact);

        //display picture and username
        displayPictureIV = (ImageView) findViewById(R.id.Activity_Login_ImageView_DisplayPicture);
        usernameET = (EditText) findViewById(R.id.Activity_Login_EditText_Name);
        usernameET.setOnFocusChangeListener(new usernameEditTextOnFocusChange()); //set listener

        //pin authentication
        pinET = (EditText) findViewById(R.id.Activity_Login_EditText_Pin);
        pinET.setOnFocusChangeListener(new pinEditTextOnFocusChange()); //set listener
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            pinET.setShowSoftInputOnFocus(false); //disable keyboard to use custom keyboard
        }
        //password authentication
        passwordRL = (RelativeLayout) findViewById(R.id.Activity_Login_RelativeLayout_Password);
        passwordET = (EditText) findViewById(R.id.Activity_Login_EditText_Password);
        passwordET.setOnEditorActionListener(new passwordEditTextOnEditorActionListener()); //set listener
        passwordLoginButtonIV = (ImageView) findViewById(R.id.Activity_Login_ImageView_PasswordLogin);
        passwordLoginButtonIV.setOnClickListener(this); //set listener
        //gesture authentication
        loginGestureGOV = (GestureOverlayView) findViewById(R.id.Activity_Login_InputOption_Gesture);
        loginGestureGOV.addOnGesturePerformedListener(new loginGestureOnGesturePerformedListener()); //set listener

        //authentication option
        authenticationOptionPinB = (Button) findViewById(R.id.InputOption_Button_Pin);
        authenticationOptionPasswordB = (Button) findViewById(R.id.InputOption_Button_Password);
        authenticationOptionGestureB = (Button) findViewById(R.id.InputOption_Button_Gesture);

        //login pop up dialog
        loginProgressDialogRL = (RelativeLayout) findViewById(R.id.Activity_Login_PopUpProgressBarDialog);
        loginProgressDialogTextTV = (TextView) findViewById(R.id.Pop_Up_Progress_Bar_Dialog_TextView_Label);
        //login error pop up dialog
        loginResultDialogRL = (RelativeLayout) findViewById(R.id.Activity_Login_PopUpButtonDialog);
        loginResultDialogTextTV = (TextView) findViewById(R.id.Pop_Up_Button_Dialog_TextView_Label);
        loginResultDialogB = (Button) findViewById(R.id.Pop_Up_Button_Dialog_Button);
        loginResultDialogB.setOnClickListener(this); //set listener

        //custom numpad
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
        numBackspace = (TextView) findViewById(R.id.custom_numeric_keyboard_key_backspace);

        viewObjectLogic();
    }

    private void viewObjectLogic() {
        //navigation drawer
        LinearLayout[] navMenuList = new LinearLayout[]{navDrawerItemNewUserLL, navDrawerItemForgotPinLL, navDrawerItemHelpLL, navDrawerItemContactLL};
        int[] navMenuImages = new int[]{R.drawable.ic_person_add_white_48dp, R.drawable.ic_backspace_white_small, R.drawable.ic_help_white_48dp, R.drawable.header};
        int[] navMenuTexts = new int[]{R.string.new_user, R.string.forgot_pin, R.string.help, R.string.contact};

        for (int i = 0; i < navMenuList.length; i++) {
            ImageView tempIV = (ImageView) navMenuList[i].findViewById(R.id.login_navigation_icon);
            TextView tempTV = (TextView) navMenuList[i].findViewById(R.id.login_navigation_text);
            tempIV.setBackgroundResource(navMenuImages[i]);
            tempTV.setText(navMenuTexts[i]);
            navMenuList[i].setOnClickListener(this); //set listener
        }

        //gesture recognition
        loginGestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gesture_default);
        if (!loginGestureLibrary.load()) { //if no saved gesture to pattern match
            authenticationOptionGestureB.setEnabled(false); //disable button
            authenticationOptionGestureB.setAlpha(0.3f); //draw 30% to show as disabled state
        }

        //authentication options
        authenticationOption = new Button[]{authenticationOptionPinB, authenticationOptionPasswordB, authenticationOptionGestureB};
        for (Button button : authenticationOption) {
            button.setOnTouchListener(new authOptionButtonOnTouchListener()); //set listener
        }

        //numpadListener
        numpadList = new TextView[]{num1, num2, num3, num4, num5, num6, num7, num8, num9, numMenu, num0, numBackspace};
        for (TextView number : numpadList) {
            number.setOnTouchListener(new customNumpadOnTouchListener()); //set listener
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //android activity and context
        mActivity = this;
        mContext = getApplicationContext();

        //load profile
        loadUserProfile();

        //if selected password input - reshow keyboard
        if (currentAuthenticationOption == R.id.InputOption_Button_Password) {
            passwordET.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showKeyboard(mActivity, passwordET);
                }
            }, 30);
        }

        //auth buttons
        Global.LoginInputMethod selectedAuthOption = Global.LoginInputMethod.lookupByCode(
                Global.loadSavedPreferences(mActivity, Global.sharedPref_AuthOption, Global.authOption_default.getCode()));

        if (selectedAuthOption == Global.LoginInputMethod.PIN_INPUT) {
            authenticationOptionPinB.setSelected(true);
        } else if (selectedAuthOption == Global.LoginInputMethod.PASSWORD_INPUT) {
            authenticationOptionPasswordB.setSelected(true);
        } else if (selectedAuthOption == Global.LoginInputMethod.GESTURE_INPUT) {
            authenticationOptionGestureB.setSelected(true);
        }

        //broadcast receiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(TAG, "Received Intent: " + intent.getAction());

                if (Intents.LOGIN_SUCCESS.equalsName(action)) {
                    enableAndShowViews(true);
                } else if (Intents.LOGIN_FAIL.equalsName(action)) {
                    loginProgressDialogRL.setVisibility(View.GONE);
                    Global.LoginInputMethod loginInputMethod = (Global.LoginInputMethod) intent.getSerializableExtra("LoginInputMethod");

                    if (loginInputMethod == Global.LoginInputMethod.PIN_INPUT) {
                        loginResultDialogTextTV.setText(R.string.login_pin_incorrect);
                    } else if (loginInputMethod == Global.LoginInputMethod.PASSWORD_INPUT) {
                        loginResultDialogTextTV.setText(R.string.login_password_incorrect);
                    } else if (loginInputMethod == Global.LoginInputMethod.GESTURE_INPUT) {
                        loginResultDialogTextTV.setText(R.string.login_gesture_incorrect);
                    }

                    loginResultDialogRL.setVisibility(View.VISIBLE);
                }
            }
        };
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(broadcastReceiver, new IntentFilter(Intents.LOGIN_SUCCESS.toString()));
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(broadcastReceiver, new IntentFilter(Intents.LOGIN_FAIL.toString()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) { ////TODO: 16/12/15 implement nav drawer item functions
            //navigation drawer items
            case R.id.Activity_Login_NewUser:
                closeDrawer();
                break;
            case R.id.Activity_Login_ForgotPin:
                closeDrawer();
                break;
            case R.id.Activity_Login_AuthenticationOption:
                showAuthOption();
                break;
            case R.id.Activity_Login_Help:
                closeDrawer();
                break;
            case R.id.Activity_Login_Contact:
                closeDrawer();
                break;
            //others
            case R.id.Pop_Up_Button_Dialog_Button:
                enableAndShowViews(true);
                break;
            case R.id.Activity_Login_ImageView_PasswordLogin:
                startLoginProcess(Global.LoginInputMethod.PASSWORD_INPUT);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeDrawer();
        } else {
            this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboard(mActivity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyboard(mActivity);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(broadcastReceiver);
    }

    //Listeners
    private class pinEditTextOnFocusChange implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Log.d(TAG, "pinET's focus: " + hasFocus);
            changeToCustomNumpad(hasFocus);
        }
    }

    private class usernameEditTextOnFocusChange implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Log.d(TAG, "usernameET's focus: " + hasFocus);
            if (hasFocus) {
                changeToCustomNumpad(false);
            }
        }
    }

    private class passwordEditTextOnEditorActionListener implements OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) { //if pressed Done key
                startLoginProcess(Global.LoginInputMethod.PASSWORD_INPUT); //log in
            }
            return false;
        }
    }

    private class loginDrawerListener implements DrawerListener {
        View lastFocused;

        @Override
        public void onDrawerSlide(View view, float v) {
            if (v * 100 > Global.hide_keyboard_login_drawer_percentage) { //hide input when opening
                if (pinET.hasFocus()) {
                    clearCurrencyPressedKeyColour();
                    changeToCustomNumpad(false);
                    lastFocused = pinET;
                } else if (usernameET.hasFocus()) {
                    hideKeyboard(mActivity);
                    lastFocused = usernameET;
                } else if (passwordET.hasFocus()) {
                    hideKeyboard(mActivity);
                    lastFocused = passwordET;
                } else if (authenticationOptionGestureB.hasFocus()) {
                    lastFocused = authenticationOptionGestureB;
                }
            }

            if (v * 100 == 0) { //show input when closed
                usernameET.requestFocus();
                if (lastFocused != null) {
                    Log.d(TAG, lastFocused.toString() + " requestFocus()");
                    lastFocused.requestFocus();
                }
            }
        }

        @Override
        public void onDrawerOpened(View view) {
        }

        @Override
        public void onDrawerClosed(View view) {
        }

        @Override
        public void onDrawerStateChanged(int i) {
        }
    }

    private class authOptionButtonOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setAuthenticationOption((Button) v);
            }
            return false;
        }
    }

    private class customNumpadOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            clearCurrencyPressedKeyColour();
            if (event.getAction() == MotionEvent.ACTION_DOWN) { //when pressed
                currentlyPressedKey = view;
                view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.custom_numpad_color_pressed));
            } else if (event.getAction() == MotionEvent.ACTION_UP) { //when finish pressing
                //currentlyPressedKey = null;
                view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.custom_numpad_color));
                Integer pinClicked = Arrays.asList(Global.numpadKeysData).indexOf(view.getId());
                if (pinClicked >= 0 && pinClicked <= 9) { //0 to 9 keys
                    pinET.append(pinClicked.toString());
                } else if (pinClicked == 10) { //menu key
                    openDrawer();
                } else if (pinClicked == 11) { //delete key
                    if (pinET.getText().length() > 0) {
                        int cursorPosition = pinET.getSelectionStart();
                        if (cursorPosition > 0) {
                            pinET.getText().delete(cursorPosition - 1, cursorPosition);
                        }
                    }
                }
                Log.d(TAG, "Current Pin: " + pinET.getText().toString());
                pinLoginChecker(pinET);
            }
            return false;
        }
    }

    private class loginGestureOnGesturePerformedListener implements GestureOverlayView.OnGesturePerformedListener {
        @Override
        public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
            ArrayList<Prediction> predictions = loginGestureLibrary.recognize(gesture);
            loginGestureScore = predictions.get(0).score;
            startLoginProcess(Global.LoginInputMethod.GESTURE_INPUT);
            /*Log.d(TAG, "loginGestureScore: " + loginGestureScore);
            if (predictions.size() > 0 && predictions.get(0).score > Global.min_gesture_score) {
                String action = predictions.get(0).name;
                Log.d(TAG, "gesture action: " + action);
                Toast.makeText(mActivity, action, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mActivity, getString(R.string.toast_sms_sent_fail), Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    //private methods
    private void changeToCustomNumpad(boolean numpad) {
        int visibility = numericKeypad.getVisibility();
        if (numpad && visibility == View.GONE) {
            hideKeyboard(mActivity);
            /*numericKeypad.postDelayed(new Runnable() {
                @Override
                public void run() {
                    numericKeypad.setVisibility(View.VISIBLE);
                }
            }, 222);*/
            numericKeypad.setVisibility(View.VISIBLE);
        } else if (!numpad) {
            if (visibility == View.VISIBLE) {
                numericKeypad.setVisibility(View.GONE);
            }
            /*usernameET.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showKeyboard(mActivity, usernameET);
                }
            }, 30);*/
            showKeyboard(mActivity, usernameET);
        }
    }

    private void clearCurrencyPressedKeyColour() {
        if (currentlyPressedKey != null) {
            currentlyPressedKey.setBackgroundColor(ContextCompat.getColor(mContext, R.color.custom_numpad_color));
        }
    }

    private void enableAndShowViews(boolean bool) { ////TODO: 11/12/15 rename
        if (bool) { //reset after login attempt
            loginResultDialogRL.setVisibility(View.GONE);
            loginProgressDialogRL.setVisibility(View.GONE);
            clearPinAndPassword();
            if (passwordET.hasFocus()) {
                showKeyboard(mActivity, passwordET);
            }
        } else { //logging in UI
            hideKeyboard(mActivity);
            loginProgressDialogRL.setVisibility(View.VISIBLE);
            loginProgressDialogTextTV.setText(getString(R.string.login_login_in));
        }
        setEnabledNumpad(bool);
    }

    private void clearPinAndPassword() {
        pinET.setText(Global.EMPTY_STRING); //clear pin
        passwordET.setText(Global.EMPTY_STRING); //clear password
    }

    private void setEnabledNumpad(boolean bool) { //enable/disable custom numpad
        for (TextView number : numpadList) {
            number.setEnabled(bool);
        }
    }

    private void setAuthenticationOption(Button selectedButton) { ////TODO: 22/02/15 refactor parameters
        currentAuthenticationOption = selectedButton.getId();
        for (Button button : authenticationOption) {
            button.setSelected(false);
        }
        clearPinAndPassword();
        selectedButton.setSelected(true);
        numericKeypad.setVisibility(View.GONE);
        pinET.setVisibility(View.GONE);
        passwordRL.setVisibility(View.GONE);
        loginGestureGOV.setVisibility(View.GONE);
        hideKeyboard(mActivity);
        switch (selectedButton.getId()) {
            case R.id.InputOption_Button_Pin:
                Global.savePreferences(mActivity, Global.sharedPref_AuthOption, Global.LoginInputMethod.PIN_INPUT.getCode());
                pinET.setVisibility(View.VISIBLE);
                pinET.requestFocus();
                break;
            case R.id.InputOption_Button_Password:
                Global.savePreferences(mActivity, Global.sharedPref_AuthOption, Global.LoginInputMethod.PASSWORD_INPUT.getCode());
                passwordRL.setVisibility(View.VISIBLE);
                passwordET.requestFocus();
                showKeyboard(mActivity, passwordET);
                break;
            case R.id.InputOption_Button_Gesture:
                Global.savePreferences(mActivity, Global.sharedPref_AuthOption, Global.LoginInputMethod.GESTURE_INPUT.getCode());
                selectedButton.setFocusableInTouchMode(true);
                selectedButton.requestFocus();
                loginGestureGOV.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void loadUserProfile() {
        //load username
        String savedUsername = Global.loadSavedPreferences(
                mActivity, Global.sharedPref_Username, Global.username_default);
        usernameET.setText(savedUsername);
        if (!savedUsername.equalsIgnoreCase(Global.username_default)) {
            pinET.requestFocus(); //request focus to show custom numpad
            mActivity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hide android keyboard
        } else {
            usernameET.requestFocus();
            showKeyboard(mActivity, usernameET);
        }

        //load display picture
        Bitmap displayPicture = DisplayPictureUtil.getDisplayPictureFromStorage(
                new ContextWrapper(mContext).getDir(Global.profileImgDirName, Context.MODE_PRIVATE).getPath(),
                Global.profilePicImgName);
        if (displayPicture == null) {
            displayPictureIV.setImageResource(R.drawable.name); //use default picture
        } else {
            Global.loadImageIntoImageView(mContext, displayPictureIV, Global.profilePicImgName);
        }
    }

    private void showAuthOption() {
        final Dialog authOptionDialog = new Dialog(this);
        authOptionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        authOptionDialog.setContentView(R.layout.dialog_auth_option);

        final RadioGroup authOptionRG = (RadioGroup) authOptionDialog.findViewById(R.id.RadioGroup_AuthOption);
        final RadioButton pinRB = (RadioButton) authOptionDialog.findViewById(R.id.RadioButton_Pin);
        final RadioButton passwordRB = (RadioButton) authOptionDialog.findViewById(R.id.RadioButton_Password);
        final RadioButton gestureRB = (RadioButton) authOptionDialog.findViewById(R.id.RadioButton_Gesture);

        final CheckBox authOptionCB = (CheckBox) authOptionDialog.findViewById(R.id.CheckBox_AuthOption);
        final Button okB = (Button) authOptionDialog.findViewById(R.id.Dialog_Button_Ok);
        final Button cancelB = (Button) authOptionDialog.findViewById(R.id.Dialog_Button_Cancel);
        final LinearLayout authOptionLL = (LinearLayout) findViewById(R.id.Activity_Login_InputOption);

        Global.LoginInputMethod selectedAuthOption = Global.LoginInputMethod.lookupByCode(
                Global.loadSavedPreferences(mActivity, Global.sharedPref_AuthOption, Global.authOption_default.getCode()));

        if (selectedAuthOption == Global.LoginInputMethod.PIN_INPUT) {
            pinRB.setChecked(true);
        } else if (selectedAuthOption == Global.LoginInputMethod.PASSWORD_INPUT) {
            passwordRB.setChecked(true);
        } else if (selectedAuthOption == Global.LoginInputMethod.GESTURE_INPUT) {
            gestureRB.setChecked(true);
        }

        if (!Global.checkContainsSharedPreferences(mActivity, Global.sharedPref_ShowAuthOptions)) {
            Global.savePreferences(mActivity, Global.sharedPref_ShowAuthOptions, Global.showAuthOptions_default);
        }
        authOptionCB.setChecked(Global.loadSavedPreferences(mActivity, Global.sharedPref_ShowAuthOptions, Global.showAuthOptions_default));

        cancelB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                authOptionDialog.dismiss();
            }
        });

        okB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                int authOptionRB = authOptionRG.getCheckedRadioButtonId();
                ////TODO: 21/02/15 combine radioButtons and buttons for authOption

                switch (authOptionRB) {
                    case R.id.RadioButton_Pin:
                        setAuthenticationOption(authenticationOptionPinB);
                        break;
                    case R.id.RadioButton_Password:
                        setAuthenticationOption(authenticationOptionPasswordB);
                        break;
                    case R.id.RadioButton_Gesture:
                        setAuthenticationOption(authenticationOptionGestureB);
                        break;
                }

                if (authOptionCB.isChecked()) {
                    authOptionLL.setVisibility(View.VISIBLE);
                } else {
                    authOptionLL.setVisibility(View.GONE);
                }
                Global.savePreferences(mActivity, Global.sharedPref_ShowAuthOptions, authOptionCB.isChecked());

                authOptionDialog.dismiss();
                closeDrawer();
            }
        });
        authOptionDialog.show();
    }

    private void pinLoginChecker(EditText pin_EditText) {
        String pin = pin_EditText.getText().toString();
        if (pin.length() >= Global.pin_text_limit) {
            finalPin = pin;
            //start logging in
            startLoginProcess(Global.LoginInputMethod.PIN_INPUT);
        }
    }

    private void startLoginProcess(final Global.LoginInputMethod loginInputMethod) {
        enableAndShowViews(false);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) { //simulate login process
                        wait(2000);
                    }
                } catch (InterruptedException ex) {
                    Log.d(TAG, ex.toString());
                }

                Boolean youShallPass = false;
                if (loginInputMethod == Global.LoginInputMethod.PIN_INPUT) {
                    youShallPass = pinET.getVisibility() == View.VISIBLE && pinET.getText().toString().equalsIgnoreCase(Global.pin_default);
                } else if (loginInputMethod == Global.LoginInputMethod.PASSWORD_INPUT) {
                    youShallPass = passwordET.getVisibility() == View.VISIBLE && passwordET.getText().toString().equals(Global.password_default);
                } else if (loginInputMethod == Global.LoginInputMethod.GESTURE_INPUT) {
                    youShallPass = loginGestureScore > Global.min_gesture_score;
                }

                Intent intent;
                if (youShallPass) {
                    //save username
                    finalUsername = usernameET.getText().toString();
                    Global.savePreferences(mActivity, Global.sharedPref_Username, finalUsername);
                    //start main activity with extra info
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle b = new Bundle();
                    b.putString(Global.sharedPref_Username, finalUsername); //Your id
                    b.putString("Pin", finalPin); //Your id
                    b.putBoolean("OpenNavDraw", false); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                    //finish();
                    //startActivity(new Intent(CustomerLoginActivity.this, ApplicationActivity.class).putExtra(Intents.EXTRA_ACCOUNT_BALANCE.toString(), accountBalance));
                    //} else if () {
                    //send success intent
                    Intent successIntent = new Intent(Intents.LOGIN_SUCCESS.toString());
                    LocalBroadcastManager.getInstance(mActivity).sendBroadcast(successIntent);
                    Log.d(TAG, "Sending Intent: " + successIntent.getAction());
                } else {
                    intent = new Intent(Intents.LOGIN_FAIL.toString());
                    intent.putExtra("LoginInputMethod", loginInputMethod);
                    LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                    Log.d(TAG, "Sending Intent: " + intent.getAction());
                }
            }
        };
        thread.start();
    }

    //not used yet
    /*public void saveSig(View view) {
        try {
            //GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.loginGestureGOV);
            loginGestureGOV.setDrawingCacheEnabled(true);
            Bitmap bm = Bitmap.createBitmap(loginGestureGOV.getDrawingCache());
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "signature.png");
            f.createNewFile();
            FileOutputStream os = new FileOutputStream(f);
            os = new FileOutputStream(f);
            //compress to specified format (PNG), quality - which is ignored for PNG, and out stream
            bm.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
        } catch (Exception e) {
            Log.v("Gestures", e.getMessage());
            e.printStackTrace();
        }
    }*/

    //not used yet
    /*private void startFragment(Fragment mFragment) {
        if (mFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.Activity_Main_FragmentLayout_FragmentContainer, mFragment);
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.addToBackStack(null);
            ft.commit();
            //setFunctionLayout(View.VISIBLE);
        }
    }*/

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


