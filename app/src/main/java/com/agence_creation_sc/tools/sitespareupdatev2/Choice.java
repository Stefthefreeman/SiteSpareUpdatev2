package com.agence_creation_sc.tools.sitespareupdatev2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Stéf on 31/01/2016.
 */
public class Choice extends Fragment {

    public static Choice newInstance() {
        Choice fragment = new Choice();
        return fragment;
    }

    public Choice() {

    }
    Animation animFadein;
    Animation rotate;
    Animation bounce;
    Animation slide;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View setView = inflater.inflate(R.layout.choice, container, false);
        animFadein = AnimationUtils.loadAnimation(getActivity(),
                R.anim.fade_in);
        rotate = AnimationUtils.loadAnimation(getActivity(),
                R.anim.rotate);
        bounce = AnimationUtils.loadAnimation(getActivity(),
                R.anim.bounce);
        slide=AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up);

        Button barcodes = (Button) setView.findViewById(R.id.barcode);
        Button qrcodes = (Button) setView.findViewById(R.id.qronly);
        Button barokqrhs = (Button) setView.findViewById(R.id.qrokbarcodehs);
        Button barhsqrok = (Button) setView.findViewById(R.id.qrokbarhs);
        barcodes.startAnimation(animFadein);
        qrcodes.startAnimation(bounce);
        PackageManager packageManager = getActivity().getPackageManager();
        Intent verify = new Intent( );
        verify.setPackage("com.google.zxing.client.android" );
        List<ResolveInfo> activities = packageManager.queryIntentActivities(verify, 0);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {

        } else {
            Toast.makeText(getContext(), "Vous devez installer BarcodeScanner!", Toast.LENGTH_LONG).show();
            Intent goToMarket = new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("market://details?id=com.google.zxing.client.android"));
            startActivity(goToMarket);

        }
        // final String numero = editText.getText().toString();
        barcodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String EXTRA_NUMRT = "lenumero";
               // String ticknum= getTitle().toString();
                Intent barok = new Intent(getActivity(),Destock.class);
             //   barok.putExtra(EXTRA_NUMRT,ticknum);
                startActivity(barok);

            }
        });
        qrcodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String EXTRA_NUMRT = "lenumero";
               // String ticknum= getTitle().toString();
                Intent qrok = new Intent(getActivity(), QrCodesOnly.class);
              //  qrok.putExtra(EXTRA_NUMRT,ticknum);
                startActivity(qrok);
            }
        });
        barhsqrok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String EXTRA_NUMRT = "lenumero";
               // String ticknum= getTitle().toString();
                Intent qrhsbarok = new Intent(getActivity(),QrhsBarok.class);
               // qrhsbarok.putExtra(EXTRA_NUMRT,ticknum);
                startActivity(qrhsbarok);
            }
        });
        barokqrhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String EXTRA_NUMRT = "lenumero";
               // String ticknum = getTitle().toString();
                Intent qrokbarhs = new Intent(getActivity(), QrokBarhs.class);
               // qrokbarhs.putExtra(EXTRA_NUMRT, ticknum);
                startActivity(qrokbarhs);
            }
        });


        return setView;
    }

}
