package com.agence_creation_sc.tools.sitespareupdatev2;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class Geolocalisation extends Fragment {
    public static Geolocalisation newInstance() {
        Geolocalisation fragment = new Geolocalisation();
        return fragment;
    }

    public Geolocalisation() {

    }


    GPSTracker gps;
    EditText site;
    EditText G2R;
    TextView latt;
    TextView lon;
    TextView coord;
    EditText infos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View setView = inflater.inflate(R.layout.httprequest, container, false);

         site = (EditText) setView.findViewById(R.id.site);
         G2R = (EditText) setView.findViewById(R.id.G2R);
         latt = (TextView) setView.findViewById(R.id.latt);
         lon = (TextView) setView.findViewById(R.id.lon);
         coord = (TextView)setView.findViewById(R.id.adresse);
        infos =(EditText)  setView.findViewById(R.id.infos);
        FloatingActionButton myFab = (FloatingActionButton)  setView.findViewById(R.id.myFAB);

        gps = new GPSTracker(getActivity());

        // check if GPS enabled
        if(gps.canGetLocation()){


            latt.setText("Lat :" + gps.getLatitude());
            lon.setText("Long :" + gps.getLongitude());
            // \n is for new line
           Toast.makeText(getActivity(), "Your Location is - \nLat: " + gps.getLatitude() + "\nLong: " + gps.getLongitude(), Toast.LENGTH_LONG).show();
            Geocoder geo = new Geocoder(getActivity());
            try {
                List<Address> adresses = geo.getFromLocation(gps.getLatitude(),
                        gps.getLongitude(), 1);

                if (adresses != null && adresses.size() == 1) {
                    Address adresse = adresses.get(0);
                    //Si le geocoder a trouver une adresse, alors on l'affiche
                    coord.setText(String.format("%s, %s %s",
                            adresse.getAddressLine(0),
                            adresse.getPostalCode(),
                            adresse.getLocality()));
                } else {
                    //sinon on affiche un message d'erreur
                    coord.setText("L'adresse n'a pu être déterminée");
                }
            } catch (Exception e) {e.printStackTrace();}

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }



        myFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(G2R.getText().toString().isEmpty()) {Toast toast= Toast.makeText(getContext(),"Le G2R doit être renseigné!", Toast.LENGTH_SHORT);
                                                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                                        toast.show();
                }
                else {

                try {

                        String myurl = "http://www.tools.agence-creation-sc.com/geoloc.php?nomsite="+site.getText().toString().replace(" ","-").trim()+"&G2R="+G2R.getText().toString().trim()+"&latt="+gps.getLatitude()+"&long="+gps.getLongitude()+"&adresse="+coord.getText().toString()
                                .replace("é","e").replace("è","e").replace("à","a").replace("ù","u")
                                .replace(" ","-").replace(",","-").trim()+"&infos="+infos.getText().toString().replace("é","e").replace("è","e").replace("à","a").replace("ù","u")
                                .replace(" ","-").replace(",","-").trim();
                      Toast.makeText(getActivity(), myurl, Toast.LENGTH_LONG).show();
                        URL conn = new URL(myurl);
                        HttpURLConnection ouch = (HttpURLConnection) conn.openConnection();


                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(ouch.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        Toast bread = Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT);
                        bread.show();
                        in.close();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }});
     return setView;
    }

    }





