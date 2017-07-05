package com.agence_creation_sc.tools.sitespareupdatev2;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
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

import java.util.List;

public class Destock extends AppCompatActivity {
    private String pseudo;
    private SharedPreference sharedPreference;
    Activity context = this;

    TextView scanpartsok;
    TextView scanpnhsresult;
    TextView scansnhsresult;
    TextView numrt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destock);
        showInputDialogRTnum();
        scanpartsok = (TextView) findViewById(R.id.scansnoktext);
        scanpnhsresult = (TextView) findViewById(R.id.pnhs);
        scansnhsresult = (TextView) findViewById(R.id.scanresultsnhs);

    /*    numrt = (TextView) findViewById(R.id.textViewRTNUM);
        final  String numberrt = numrt.getText().toString();*/

       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.logontsmall);
        sharedPreference = new SharedPreference();
        pseudo = sharedPreference.getValue(context);
        final String EXTRA_NUMRT = "lenumero";
        Intent barok = getIntent();
        if (barok != null) {
            setTitle(barok.getStringExtra(EXTRA_NUMRT));


        }
        Button scansnok = (Button) findViewById(R.id.scansnok);
        Button scanpnhs = (Button) findViewById(R.id.buttonscanpnhs);
        Button scansnhs = (Button) findViewById(R.id.buttonscansnhs);

        scansnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.setPackage("com.google.zxing.client.android");
                intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A");
                startActivityForResult(intent, 1);


            }
        });
        scanpnhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.setPackage("com.google.zxing.client.android");
                intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A");
                startActivityForResult(intent, 2);
            }
        });
        scansnhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.setPackage("com.google.zxing.client.android");
                intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A");
                startActivityForResult(intent, 3);
            }
        });


        Button senddatas = (Button) findViewById(R.id.buttonMAJ);

        senddatas.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (scanpartsok.getText().toString().equals("")) {
                    Toast bread = Toast.makeText(getApplicationContext(), "Les champs doivent être tous remplis", Toast.LENGTH_LONG);
                    bread.show();}
                else {

                    try {

                        ContactsBDD insertdatas = new ContactsBDD(Destock.this);
                        insertdatas.open();
                        String rtnumber[]=getTitle().toString().split(":");
                        String ticket= rtnumber[1];
                        double latitude; // latitude
                        double longitude;

                            longitude = 0.0;
                            latitude = 0.0;

                        Contact contact = new Contact(scanpartsok.getText().toString(), scanpnhsresult.getText().toString(), scansnhsresult.getText().toString(), "na", "na",longitude, latitude,
                                "",ticket );
                        String verifemptysn = insertdatas.getFirstboard(scanpartsok.getText().toString());
                        if (verifemptysn.equals("test"))
                        {insertdatas.insertContact(contact);
                            Toast.makeText(getApplicationContext(), contact.toString(), Toast.LENGTH_LONG).show();
                        insertdatas.close();

                        }
                        else {Toast.makeText(context,"Ce Mouvement est déjà fait",Toast.LENGTH_SHORT).show();}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {



        switch(requestCode){
            case 1:     if (resultCode == RESULT_OK) {

                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                scanpartsok.setText(contents.trim().replace(" ",""));

            }
                break;
            case 2:   if (resultCode == RESULT_OK) {

                String contents1 = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                scanpnhsresult.setText(contents1.trim().replace(" ",""));

            }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    String contents2 = intent.getStringExtra("SCAN_RESULT");
                    scansnhsresult.setText(contents2.trim().replace(" ",""));
                }

                break;
        }


    }
    public void verifypackage(Intent intent) {
        // Verify it resolves
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {
          //  startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Vous devez installer BarcodeScanner!", Toast.LENGTH_LONG).show();


        }
    }
    public void showInputDialogRTnum() {


        // get inputdialog.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Destock.this);
        View promptView = layoutInflater.inflate(R.layout.inputrtnumber, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Destock.this);
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

        LayoutInflater layoutInflater = LayoutInflater.from(Destock.this);
        View promptView = layoutInflater.inflate(R.layout.inputdialogbarman, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Destock.this);
        alertDialogBuilder.setView(promptView);

        final   EditText snok= (EditText) promptView.findViewById(R.id.edittextsnok);
        final   EditText pnhs = (EditText) promptView.findViewById(R.id.edittextpnhs);
        final   EditText snhs = (EditText) promptView.findViewById(R.id.edittextsnhs);

        if(scanpartsok.getText().toString()!="")
        {String transfertsn = scanpartsok.getText().toString();
        snok.setText(transfertsn,TextView.BufferType.EDITABLE);}
        if(scanpnhsresult.getText().toString()!="")
        {String transfertpnhs = scanpnhsresult.getText().toString();
            pnhs.setText(transfertpnhs,TextView.BufferType.EDITABLE);}
        if(scansnhsresult.getText().toString()!="")
        {String transfertsnhs = scansnhsresult.getText().toString();
            snhs.setText(transfertsnhs,TextView.BufferType.EDITABLE);}
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                     final   String setsnok = snok.getText().toString().trim();
                    final    String setpnhs = pnhs.getText().toString().trim();
                     final   String setsnhs = snhs.getText().toString().trim();

                        if (setsnok.equals("")) {
                            Toast bread = Toast.makeText(getApplicationContext(), "Les champs doivent être tous remplis", Toast.LENGTH_LONG);
                            bread.show();
                        } else {

                            try {
                                double latitude; // latitude
                                double longitude;

                                ContactsBDD insertdatas = new ContactsBDD(Destock.this);
                                String rtnumber[]=getTitle().toString().split(":");
                                String ticket= rtnumber[1];
                                insertdatas.open();

                                    longitude = 0.0;
                                    latitude = 0.0;

                                Contact contact = new Contact(setsnok, setpnhs, setsnhs, "na", "na", longitude, latitude,
                                        "",ticket );
                                insertdatas.insertContact(contact);
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


            case R.id.action_man:
               showInputDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
    }

