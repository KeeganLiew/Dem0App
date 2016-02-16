package com.keegan.experiment.fragments;

import android.app.Fragment;
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

import com.keegan.experiment.Global;
import com.keegan.experiment.R;

/**
 * Created by keegan on 10/02/16.
 */
public class ContactMe extends Fragment implements OnClickListener {

    private final String TAG = ContactMe.class.getSimpleName();
    private WebView webView;
    private RelativeLayout progressBar;
    private String url = Global.KEEGAN_LINKEDIN_URL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_me, container, false);
        progressBar = (RelativeLayout) rootView.findViewById(R.id.Fragment_ContactMe_ProgressBar);

        webView = (WebView) rootView.findViewById(R.id.Fragment_ContactMe_WebView);
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

        return rootView;
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
