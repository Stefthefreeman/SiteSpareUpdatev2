package com.agence_creation_sc.tools.sitespareupdatev2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class FirstConnection extends AppCompatActivity {





        // UI References
        private EditText textEtxt;
        private EditText textprenom;
        private EditText textuserrt;
        private EditText textpswdrt;
        private Button saveButton;
        private Button activity2Button;

        private String text;
        private String prenom;
        private String userrt;
        private String pswrt;
    private String email;
    private String password;

        private SharedPreference sharedPreference;

        Activity context = this;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.firstconnec);

            sharedPreference = new SharedPreference();

            findViewsById();

            saveButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    text = textEtxt.getText().toString();
                    prenom = textprenom.getText().toString();
                    userrt = textuserrt.getText().toString();
                    pswrt = textpswdrt.getText().toString();

                    UserBDD insernewuser = new UserBDD(FirstConnection.this);
                    insernewuser.open();
                  // insernewuser.delete();
                    User thenew = new User(text,prenom,userrt,pswrt,email,password);
                    insernewuser.insertUser(thenew);


                //    mail=  email.getText().toString();
                    // Hides the soft keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textEtxt.getWindowToken(), 0);

                    // Save the text in SharedPreference
                    sharedPreference.save(context, text);
                 //   sharedPreference.save(context, mail);
                    Toast.makeText(context,
                            thenew.toString(),
                            Toast.LENGTH_LONG).show();
                    insernewuser.close();
                    Intent backhome= new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(backhome);
                }
            });

           /* activity2Button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Parametres.class);
                    // Start next activity
                    startActivity(intent);
                }
            });*/
        }

        private void findViewsById() {
            textEtxt = (EditText) findViewById(R.id.etxt_text);
            textprenom = (EditText) findViewById(R.id.etxt_prenom);
            textuserrt = (EditText) findViewById(R.id.etxt_userrt);
            textpswdrt = (EditText) findViewById(R.id.etxt_pswdrt);
            saveButton = (Button) findViewById(R.id.button_save);
            activity2Button = (Button) findViewById(R.id.button_second_activity);
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
         /*switch(item.getItemId()) {
           case R.id.search_bar:
                Intent change = new Intent(this, SearchableActivity.class);
                this.startActivity(change);
                break;
            case R.id.action_settings:
                Intent param = new Intent(this, Parametres.class);
                this.startActivity(param);
                break;
            case R.id.action_histo:
                Intent historique= new Intent(this, HistoMouv.class);
                this.startActivity(historique);
                break;
            case R.id.search_bar2:
                Intent searchparts = new Intent(this, SearchableActivity2.class);
                this.startActivity(searchparts);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }*/

        return true;
    }
    }


