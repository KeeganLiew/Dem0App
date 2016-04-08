package com.keegan.experiment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.keegan.experiment.Global;
import com.keegan.experiment.R;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * Created by keegan on 16/02/16.
 */
public class SplashActivity extends RoboActivity implements OnClickListener {

    private final String TAG = SplashActivity.class.getSimpleName();

    @InjectView(R.id.Activity_Splash_RelativeLayout_WholeScreen)
    private RelativeLayout wholeScreenRL;
    @InjectView(R.id.Activity_Splash_KenBurnsView_Background)
    private KenBurnsView backgroundKBV;
    @InjectView(R.id.Activity_Splash_ImageView_Logo)
    private ImageView logoIV;
    @InjectView(R.id.Activity_Splash_CenterItem)
    private RelativeLayout centerItemRL;
    @InjectView(R.id.Activity_Splash_TextView_Title)
    private TextView titleTV;

    //findViewById injects
    /*private RelativeLayout wholeScreenRL;
    private KenBurnsView backgroundKBV;
    private ImageView logoIV;
    private RelativeLayout centerItemRL;
    private TextView titleTV;*/

    private int centerItemPosition;
    private int titleTVHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        viewObjectsInitializations();
        otherInitializations();
    }

    private void viewObjectsInitializations() {
        wholeScreenRL.setOnClickListener(this); //set listener
        /*wholeScreenRL = (RelativeLayout) findViewById(R.id.Activity_Splash_RelativeLayout_WholeScreen);
        backgroundKBV = (KenBurnsView) findViewById(R.id.Activity_Splash_KenBurnsView_Background);
        centerItemRL = (RelativeLayout) findViewById(R.id.Activity_Splash_CenterItem);
        titleTV = (TextView) findViewById(R.id.Activity_Splash_TextView_Title);
        logoIV = (ImageView) findViewById(R.id.Activity_Splash_ImageView_Logo);*/
    }

    private void otherInitializations() {
        RandomTransitionGenerator generator = new RandomTransitionGenerator(Global.actualTotalSplashScreenTime, new DecelerateInterpolator());
        backgroundKBV.setTransitionGenerator(generator);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            centerItemPosition = centerItemRL.getTop();
            titleTVHeight = titleTV.getHeight();
            animateLogo();
            animateMessage();
        }
    }

    @Override
    public void onClick(View v) {
        skipAnimationSplashScreen();
    }

    //private methods
    private void animateLogo() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, centerItemPosition);
        animation.setDuration(Global.animationTime);
        animation.setFillAfter(true);
        animation.setAnimationListener(new splashScreenAnimationListener());
        logoIV.startAnimation(animation);
    }

    private void animateMessage() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -(centerItemPosition + titleTVHeight));
        animation.setDuration(Global.animationTime);
        animation.setFillAfter(true);
        animation.setAnimationListener(new splashScreenAnimationListener());
        titleTV.startAnimation(animation);
    }

    private void skipAnimationSplashScreen() {
        logoIV.clearAnimation();
        titleTV.clearAnimation();
        backgroundKBV.pause();
        finishSplashScreen();
    }

    private void finishSplashScreen() {
        //wait a bit before ending
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(Global.afterAnimationWaitTime);
                    }
                } catch (InterruptedException ex) {
                    Log.d(TAG, ex.toString());
                }
                backgroundKBV.pause();
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        };
        thread.start();
    }

    //listeners
    private class splashScreenAnimationListener implements AnimationListener {
        @Override
        public void onAnimationEnd(Animation animation) {
            //logo
            logoIV.clearAnimation();
            LayoutParams logoLP = (LayoutParams) logoIV.getLayoutParams();
            logoLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
            logoLP.setMargins(0, centerItemPosition - logoIV.getHeight(), 0, 0);
            logoIV.setLayoutParams(logoLP);
            //message
            titleTV.clearAnimation();
            LayoutParams messageLP = (LayoutParams) titleTV.getLayoutParams();
            messageLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
            messageLP.setMargins(0, 0, 0, centerItemPosition - titleTVHeight);
            titleTV.setLayoutParams(messageLP);

            finishSplashScreen();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }
    }
}
