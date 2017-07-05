package com.agence_creation_sc.tools.sitespareupdatev2;

import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchableActivity extends AppCompatActivity {
    private String address;
    String query;
    List items;
    TextView nombre;
    Button reset;
    String adresse;
    String latlg[];
    String lalat;
    String lalong;
    String infos;
    String infosplus;
    String myg2r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_isreal);
        reset = (Button) findViewById(R.id.reset);
        if (getIntent() != null) {
            handleIntent(getIntent());


        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }


    public void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {


            query = intent.getStringExtra(SearchManager.QUERY);
            //Toast.makeText(getApplicationContext(),query,Toast.LENGTH_LONG).show();
           final SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
             nombre = (TextView) findViewById(R.id.textView4);
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    suggestions.clearHistory();
                }
            });
            launch(query);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        return true;
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(SearchableActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    public void launch(String query) {

        try {
            String url = "http://www.tools.agence-creation-sc.com/example/searchinfosite.php?SITE=" + query.trim() + "&G2R=" + query.trim() + "";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, jsonRequestListener, errorListener);
            getRequestQueue().add(request);
        } catch (Exception e) {
            Log.e("Search", e.getLocalizedMessage());
        }
    }


        private Response.Listener<JSONObject> jsonRequestListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
              //  Toast.makeText(SearchableActivity.this,response, Toast.LENGTH_LONG).show();
                try {
                    JSONArray jsonArray = response.getJSONArray("infosites");
                    JSONObject jsonObjectt = jsonArray.getJSONObject(0);


                    List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Map<String, Object> map = new HashMap<String, Object>();

                        map.put("site", jsonObject.getString("site") + " - " +jsonObject.getString("G2R"));
                        map.put("g2r", " Lat :" +jsonObject.getString("lat")+ " Long :" +jsonObject.getString("long"));
                        map.put("adresse", jsonObject.getString("adresse"));
                        map.put("infos",jsonObject.getString("infos"));
                        items.add(map);

                    }
                    SimpleAdapter adapter = new SimpleAdapter(SearchableActivity.this, items, R.layout.list_irem_sites,
                            new String[]{"site", "g2r", "adresse","infos"},
                            new int[]{R.id.text1, R.id.text2, R.id.text3,R.id.infossites});
                    final ListView lv = (ListView) findViewById(R.id.listisreal);
                    lv.setAdapter(adapter);
                    nombre.setText(lv.getAdapter().getCount() + " Sites à " + query);
                    nombre.setBackgroundColor(Color.LTGRAY);
                    nombre.setTextColor(Color.BLACK);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                          final  String site = ((TextView) view.findViewById(R.id.text1)).getText().toString();
                            adresse = ((TextView) view.findViewById(R.id.text3)).getText().toString();
                            latlg = ((TextView) view.findViewById(R.id.text2)).getText().toString().split(":");
                            lalat= latlg[1].replace("Long","").trim();
                            lalong = latlg[2].replace("Long","").trim();

                            infos = ((TextView) view.findViewById(R.id.infossites)).getText().toString();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SearchableActivity.this);
                            alertDialogBuilder.setTitle("Infos site  " + site);
                            alertDialogBuilder.setMessage(infos).setIcon(R.drawable.logontsmall);

                            alertDialogBuilder.setPositiveButton("FERMER", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();


                                }
                            });
                            alertDialogBuilder.setNeutralButton("INFOS+",new DialogInterface.OnClickListener(){
                                @Override
                                public  void onClick(DialogInterface dialog,int which){
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchableActivity.this);
                                    alertDialog.setTitle("Ajouter Infos");


                                    final EditText input = new EditText(SearchableActivity.this);
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT);
                                    input.setLayoutParams(lp);
                                    alertDialog.setView(input);
                                    alertDialog.setIcon(R.drawable.logont);
                                    input.setText(infos);
                                    alertDialog.setPositiveButton("Envoyer",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialoge, int which) {

                                                    infosplus= input.getText().toString().replace("é","e").replace("è","e").replace("à","a").replace("ù","u")
                                                            .replace(" ","-").replace(",","-").trim();
                                                    String [] g2rforsend = site.split("-");
                                                    myg2r = g2rforsend[1];
// Instantiate the RequestQueue.
                                                    RequestQueue queue = Volley.newRequestQueue(SearchableActivity.this);
                                                    String url ="http://www.tools.agence-creation-sc.com/geoloc.php?&G2R="+myg2r.trim()+"&infosplus="+infosplus+"";

// Request a string response from the provided URL.
                                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    // Display the first 500 characters of the response string.

                                                                    Toast.makeText(SearchableActivity.this,response,Toast.LENGTH_LONG).show();
                                                                }
                                                            }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(SearchableActivity.this,"Erreur",Toast.LENGTH_LONG).show();
                                                        }
                                                    });
// Add the request to the RequestQueue.
                                                    queue.add(stringRequest);


                                                }
                                            });


                                    alertDialog.setNegativeButton("Annuler",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialoge, int which) {
                                                    dialoge.cancel();
                                                }
                                            });

                                    alertDialog.show();
                                }

                            });

                            alertDialogBuilder.setNegativeButton("GPS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //   Toast.makeText(getApplicationContext(),unduitem,Toast.LENGTH_LONG).show();
                                    LayoutInflater layoutInflator = getLayoutInflater();
                                    View layout = layoutInflator.inflate(R.layout.custom_toast,
                                            (ViewGroup) findViewById(R.id.toast_layout_root));

                                    ImageView iv = (ImageView) layout.findViewById(R.id.toast_iv);
                                    TextView tv = (TextView) layout.findViewById(R.id.toast_tv);
                                    iv.setBackgroundResource(R.drawable.bub_maps);

                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setView(layout);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();

                                    address = "google.navigation:q="+lalat+","+lalong;
                                    tv.setText("Navigation vers " + adresse);
// String query = URLEncoder.encode(address, "utf-8");
                                    Uri location = Uri.parse(address);
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                                    mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                                    // Verify it resolves
                                    PackageManager packageManager = getPackageManager();
                                    List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
                                    boolean isIntentSafe = activities.size() > 0;

                                    // Start an activity if it's safe
                                    if (isIntentSafe) {
                                        startActivity(mapIntent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please install google maps app", Toast.LENGTH_LONG).show();


                                    }


                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();



                        }

                    });

                } catch (JSONException e) {
                    Log.e("JSON", e.getLocalizedMessage());
                }

            }

        };

            public Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getActivity(),"On a une erreur" +error.toString(), Toast.LENGTH_LONG).show();
                    //showrtstream.setText(error.toString());
                    Log.d("REQUEST", error.toString());

                }
            };

            RequestQueue requestQueue;
            RequestQueue getRequestQueue() {
                if (requestQueue == null)
                    requestQueue = Volley.newRequestQueue(SearchableActivity.this);
                return requestQueue;
            }




    }




