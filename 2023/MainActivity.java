package com.xxx.xxx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {


    //Webview related variables
    WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
            /*
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                //browser.setVisibility(View.GONE);

                if(urlx.contains("pustakasyiah.icc")){
                    viewx.loadUrl(urlx);
                    return false;
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlx));
                    startActivity(intent);
                    return true;
                }

            }
            */

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                browser.loadUrl("file:///android_asset/error.html");
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                browser.setVisibility(View.VISIBLE);

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {

            }

        });
    }


    //Share this app
    public void launchSharer(String msg, String titlemsg) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, titlemsg);
            String shareMessage = msg+"\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName() +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    //Share a text
    public void launchTextSharer(String msg, String titlemsg){
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, titlemsg);
            String shareMessage = msg;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    public class WebAppInterface {

        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }


        @JavascriptInterface
        public void showAlert(String alerttext){
            Toast.makeText(getApplicationContext(), alerttext, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void portrait() {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        @JavascriptInterface
        public void landscape() {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        @JavascriptInterface
        public void rateThisApp(){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(browserIntent);
        }

        @JavascriptInterface
        public void shareText(String txt, String title){
            launchTextSharer(txt, title);
        }

        @JavascriptInterface
        public void shareThisApp(String txt, String title){
            launchSharer(txt, title);
        }

        @JavascriptInterface
        public void downloadFile(String fileurl){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileurl));
            startActivity(intent);
        }

    }



    //For webview...
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView browser = findViewById(R.id.webView);
        if ((keyCode == KeyEvent.KEYCODE_BACK) && browser.canGoBack()) {
            //if Back key pressed and webview can navigate to previous page
            browser.goBack();
            // go back to previous page
            return true;
        }
        else
        {
            //finish();
            // finish the activity
        }
        return super.onKeyDown(keyCode, event);
    }




}