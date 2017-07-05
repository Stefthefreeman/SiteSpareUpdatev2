package com.agence_creation_sc.tools.sitespareupdatev2;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchableActivity2 extends AppCompatActivity {
    TextView nombre;
    ProgressDialog dialog;
    String user;
    String query;
    JsonObjectRequest request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_isreal);

        if (getIntent() != null) {
            handleIntent(getIntent());


        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }


    public  void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);

             nombre = (TextView) findViewById(R.id.textView4);
            launch(query);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_parts, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView =(SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        return true;
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(SearchableActivity2.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    public void launch(String query) {
        UserBDD wmess = new UserBDD(SearchableActivity2.this);
        wmess.open();
        User perso =wmess.getFirstUser("1");
        user = perso.getUserrt();


        try {
            Toast.makeText(SearchableActivity2.this,query,Toast.LENGTH_LONG).show();
            String url = "http://www.tools.agence-creation-sc.com/example/searchforparts.php?user="+user+"&pn=" + query.trim() + "";
            request = new JsonObjectRequest(Request.Method.GET, url, null, jsonRequestListener, errorListener);
            getRequestQueue().add(request);

        } catch (Exception e) {
            Log.e("Search", e.getLocalizedMessage());
        }
        wmess.close();
    }


    private Response.Listener<JSONObject> jsonRequestListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
           Toast.makeText(SearchableActivity2.this,"On a une reponse", Toast.LENGTH_LONG).show();
            try {
                JSONArray jsonArray = response.getJSONArray("spare");



                List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Map<String, Object> map = new HashMap<String, Object>();

                    map.put("pn", jsonObject.getString("pn"));
                    map.put("quant",jsonObject.getString("quant"));
                    map.put("sn",jsonObject.getString("sn")+ " | " + jsonObject.getString("fournisseur"));
                    map.put("des",jsonObject.getString("des"));

                    items.add(map);

                }
                SimpleAdapter adapter = new SimpleAdapter(SearchableActivity2.this, items, R.layout.list_item_parts,
                        new String[]{"quant", "pn", "sn","des"},
                        new int[]{R.id.theid, R.id.text1, R.id.text2, R.id.text3});
                final ListView lv = (ListView) findViewById(R.id.listisreal);
                lv.setAdapter(adapter);
                nombre.setText(lv.getAdapter().getCount() + " Cartes " + query);
                nombre.setBackgroundColor(Color.LTGRAY);
                nombre.setTextColor(Color.BLACK);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
            requestQueue = Volley.newRequestQueue(SearchableActivity2.this);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        return requestQueue;
    }
}
