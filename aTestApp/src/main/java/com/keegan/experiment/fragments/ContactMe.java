package com.keegan.experiment.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.google.inject.Inject;
import com.keegan.experiment.Global;
import com.keegan.experiment.R;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by keegan on 10/02/16.
 */
public class ContactMe extends RoboFragment implements OnClickListener {

    private final String TAG = ContactMe.class.getSimpleName();

    @Inject
    Activity mActivity;

    @InjectView(R.id.Fragment_ContactMe_ProgressBar)
    private RelativeLayout progressBar;
    @InjectView(R.id.Fragment_ContactMe_WebView)
    private WebView webView;
    //findViewById injects
    /*private WebView webView;
    private RelativeLayout progressBar;*/

    private String url = Global.KEEGAN_LINKEDIN_URL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_me, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        initializeViewObjects();
    }

    private void initializeViewObjects() {
        /*progressBar = (RelativeLayout) rootView.findViewById(R.id.Fragment_ContactMe_ProgressBar);
        webView = (WebView) rootView.findViewById(R.id.Fragment_ContactMe_WebView);*/
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                Log.d(TAG, "progress: " + progress);
                if (progress >= 100) { //show webview when something finished loading
                    progressBar.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
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
