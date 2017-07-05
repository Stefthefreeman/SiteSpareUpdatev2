package com.agence_creation_sc.tools.sitespareupdatev2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stéf on 26/04/2016.
 */
public class MyticketsList extends Fragment {

    public static MyticketsList newInstance() {
        MyticketsList fragment = new MyticketsList();
        return fragment;
    }

    public MyticketsList() {

    }

    ListView stream;
    String nomrt;
    ProgressDialog progressDialog;
    String pipe;
    String details;
    String idrt;
    String idnokia;
    String critic;
    String planif;
    String ville;
    String stock;
    String target;
    String site;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View setView = inflater.inflate(R.layout.list_histo, container, false);
        stream = (ListView) setView.findViewById(R.id.list_histo);
         UserBDD wmess = new UserBDD(getActivity());
        wmess.open();
         User perso = wmess.getFirstUser("1");
         nomrt = perso.getUserrt().trim();



         return setView;
    }
    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();
        launchSearch();

    }

   /* @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of loginFragment");
        super.onPause();
        if (requestQueue  != null) {
            requestQueue.cancelAll(this);
        }
    }*/

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue  != null) {
            requestQueue.cancelAll(this);
        }
        Log.e("DEBUG", "OnStop of MyticketListt");
    }
    private void launchSearch() {

        try {

             progressDialog = ProgressDialog.show(getActivity(), null, "Chargement des tickets", false, false);

            String url ="http://www.tools.agence-creation-sc.com/example/mytickets_list_test.php?userrt="+nomrt+"";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, jsonRequestListener, errorListener);
            getRequestQueue().add(request);

            progressDialog.setCanceledOnTouchOutside(true);

        } catch (Exception e) {
            Log.e("Search", e.getLocalizedMessage());
        }
    }

    private Response.Listener<JSONObject> jsonRequestListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            // Toast.makeText(getActivity(),"On a une reponse"+response, Toast.LENGTH_LONG).show();


            try {
                JSONArray jsonArray = response.getJSONArray("mestickets");
                List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("rt", jsonObject.getString("RT"));
                    map.put("nokia", jsonObject.getString("titre").replace("&#40;", "(").replace("&#41;", ")"));
                    map.put("critic", jsonObject.getString("critic"));
                    map.put("ville", jsonObject.getString("site"));
                  //  map.put("stock", jsonObject.getString("plutch"));
                    map.put("target", jsonObject.getString("target"));
                    map.put("details", jsonObject.getString("details").replace("*","\n"));

                    items.add(map);
                }
                progressDialog.dismiss();
                final SimpleAdapter adapter = new SimpleAdapter(getActivity(), items, R.layout.list_tickets,
                        new String[]{"rt", "nokia", "critic", "target", "ville", "details"},
                        new int[]{R.id.theid, R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text6});
                stream.setAdapter(adapter);

                stream.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {


                        String rt = ((TextView) v.findViewById(R.id.theid)).getText().toString();
                        String plus = ((TextView) v.findViewById(R.id.text6)).getText().toString();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Details ticket " + rt);
                        alertDialogBuilder.setMessage(plus);

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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
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
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }
}
