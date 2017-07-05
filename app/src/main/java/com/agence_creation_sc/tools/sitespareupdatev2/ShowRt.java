package com.agence_creation_sc.tools.sitespareupdatev2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Stéf on 19/01/2016.
 */
public class ShowRt extends Fragment {

    public static ShowRt newInstance() {
        ShowRt fragment = new ShowRt();
        return fragment;
    }

    public ShowRt() {

    }
    private WebView wv1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View setView = inflater.inflate(R.layout.webviewrt, container, false);

        wv1 = (WebView) setView.findViewById(R.id.webview);



return setView;
    }
            @Override
            public void onResume(){
                super.onResume();
                showrtweb();
            }

    @Override
 public void onStop(){
        super.onStop();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
public void showrtweb(){
    final ProgressDialog dialog = ProgressDialog.show(getContext(), "", "Chargement en cours", true);
    dialog.setCanceledOnTouchOutside(true);
    wv1 .getSettings().setJavaScriptEnabled(true);
    wv1 .getSettings().setLoadWithOverviewMode(true);
    wv1 .getSettings().setUseWideViewPort(true);
    wv1 .getSettings().setBuiltInZoomControls(true);

    wv1 .setWebViewClient(new WebViewClient() {

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            dialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            dialog.dismiss();
            wv1.getUrl();
        }


    });

    wv1 .loadUrl("http://rt.networks-technologies.org:8080/");
}

}
