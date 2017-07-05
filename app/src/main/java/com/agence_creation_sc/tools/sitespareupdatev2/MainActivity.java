package com.agence_creation_sc.tools.sitespareupdatev2;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        String prenom;
        String nom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Ouvrir Boite mail", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
                i.putExtra(Intent.EXTRA_SUBJECT, "");
                i.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    startActivity(Intent.createChooser(i, "Envoyer un mail"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        UserBDD check = new UserBDD(MainActivity.this);
        check.open();

        if(check.getifexist()==false) {
            Toast signup =Toast.makeText(getApplicationContext(),
                    "Vous devez vous enregistrer pour utiliser l'application \n Redirection...", Toast.LENGTH_LONG);
            signup.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            signup.show();
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);}else{
            User user = check.getFirstUser("1");
            prenom = user.getPrenom();
            nom = user.getNom();

             }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(Gravity.LEFT);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v = navigationView.getHeaderView(0);
        TextView name = (TextView) v.findViewById(R.id.textView);
        name.setText(prenom + " " + nom);
        check.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            new AlertDialog.Builder(this)
                    .setTitle("Quitter Site Spare Update?")
                    .setMessage("Voulez vous fermer l'application?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            MainActivity.super.onBackPressed();
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            // notificationManager.cancel(ID_NOTIFICATION);
                        }
                    }).create().show();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
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


            default:
                return super.onOptionsItemSelected(item);
        }
            return true;
        }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();


        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, ShowStreamRt.newInstance())
                    .commit();
        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, Choice.newInstance())
                    .commit();

        } else if (id == R.id.nav_slideshow) {
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, HistoList.newInstance())
                    .commit();

        } else if (id == R.id.nav_manage) {
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, ShowRt.newInstance())
                    .commit();

        } else if (id == R.id.nav_geoloc) {
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, Geolocalisation.newInstance())
                    .commit();

        } else if (id == R.id.nav_mytickets) {
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, MyticketsList.newInstance())
                    .commit();

        }else if (id == R.id.nav_details) {
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, ShowMyTickets.newInstance())
                    .commit();

        }else if (id == R.id.nav_astreintes) {
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, PlannigAstreintes.newInstance())
                    .commit();

        }else if (id == R.id.nav_pointage) {
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, Pointage.newInstance())
                    .commit();

        }else if (id == R.id.nav_tickets_flm) {
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, Stream_Aix.newInstance())
                    .commit();

        }

        // Close the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

}
