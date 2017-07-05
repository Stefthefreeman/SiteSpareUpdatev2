package com.agence_creation_sc.tools.sitespareupdatev2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Parametres extends AppCompatActivity {
    // UI References
    private Button update;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parametres);
       final   EditText nom;
       final  EditText prenom;
       final  EditText email;
       final  EditText nomrt;
         final EditText passrt;
         Button delete;
         Button update;

         nom = (EditText) findViewById(R.id.nomup);
        prenom = (EditText) findViewById(R.id.prenomup);
        email = (EditText) findViewById(R.id.input_emailup);
        nomrt = (EditText) findViewById(R.id.etxt_userrtup);
        passrt = (EditText) findViewById(R.id.etxt_pswdrtup);
        update = (Button) findViewById(R.id.btn_signupup);


  final  UserBDD modify = new UserBDD(this);
        modify.open();
        final User userfromBDD = modify.getFirstUser("1");
        if (userfromBDD != null) {

            nom.setText(userfromBDD.getNom().toString());
            prenom.setText(userfromBDD.getPrenom().toString());
            email.setText(userfromBDD.getEmail().toString());
            nomrt.setText(userfromBDD.getUserrt().toString());
            passrt.setText(userfromBDD.getPswdrt().toString());

        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Parametres.this,
                        R.style.NewDialog);
                progressDialog.setIndeterminate(false);
                progressDialog.setMessage("Modifications en cours...");
                progressDialog.show();
                modify.open();
                int colid= userfromBDD.getId();
                User up= new User(nom.getText().toString().trim(),prenom.getText().toString().trim(),email.getText().toString().trim(),"",
                        nomrt.getText().toString().trim(),passrt.getText().toString().trim());
                modify.updateUser(colid, up);

                modify.close();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                onSignupSuccess();
                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(),"Modifications enregistrées",Toast.LENGTH_LONG).show();
                            }
                        }, 3000);
            }
        });

        modify.close();

    }
    public void onSignupSuccess() {
        
       finish();
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
        switch(item.getItemId()) {
            case R.id.search_bar:
                Intent change = new Intent(this, SearchableActivity.class);
                this.startActivity(change);
                break;
            case R.id.action_settings:
                Intent param = new Intent(this, Parametres.class);
                this.startActivity(param);
                break;
           /* case R.id.action_histo:
                Intent historique= new Intent(this, HistoMouv.class);
                this.startActivity(historique);
                break;*/
            case R.id.search_bar2:
                Intent searchparts = new Intent(this, SearchableActivity2.class);
                this.startActivity(searchparts);
                break;


            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}

