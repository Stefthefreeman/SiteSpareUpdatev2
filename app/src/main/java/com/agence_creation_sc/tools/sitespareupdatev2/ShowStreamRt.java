package com.agence_creation_sc.tools.sitespareupdatev2;


import android.app.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stéf on 30/01/2016.
 */
public class ShowStreamRt extends Fragment {
    public static ShowStreamRt newInstance() {
        ShowStreamRt fragment = new ShowStreamRt();
        return fragment;
    }

    public ShowStreamRt() {

    }
    private String TAG = "VOLLEYERROR";
    Handler myHandler;
    String text;
    TextView showrtstream ;
    TextView showdate;
    TextView welcome;
    ConnectivityManager cm;
    TextView reseau;
    Animation animFadein;
    Animation rotate;
    Animation bounce;
    Animation move;
    private ImageView nt;
    private int i = 60;
    ListView stream;
    SharedPreferences sharedpreferences;
    String MyPREFERENCES = "MyPrefs" ;
    String Nb = "nbKey";
    String pipe;
    String pluriel;
    String details;
    String idrt;
    SimpleAdapter adapter;
    String site;
    Button sfr;
    Button gwc;
    String nomrt;

    Runnable myRunnable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      final View setView = inflater.inflate(R.layout.showstreamrt, container, false);
        myHandler = new Handler();
        animFadein = AnimationUtils.loadAnimation(getActivity(),
                R.anim.fade_in);
        rotate = AnimationUtils.loadAnimation(getActivity(),
                R.anim.rotate);
        bounce = AnimationUtils.loadAnimation(getActivity(),
                R.anim.bounce);
        move= AnimationUtils.loadAnimation(getActivity(),
                R.anim.shrink);
       showrtstream = (TextView) setView.findViewById(R.id.showstreamrt);
        stream = (ListView) setView.findViewById(R.id.listView2);
        nt = (ImageView) setView.findViewById(R.id.imageView);
        nt.setVisibility(nt.VISIBLE);
        nt.startAnimation(bounce);
        showdate = (TextView) setView.findViewById(R.id.datedujour);
        showdate.startAnimation(bounce);
        reseau = (TextView) setView.findViewById(R.id.textView7);
        Object[] params = new Object[] { new Date() };
        String msg = MessageFormat.format("{0,date,long}",
                params);
        cm = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        showdate.setText(msg);
        welcome = (TextView) setView.findViewById(R.id.welcome);

        sfr = (Button) setView.findViewById(R.id.buttonsfr);
        gwc = (Button) setView.findViewById(R.id.buttongwc);

        sfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callsfr();
            }
        });
        gwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callgwc();
            }
        });


     /*  new Thread(new Runnable() {
            public void run() {
                while (i !=0) {
                    i -= 1;
                    // Update the progress bar and display the
                    //current value in the text view

                    myHandler.post(new Runnable() {
                        public void run() {

                            showrtstream.setText("Affichage RT dans " + i +" s.");
                            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                            llp.setMargins(0, 160, 0, 0); // llp.setMargins(left, top, right, bottom);
                            showrtstream.setLayoutParams(llp);
                            showrtstream.setTextSize(22);

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        //Just to display the progress slowly
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/

        return setView;

    }
  public void launchSearch()  {

        try {
            String url="http://www.tools.agence-creation-sc.com/example/run-rtstream-array-test-json.php?userrt="+nomrt.trim()+"";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,jsonRequestListener ,errorListener);
            getRequestQueue().add(request);
        }
        catch (Exception e) {
            Log.e("Search",e.getLocalizedMessage());
        }
    }


    private Response.Listener<JSONObject> jsonRequestListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

      //  Toast.makeText(getActivity(),"On a une reponse", Toast.LENGTH_LONG).show();
            try {
                JSONArray jsonArray = response.getJSONArray("tickets");
                JSONObject jsonObjectt = jsonArray.getJSONObject(0);

                if (jsonObjectt.getString("nb").equals("0")){
                    welcome.setVisibility(welcome.INVISIBLE);
                    showrtstream.setVisibility(showrtstream.VISIBLE);
                        showrtstream.setText("RAS");
                        showrtstream.setBackgroundColor(Color.TRANSPARENT);
                        showrtstream.startAnimation(move);
                        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                        llp.setMargins(0, 1, 0, 0); // llp.setMargins(left, top, right, bottom);
                        showrtstream.setLayoutParams(llp);
                        showrtstream.setTextSize(38);
//                        showrtstream.setTextColor(getResources().getColor(R.color.ras));
                        showrtstream.setBackgroundResource(R.drawable.siite_spare_update);
                   /* if (sharedpreferences.getInt(Nb,0)>0) {
                        editor.clear();
                        editor.commit();
                    }*/

                    }
               else {
                    ArrayList<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (jsonObject.getString("critic").isEmpty() ||
                        jsonObject.getString("seo").isEmpty()) {
                        pipe = " ";
                    } else {
                        pipe = " | ";
                    }
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("rt", jsonObject.getString("rt"));
                    map.put("nokia", jsonObject.getString("titreticket").replace("&#40;", "(").replace("&#41;", ")"));
                    map.put("critic", jsonObject.getString("critic") + pipe + jsonObject.getString("planif"));
                    map.put("ville", jsonObject.getString("seo") + pipe + jsonObject.getString("ville"));
                    map.put("stock", jsonObject.getString("plutch"));
                    map.put("target", jsonObject.getString("target"));
                    map.put("details", jsonObject.getString("details").replace("*","\n"));

                    items.add(map);
                }
               adapter =new SimpleAdapter(getActivity(), items, R.layout.list_tickets,
                        new String[]{"rt", "nokia", "critic", "target", "ville", "stock", "details"},
                        new int[]{R.id.theid, R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6});

                stream.setAdapter(adapter);
                stream.startAnimation(animFadein);

                stream.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

                        idrt = ((TextView) v.findViewById(R.id.theid)).getText().toString();
                        String plus = ((TextView) v.findViewById(R.id.text6)).getText().toString();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Details ticket " + idrt);
                        alertDialogBuilder.setMessage(plus).setIcon(R.drawable.logontsmall);

                        alertDialogBuilder.setPositiveButton("FERMER", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();


                            }
                        });


                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                    }


                });

                if (stream.getAdapter().getCount() > 1) {
                    pluriel = " Nouveaux Tickets";
                } else {
                    pluriel = " Nouveau Ticket";
                }
                welcome.setVisibility(welcome.VISIBLE);
                    welcome.setText(stream.getAdapter().getCount() + pluriel);
               // showrtstream.setText(stream.getAdapter().getCount() + pluriel);
                welcome.setBackgroundColor(Color.BLACK);
               LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                llp.setMargins(0, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
                showrtstream.setLayoutParams(llp);
                showrtstream.setVisibility(showrtstream.INVISIBLE);





                if(sharedpreferences.getInt(Nb,0) != stream.getAdapter().getCount() && stream.getAdapter().getCount() != 0)
                {//createNotification();
                   }
                 SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt(Nb, stream.getAdapter().getCount());
                editor.commit();
                }
            } catch (JSONException e) {
                Log.e("JSON", e.getLocalizedMessage());
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //Toast.makeText(getActivity(),"On a une erreur" +error.toString(), Toast.LENGTH_LONG).show();
            //showrtstream.setText(error.toString());
            Log.d("REQUEST", error.toString());

            VolleyLog.d(TAG, "Error: " + error.getMessage());
        }
    };
    RequestQueue requestQueue;
    RequestQueue getRequestQueue() {
        if(requestQueue==null)
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }

    private void callsfr() {
        try {
            Intent in = new Intent(Intent.ACTION_CALL);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.setData(Uri.parse("tel:0488691472"));
            startActivity(in);
        } catch (android.content.ActivityNotFoundException ex){}
    }
    private void callgwc() {
        try {
            Intent in = new Intent(Intent.ACTION_CALL);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.setData(Uri.parse("tel:0157324470"));
            startActivity(in);
        } catch (android.content.ActivityNotFoundException ex){}
    }



    @Override
    public void onResume() {
       Log.e("DEBUG", "onResume of ShowstreamRT");


        super.onResume();
        final UserBDD wmess = new UserBDD(getActivity());
        wmess.open();
        final  User perso =wmess.getFirstUser("1");

       /* if (perso != null) {
            welcome.setText("Hello " + perso.getPrenom());

        }*/

        if(isOnline() && perso !=null) {
            nomrt = perso.getUserrt();

            launchSearch();

             myRunnable = new Runnable() {

                @Override
                public void run() {

                    nomrt = perso.getUserrt();

                   launchSearch();




                    myHandler.postDelayed(this, 60000);
                }
            };

            myHandler.postDelayed(myRunnable, 60000);

            // on redemande toute les mn

      wmess.close();
        } else {

            Toast.makeText(getActivity(),"Problème réseau",Toast.LENGTH_LONG).show();
            showrtstream.setText("Réseau indisponible");}



    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of loginFragment");
        super.onPause();
    /*    if (requestQueue  != null) {
            requestQueue.cancelAll(this);
        }*/
    }


    @Override
            public void onStop() {
          super.onStop();
        if (requestQueue  != null) {
            requestQueue.cancelAll(this);
        }
        myHandler.removeCallbacks(myRunnable);
        Log.e("DEBUG", "OnStop of Showstreamrt fragment");
    }
    public boolean isOnline() {


        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {
            boolean wifi = netInfo.getType() == ConnectivityManager.TYPE_WIFI;
            boolean radio = netInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifi) {
            reseau.setText("Connecté en wifi");}
            else if (radio){
                reseau.setText("Connecté en Radio ");
            }
            else if (!wifi && !radio){
                reseau.setText("Aucune connexion disponible");
            }

            return true;
        }

        return false;
    }
    public void createNotification() {
            Context context= getContext();
        // BEGIN_INCLUDE(notificationCompat)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        // END_INCLUDE(notificationCompat)

        // BEGIN_INCLUDE(intent)
        //Create Intent to launch this Activity again if the notification is clicked.
        Intent i = new Intent( getActivity(),MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(getActivity(), 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);
        // END_INCLUDE(intent)

        // BEGIN_INCLUDE(ticker)
        // Sets the ticker text
        builder.setTicker(getResources().getString(R.string.custom_notification));



        // Sets the small icon for the ticker
        builder.setSmallIcon(R.drawable.flag);
        // END_INCLUDE(ticker)

        // BEGIN_INCLUDE(buildNotification)
        // Cancel the notification when clicked
        builder.setAutoCancel(true);

        // Build the notification
        Notification notification = builder.build();
        // END_INCLUDE(buildNotification)
        // notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notifysnd);
        notification.defaults =Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
        // BEGIN_INCLUDE(customLayout)
        // Inflate the notification layout as RemoteViews
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
        // showrtstream = (TextView) findViewById(R.id.streamrt);
        // Set text on a TextView in the RemoteViews programmatically.
        final String time = DateFormat.getTimeInstance().format(new Date()).toString();
        text = getResources().getString(R.string.collapsed, time);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) { contentView.setTextColor(R.id.textView,0xffa3b8fe);
            contentView.setTextViewText(R.id.textView, text);}
        else {
            contentView.setTextViewText(R.id.textView, text);
        }
        /* Workaround: Need to set the content view here directly on the notification.
         * NotificationCompatBuilder contains a bug that prevents this from working on platform
         * versions HoneyComb.
         * See https://code.google.com/p/android/issues/detail?id=30495
         */
        notification.contentView = contentView;
        notification.sound = Uri.parse("file:///sdcard/Notifications/Popipopipopi.mp3");
        // Add a big content view to the notification if supported.
        // Support for expanded notifications was added in API level 16.
        // (The normal contentView is shown when the notification is collapsed, when expanded the
        // big content view set here is displayed.)
       /* if (Build.VERSION.SDK_INT >= 23) {
            // Inflate and set the layout for the expanded notification view
            RemoteViews expandedView =
                    new RemoteViews(getActivity(), R.layout.notification_expanded);
            notification.bigContentView = expandedView;
        }*/
        // END_INCLUDE(customLayout)

        // START_INCLUDE(notify)
        // Use the NotificationManager to show the notification
    //    NotificationManager nm = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
      //  nm.notify(0, notification);
        // END_INCLUDE(notify)

    }
}
