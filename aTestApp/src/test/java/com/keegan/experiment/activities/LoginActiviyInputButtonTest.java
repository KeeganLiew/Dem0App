package com.keegan.experiment.activities;

import android.app.Activity;
import android.gesture.GestureOverlayView;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;

import com.keegan.experiment.BuildConfig;
import com.keegan.experiment.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

/**
 * Created by keegan on 20/01/16.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class LoginActiviyInputButtonTest {
    private Activity activity;

    @Before
    public void setup() {
        // Convenience method to run [LoginActivity] through the Activity Lifecycle methods:
        // onCreate(...) => onStart() => onPostCreate(...) => onResume()
        activity = Robolectric.setupActivity(LoginActivity.class);
    }

    // The test checks that [numeric keypad shows up] when we [select PIN input]
    @Test
    public void clickingPinButton_shouldOpenNumericKeypad() {
        Button button = (Button) activity.findViewById(R.id.InputOption_Button_Pin);
        TableLayout numericKeypad = (TableLayout) activity.findViewById(R.id.Activity_Login_NumericKeypad);
        GestureOverlayView loginGestureGOV = (GestureOverlayView) activity.findViewById(R.id.Activity_Login_InputOption_Gesture);

        button.performClick();
        System.out.println("numericKeypad.getVisibility(): " + numericKeypad.getVisibility());
        System.out.println("loginGestureGOV.getVisibility(): " + loginGestureGOV.getVisibility());
        assertTrue(numericKeypad.getVisibility() == View.VISIBLE);
        assertTrue(loginGestureGOV.getVisibility() == View.GONE);
    }

    // The test checks that [android keyboard shows up] when we [select Password input]
    @Test
    public void clickingPasswordButton_shouldOpenNormalKeyboard() {
        Button button = (Button) activity.findViewById(R.id.InputOption_Button_Password);
        TableLayout numericKeypad = (TableLayout) activity.findViewById(R.id.Activity_Login_NumericKeypad);
        GestureOverlayView loginGestureGOV = (GestureOverlayView) activity.findViewById(R.id.Activity_Login_InputOption_Gesture);

        button.performClick();
        System.out.println("numericKeypad.getVisibility(): " + numericKeypad.getVisibility());
        System.out.println("loginGestureGOV.getVisibility(): " + loginGestureGOV.getVisibility());
        assertTrue(numericKeypad.getVisibility() == View.GONE);
        assertTrue(loginGestureGOV.getVisibility() == View.GONE);
    }

    // The test checks that [gesture overlay shows up] when we [select Gesture input]
    @Test
    public void clickingGestureButton_shouldOpengestureOverlay() {
        Button button = (Button) activity.findViewById(R.id.InputOption_Button_Gesture);
        TableLayout numericKeypad = (TableLayout) activity.findViewById(R.id.Activity_Login_NumericKeypad);
        GestureOverlayView loginGestureGOV = (GestureOverlayView) activity.findViewById(R.id.Activity_Login_InputOption_Gesture);

        button.performClick();
        System.out.println("numericKeypad.getVisibility(): " + numericKeypad.getVisibility());
        System.out.println("loginGestureGOV.getVisibility(): " + loginGestureGOV.getVisibility());
        assertTrue(numericKeypad.getVisibility() == View.GONE);
        assertTrue(loginGestureGOV.getVisibility() == View.VISIBLE);
    }
}
