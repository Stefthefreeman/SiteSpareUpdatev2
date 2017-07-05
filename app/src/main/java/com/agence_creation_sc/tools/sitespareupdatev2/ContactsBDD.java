package com.agence_creation_sc.tools.sitespareupdatev2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ContactsBDD {

    private static final int VERSION_BDD = 5;
    private static final String NOM_BDD = "mouvements.db";

    private static final String TABLE_MOUVEMENTS = "table_mouvements";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_SNOK = "SERIALOK";
    private static final int NUM_COL_SNOK = 1;
    private static final String COL_PNHS = "PARTHS";
    private static final int NUM_COL_PNHS = 2;
    private static final String COL_SNHS = "SERIALHS";
    private static final int NUM_COL_SNHS = 3;
    private static final String COL_QROK = "QRCODEOK";
    private static final int NUM_COL_QROK = 4;
    private static final String COL_QRHS = "QRCODEHS";
    private static final int NUM_COL_QRHS = 5;
    private static final String COL_LONGITUDE= "LONGITUDE";
    private static final int NUM_COL_LONGITUDE = 6;
    private static final String COL_LATITUDE = "LATITUDE";
    private static final int NUM_COL_LATITUDE = 7;
    private static final String COL_TIMESTAMPS= "TIMESTAMPS";
    private static final int NUM_COL_TIMESTAMPS = 8;
    private static final String COL_TICKET= "TICKET";
    private static final int NUM_COL_TICKET = 9;
    private SQLiteDatabase bdd;

    private MySQLiteDatabase maBaseSQLite;

    public ContactsBDD(Context context) {
        maBaseSQLite = new MySQLiteDatabase(context, NOM_BDD, null, VERSION_BDD);
    }

    /**
     * Ouvre la BDD en écriture
     */
    public void open() {
        bdd = maBaseSQLite.getWritableDatabase();
    }

    /**
     * Ferme l'accès à la BDD
     */
    public void close() {
        bdd.close();
    }

    public SQLiteDatabase getBDD() {
        return bdd;
    }

    /**
     * Insère un contact en base de données
     *
     * @param contact
     *            le contact à insérer
     * @return l'identifiant de la ligne insérée
     */
    public long insertContact(Contact contact) {
        ContentValues values = new ContentValues();

        // On insère les valeurs dans le ContentValues : on n'ajoute pas
        // l'identifiant car il est créé automatiquement
        values.put(COL_SNOK, contact.getSnok());
        values.put(COL_PNHS, contact.getPnhs());
        values.put(COL_SNHS, contact.getSnhs());
        values.put(COL_QROK, contact.getQrok());
        values.put(COL_QRHS, contact.getQrhs());
        values.put(COL_LONGITUDE, contact.getLongitude());
        values.put(COL_LATITUDE, contact.getLatitude());
       // values.put(COL_TIMESTAMPS, contact.getTimestamps());
        values.put(COL_TICKET, contact.getTicket());

        return bdd.insert(TABLE_MOUVEMENTS, null, values);
    }

    /**
     * Met à jour le contact en base de données
     *
     * @param id
     *            l'identifiant du contact à modifier
     * @param contact
     *            le nouveau contact à associer à l'identifiant
     * @return le nombre de lignes modifiées
     */
    public int updateContact(int id, Contact contact) {
        ContentValues values = new ContentValues();
        values.put(COL_SNOK, contact.getSnok());
        values.put(COL_PNHS, contact.getPnhs());
        values.put(COL_SNHS, contact.getSnhs());
        values.put(COL_QROK, contact.getQrok());
        values.put(COL_QRHS, contact.getQrhs());
        values.put(COL_LONGITUDE, contact.getLongitude());
        values.put(COL_LATITUDE, contact.getLatitude());
       // values.put(COL_TIMESTAMPS, getDateTime().toString() );
        values.put(COL_TICKET, contact.getTicket());
        return bdd.update(TABLE_MOUVEMENTS, values, COL_ID + " = " + id, null);
    }

    /**
     * Supprime un contact de la BDD (celui dont l'identifiant est passé en
     * paramètres)
     *
     * @param id
     *            l'identifiant du contact à supprimer
     * @return le nombre de contacts supprimés
     */
    public int removemouvement(String id) {
        return bdd.delete(TABLE_MOUVEMENTS, COL_ID + " = " + id, null);
    }

    public void delete(){
     bdd.delete(TABLE_MOUVEMENTS, null, null);
    }



    /**
     * Retourne le premier contact dont le numéro de téléphone correspond à
     * celui en paramètre
     *
     * @param numeroTelephone
     *            le numéro de téléphone
     * @return le contact récupéré depuis la base de données
     */
   /* public Contact getFirstContactWithNumeroTelephone(String numeroTelephone) {
        Cursor c = bdd.query(TABLE_CONTACTS, new String[] { COL_ID, COL_NOM,
                COL_PRENOM, COL_NUM_TEL }, COL_NUM_TEL + " LIKE "
                + numeroTelephone + "", null, null, null, null);
        return cursorToContact(c);
    }*/

    /**
     * Convertit le cursor en contact
     *
     * @param c
     *            le cursor à convertir
     * @return le Contact créé à partir du Cursor
     */
    public Contact cursorToContact(Cursor c) {
        // si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        // Sinon on se place sur le premier élément
        c.moveToFirst();

        Contact contact = new Contact();
        contact.setId(c.getInt(NUM_COL_ID));
        contact.setSnok(c.getString(NUM_COL_SNOK));
        contact.setPnhs(c.getString(NUM_COL_PNHS));
        contact.setSnhs(c.getString(NUM_COL_SNHS));
        contact.setQrok(c.getString(NUM_COL_QROK));
        contact.setQrhs(c.getString(NUM_COL_QRHS));
        contact.setLongitude(c.getDouble(NUM_COL_LONGITUDE));
        contact.setLatitude(c.getDouble(NUM_COL_LATITUDE));
        contact.setTimestamps(c.getString(NUM_COL_TIMESTAMPS));
        contact.setTicket(c.getString(NUM_COL_TICKET));

        c.close();

        return contact;
    }
    public String getWithrtnum(String numrt)throws SQLException {

        Cursor c = bdd.rawQuery("SELECT * from table_mouvements where TICKET= '" + numrt + "'", null);


        if(c!=null && c.moveToFirst() )
        {
            String  name = c.getString(1);
            double latitude = c.getDouble(7);
            double longitude = c.getDouble(6);
            c.close();
            return name;
        }
        String test="test";
        return test;
    }
    public String getFirstboard(String scanpartsok)throws SQLException {

        Cursor c = bdd.rawQuery("SELECT * from table_mouvements where SERIALOK= '" + scanpartsok + "'", null);


        if(c!=null && c.moveToFirst() )
        {
            String  name = c.getString(1);
            c.close();
            return name;
        }
        String test="test";
        return test;
    }

    public List<Map<String, ?>> getAllPersonnes() {
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        String selectQuery = "SELECT * FROM table_mouvements ORDER BY TIMESTAMPS DESC";
        Cursor cursor = bdd.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> map =new HashMap();
                map.put("id", cursor.getInt(0));
                map.put("snok", cursor.getString(1).replace("_"," ").toString());
                map.put("pnhs", cursor.getString(2).toString()+" "+cursor.getString(3).toString());
                map.put("time", cursor.getString(9).toString()+" - "+cursor.getString(8).toString());
                items.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;



    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
