package com.agence_creation_sc.tools.sitespareupdatev2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoList extends Fragment {
    public static HistoList newInstance() {
        HistoList fragment = new HistoList();
        return fragment;
    }

    public HistoList() {

    }
    private String pseudo;
    private String mail;
    private String snforsend;
    private String[] pnhssnhs;
    private String pnhsforsend;
    private String snhsforsend;
    private String[] rtticket;
    private String ticket;
    private String pinkid;
    String userid;
    String cartehs;
    String infosticket;
    private SharedPreference sharedPreference;

    //Activity context = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View setView = inflater.inflate(R.layout.list_histo, container, false);

        final UserBDD wmess = new UserBDD(getActivity());
        wmess.open();
        final  User perso =wmess.getFirstUser("1");
        userid = perso.getUserrt();
        wmess.close();
        sharedPreference = new SharedPreference();
       // pseudo = sharedPreference.getValue(context);
      //  setTitle("Historique : " + pseudo);
        ContactsBDD thehisto = new ContactsBDD(getContext());
        thehisto.open();

        SimpleAdapter adapter = new SimpleAdapter(getContext(), thehisto.getAllPersonnes(), R.layout.simple_list_item_3,
                new String[] {"id", "snok", "pnhs","time"},
                new int[] {R.id.theid,R.id.text1, R.id.text2,R.id.text3});

        ListView lv = (ListView)setView.findViewById(R.id.list_histo);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                pinkid=((TextView) view.findViewById(R.id.theid)).getText().toString();
                infosticket = ((TextView) view.findViewById(R.id.text3)).getText().toString();
                snforsend = ((TextView) view.findViewById(R.id.text1)).getText().toString();
                cartehs = ((TextView) view.findViewById(R.id.text2)).getText().toString();

                String[] verif = infosticket.split("-");
                ticket= verif[0];
                if(cartehs.equals("|"))
                {   pnhsforsend = "NA";
                    snhsforsend ="NA";}
                else { String[] tiret = cartehs.split(" ");
                         pnhsforsend = tiret[0];
                         snhsforsend = tiret[1];}

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle("Envoyer les données du ticket " +ticket +":");
                builder1.setMessage("SN carte OK :" + snforsend + "\nPN CARTE HS :" + pnhsforsend + "\n"  +
                        "SN CARTE HS :" + snhsforsend);
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {

                                    ContactsBDD insertdatas = new ContactsBDD(getContext());
        String myurl = "http://www.tools.agence-creation-sc.com/qrcodes.php?SNOK="+snforsend.trim()+
                "&PNHS="+pnhsforsend.trim()+"&SNHS="+snhsforsend.trim()+"&TickRt="
        +ticket.trim()+"&userid="+userid.trim()+"";
         URL conn = new URL(myurl);
         HttpURLConnection ouch = (HttpURLConnection) conn.openConnection();
         BufferedReader in = new BufferedReader(
         new InputStreamReader(ouch.getInputStream()));
         String inputLine;
         StringBuffer response = new StringBuffer();

                                    while ((inputLine = in.readLine()) != null) {
                                        response.append(inputLine);
                                    }
                                    Toast bread = Toast.makeText(getContext(), response, Toast.LENGTH_LONG);
                                    bread.show();
                                    in.close();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        });

                builder1.setNeutralButton(
                        "Supprimer Mvt",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ContactsBDD insertdatas = new ContactsBDD(getContext());
                                insertdatas.open();

                                insertdatas.removemouvement(pinkid);
                                Toast.makeText(getContext(), "Mouvement " + pinkid + " Ticket: "+ticket+ " supprimé", Toast.LENGTH_LONG).show();
                                insertdatas.close();
                                update();
                            }
                        });
                builder1.setNegativeButton(
                        "Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
        thehisto.close();

return setView;
    }
    private List<Map<String, ?>> createSensorsList()
    {

        SensorManager sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        try {

            URL url = new URL("http://www.tools.travelonline.fr/stockt.php?username="+pseudo+"");
            HttpURLConnection ouch = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(ouch.getInputStream()));
            String listitem=null;
            String quantite=null;
            String timestamps=null;
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                Map<String, Object> map = new HashMap<String, Object>();
                String[] oneData = line.split(",");
                listitem = oneData[5];
                quantite = oneData[7];
                timestamps = oneData[13];
                map.put("sn", listitem);
                map.put("id", quantite);
                map.put("time",timestamps);
                items.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

 public void update() {

     ContactsBDD thehisto = new ContactsBDD(getContext());
     thehisto.open();

     SimpleAdapter adapter = new SimpleAdapter(getContext(), thehisto.getAllPersonnes(), R.layout.simple_list_item_3,
             new String[] {"id","snok", "pnhs","time"},
             new int[] {R.id.theid,R.id.text1, R.id.text2,R.id.text3});

     ListView lv = (ListView)getActivity().findViewById(R.id.list_histo);
     lv.setAdapter(adapter);
     thehisto.close();
 }



}
