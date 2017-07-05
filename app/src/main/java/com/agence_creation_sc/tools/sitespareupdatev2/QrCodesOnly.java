package com.agence_creation_sc.tools.sitespareupdatev2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Stéf on 03/11/2015.
 */
public class QrCodesOnly extends AppCompatActivity {

    double latitude; // latitude
    double longitude;
    Button qrcarteok;
    Button qrcartehs;
    TextView qrok;
    TextView qrhs;
    TextView numrt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcodesonly);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.logontsmall);
        showInputDialogRTnum();
        qrok = (TextView) findViewById(R.id.textViewqrok);
        qrhs = (TextView) findViewById(R.id.textviewqrhs);
        numrt = (TextView) findViewById(R.id.textViewRTNUM);
        qrcarteok = (Button) findViewById(R.id.buttonqrok);
        qrcartehs = (Button) findViewById(R.id.buttonqrhs);
        final  String numberrt = numrt.getText().toString();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final String EXTRA_NUMRT = "lenumero";
        Intent barok = getIntent();
        if (barok != null) {
            setTitle(barok.getStringExtra(EXTRA_NUMRT));


        }
        qrcarteok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.setPackage("com.google.zxing.client.android");
                intent.putExtra("SCAN_FORMATS", "QR_CODE");
                startActivityForResult(intent, 0);

            }
        });

       qrcartehs.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent("com.google.zxing.client.android.SCAN");
               intent.setPackage("com.google.zxing.client.android");
               intent.putExtra("SCAN_FORMATS", "QR_CODE");
               startActivityForResult(intent, 1);
           }
       });



        Button senddatas = (Button) findViewById(R.id.buttonMAJ);

        senddatas.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                if (qrok.getText().toString().equals("")) {
                    Toast bread = Toast.makeText(getApplicationContext(), "Les champs doivent être tous remplis", Toast.LENGTH_LONG);
                    bread.show();
                } else {
                    try {

                        ContactsBDD insertdatas = new ContactsBDD(QrCodesOnly.this);
                        insertdatas.open();
                        String rtnumber[] = getTitle().toString().split(":");
                        String ticket = rtnumber[1];
                        double latitude; // latitude
                        double longitude;

                            longitude = 0.0;
                            latitude = 0.0;

                        String snok[]= qrok.getText().toString().trim().replace(" ", "").split(";");
                        String serialok = snok[1];

                        String snhs[]= qrhs.getText().toString().trim().replace(" ", "").split(";");
                        String serialhs= snhs[1].toString();
                        String pnhs= snhs[0].toString();

                        Contact contact = new Contact(serialok,pnhs, serialhs, qrok.getText().toString(), qrhs.getText().toString(), longitude, latitude,
                                "", ticket);
                        String verifemptysn = insertdatas.getFirstboard(serialok.toString());
                        if (verifemptysn.equals("test")) {
                            insertdatas.insertContact(contact);
                            Toast.makeText(getApplicationContext(), contact.toString(), Toast.LENGTH_LONG).show();
                            insertdatas.close();
                        }


                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }



        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        final TextView scanpartsok = (TextView) findViewById(R.id.textViewqrok);
        final TextView scanpnhs = (TextView) findViewById(R.id.textviewqrhs);


        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {

                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                scanpartsok.setText(contents);


            } else if (resultCode == RESULT_CANCELED) {


            }
        }
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                scanpnhs.setText(contents);
                Toast.makeText(this, format, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {


            }
        }


    }
    public void showInputDialogRTnum() {


        // get inputdialog.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(QrCodesOnly.this);
        View promptView = layoutInflater.inflate(R.layout.inputrtnumber, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QrCodesOnly.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle("Indiquez N° Ticket RT");
        //alertDialogBuilder.setIcon(R.drawable.logontsmall);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);


        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String numero = editText.getText().toString();
                        setTitle(" Ticket:"  + numero);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    public void showInputDialog() {

        final TextView numrt = (TextView) findViewById(R.id.textViewRTNUM);
        final  LocationManager locationManager;
        final  String PROVIDER = LocationManager.GPS_PROVIDER;
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        LayoutInflater layoutInflater = LayoutInflater.from(QrCodesOnly.this);
        View promptView = layoutInflater.inflate(R.layout.inputdialogbarman, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QrCodesOnly.this);
        alertDialogBuilder.setView(promptView);

        final   EditText snok= (EditText) promptView.findViewById(R.id.edittextsnok);
        final   EditText pnhs = (EditText) promptView.findViewById(R.id.edittextpnhs);
        final   EditText snhs = (EditText) promptView.findViewById(R.id.edittextsnhs);


        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        final   String setsnok = snok.getText().toString();
                        final    String setpnhs = pnhs.getText().toString();
                        final   String setsnhs = snhs.getText().toString();

                        if (setsnok.equals("")) {
                            Toast bread = Toast.makeText(getApplicationContext(), "Les champs doivent être tous remplis", Toast.LENGTH_LONG);
                            bread.show();
                        } else {

                            try {


                                ContactsBDD insertdatas = new ContactsBDD(QrCodesOnly.this);
                                String rtnumber[]=getTitle().toString().split(":");
                                String ticket= rtnumber[1];
                                insertdatas.open();


                                Contact contact = new Contact(setsnok, setpnhs, setsnhs, "na", "na", longitude, latitude,
                                        "",ticket );
                                insertdatas.insertContact(contact);

                                //   insertdatas.delete();
                                Toast.makeText(getApplicationContext(), contact.toString(), Toast.LENGTH_LONG).show();
                                insertdatas.close();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        dialog.dismiss();


                        //  contactBdd.close();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_baronly, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.search_bar:
                Intent change = new Intent(this, SearchableActivity.class);
                this.startActivity(change);
                break;
            case R.id.action_settings:
                Intent param = new Intent(this, Parametres.class);
                this.startActivity(param);
                break;

            case R.id.search_bar2:
                Intent searchparts = new Intent(this, SearchableActivity2.class);
                this.startActivity(searchparts);
                break;
            case R.id.action_man:
                showInputDialog();
                break;


            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
