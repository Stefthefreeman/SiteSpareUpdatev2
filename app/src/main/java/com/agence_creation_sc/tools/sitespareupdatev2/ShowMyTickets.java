package com.agence_creation_sc.tools.sitespareupdatev2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
public class ShowMyTickets extends Fragment {

    public static ShowMyTickets newInstance() {
        ShowMyTickets fragment = new ShowMyTickets();
        return fragment;
    }

    public ShowMyTickets() {

    }

    ProgressDialog dialog;
    WebView wv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View setView = inflater.inflate(R.layout.webviewrt, container, false);

        wv1 = (WebView) setView.findViewById(R.id.webview);

        return setView;
    }
   @Override
    public void onResume(){
       super.onResume();

       dialog = ProgressDialog.show(getContext(), "", "Chargement en cours", true);
       dialog.setCanceledOnTouchOutside(true);
       wv1 .getSettings().setJavaScriptEnabled(true);
       wv1 .getSettings().setLoadWithOverviewMode(true);
       wv1 .getSettings().setUseWideViewPort(true);
       wv1 .getSettings().setBuiltInZoomControls(true);

       wv1 .setWebViewClient(new WebViewClient() {


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
       UserBDD owner = new UserBDD(getContext());
       owner.open();
       User perso =owner.getFirstUser("1");
       if (perso != null) {
           wv1 .loadUrl("http://www.tools.agence-creation-sc.com/example/show_my_tickets.php?userrt="+perso.getUserrt()+"");

       }
       owner.close();



   }
    @Override
    public void onStop(){
        super.onStop();
        Log.e("DEBUG", "onStop of Myticketlist");
    }



}

