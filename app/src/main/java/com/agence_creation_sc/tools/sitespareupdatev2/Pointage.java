package com.agence_creation_sc.tools.sitespareupdatev2;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Pointage extends Fragment {

    // TODO: Rename and change types and number of parameters
    public static Pointage newInstance() {
        Pointage fragment = new Pointage();
        return fragment;
    }

    public Pointage() {
        // Required empty public constructor
    }


    ProgressDialog progressDialog;
    String nomrt;
    String currentDateandTime;
    String strDate;
    GPSTracker gps;
    String site;
    Button debut;
    Button fin;
    TextView showdate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View setView = inflater.inflate(R.layout.fragment_pointage, container, false);
        debut = (Button) setView.findViewById(R.id.buttondeb);
        fin = (Button) setView.findViewById(R.id.buttonfin);
        showdate = (TextView) setView.findViewById(R.id.showdate_p);
        UserBDD wmess = new UserBDD(getActivity());
        wmess.open();
        User perso = wmess.getFirstUser("1");
        nomrt = perso.getUserrt().trim();
        Object[] params = new Object[] { new Date() };
        String msg = MessageFormat.format("{0,date,long}",
                params);
        showdate.setText(msg);
        currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.FRANCE).format(new Date()).replace(" ","_");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
        strDate = sdf.format(c.getTime()).replace(" ","_");;
        gps = new GPSTracker(getActivity());
        if(gps.canGetLocation()){


        /* latt.setText("Lat :" + gps.getLatitude());
            lon.setText("Long :" + gps.getLongitude());*/
        Geocoder geo = new Geocoder(getActivity());
        try {
            List<Address> adresses = geo.getFromLocation(gps.getLatitude(),
                    gps.getLongitude(), 1);

            if (adresses != null && adresses.size() == 1) {
                Address adresse = adresses.get(0);
                //Si le geocoder a trouver une adresse, alors on l'affiche
               site =String.format("%s, %s %s",
                        adresse.getAddressLine(0),
                        adresse.getPostalCode(),
                        adresse.getLocality()).replace(" ","-").replace("é","e").replace("è","e").replace("à","a").replace("ù","u")
                       .replace(" ","-").replace(",","-").trim();
            } else {
                //sinon on affiche un message d'erreur
                site ="L'adresse n'a pu être déterminée";
            }
        } catch (Exception e) {e.printStackTrace();}

    }else{
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings
        gps.showSettingsAlert();
    }

        debut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(),site,Toast.LENGTH_LONG).show();
                String period = "timedeb";
               launchPointage(period);
            }
        });

        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String period = "timend";
                launchPointage(period);
            }
        });
        return setView;
    }


    public void launchPointage(String period) {

        try {

            progressDialog = ProgressDialog.show(getActivity(), null, "Mise à jour du pointage", false, false);
            progressDialog.setCanceledOnTouchOutside(true);

            String url ="http://www.tools.agence-creation-sc.com/pointage.php?userrt="+nomrt+"&period="+period+"&time="+currentDateandTime+"&loc="+site+"";

            URL conn = new URL(url);
            HttpURLConnection ouch = (HttpURLConnection) conn.openConnection();


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(ouch.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            progressDialog.dismiss();
            Toast bread = Toast.makeText(getActivity(), response.toString().replace("<br/>","\n"), Toast.LENGTH_LONG);
            bread.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL,0,0);
            bread.show();
            in.close();



        } catch (Exception e) {
            Log.e("Search", e.getLocalizedMessage());
        }
    }





}
