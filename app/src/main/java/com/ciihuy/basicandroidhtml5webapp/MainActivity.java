package com.ciihuy.basicandroidhtml5webapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    AdView mAdView;
    private InterstitialAd mInterstitialAd;
    boolean intCanShow = true;
    boolean bannerCanShow = false;
    boolean mustDestroyBanner = false;

    ProgressDialog progress;

    WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browser = findViewById(R.id.webView);
        browser.setWebChromeClient(new WebChromeClient());
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setDomStorageEnabled(true);
        browser.getSettings().setAllowFileAccessFromFileURLs(true);
        browser.getSettings().setAllowUniversalAccessFromFileURLs(true);
        browser.addJavascriptInterface(new WebAppInterface(this), "Android");
        browser.loadUrl("file:///android_asset/index.html");
        browser.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                browser.setVisibility(View.GONE);
                if(!urlx.contains("http")){
                    viewx.loadUrl(urlx);
                    return false;
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlx));
                    startActivity(intent);
                    return true;
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                browser.loadUrl("file:///android_asset/error.html");
            }

            @Override
            public void onPageFinished(WebView view, String url){
                progress.dismiss();
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                progress = ProgressDialog.show(MainActivity.this, null, "Please wait...", true);
                progress.setCanceledOnTouchOutside(false);
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        initAd();
        initBanner();
        destroyBanner();
    }


    public void initAd(){
        if(intCanShow){
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
        intCanShow = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initAd();
            }
        }, 1000);
    }

    public void initBanner(){
        if(bannerCanShow){
            showBanner();
        }
        bannerCanShow = false;
        if(mustDestroyBanner){
            destroyBanner();
        }
        mustDestroyBanner = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initBanner();
            }
        }, 1000);
    }

    public void showBanner(){
        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView.setVisibility(View.VISIBLE);
    }

    public void destroyBanner(){
        mAdView.setVisibility(View.GONE);
    }

    public class WebAppInterface {

        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showAd(){
            intCanShow = true;
        }

        @JavascriptInterface
        public void showAndroidBanner() {
            bannerCanShow = true;
        }

        @JavascriptInterface
        public void hideAndroidBanner() {
            mustDestroyBanner = true;
        }

        @JavascriptInterface
        public void removeAds() {

        }

        @JavascriptInterface
        public void portrait() {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        @JavascriptInterface
        public void landscape() {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

    }

}
