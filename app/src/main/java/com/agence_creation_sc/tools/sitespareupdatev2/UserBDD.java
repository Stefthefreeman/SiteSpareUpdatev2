package com.agence_creation_sc.tools.sitespareupdatev2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserBDD {

    private static final int VERSION_BDD = 5;
    private static final String NOM_BDD = "user.db";

    private static final String TABLE_USER = "table_user";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NOM = "NOM";
    private static final int NUM_COL_NOM = 1;
    private static final String COL_PRENOM= "PRENOM";
    private static final int NUM_COL_PRENOM = 2;
    private static final String COL_EMAIL = "EMAIL";
    private static final int NUM_COL_EMAIL= 3;
    private static final String COL_PASSWORD = "PASSWORD";
    private static final int NUM_COL_PASSWORD= 4;
    private static final String COL_USERRT = "USERRT";
    private static final int NUM_COL_USERRT = 5;
    private static final String COL_PSWDRT = "PSWDRT";
    private static final int NUM_COL_PSWDRT= 6;

    private static final String COL_TIMESTAMPS= "TIMESTAMPS";
    private static final int NUM_COL_TIMESTAMPS = 7;

    private SQLiteDatabase bdd;

    private DataBaseUser maBaseSQLite;

    public UserBDD(Context context) {
        maBaseSQLite = new DataBaseUser(context, NOM_BDD, null, VERSION_BDD);
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
     * @param user
     *            le contact à insérer
     * @return l'identifiant de la ligne insérée
     */
    public long insertUser(User user) {
        ContentValues values = new ContentValues();

        // On insère les valeurs dans le ContentValues : on n'ajoute pas
        // l'identifiant car il est créé automatiquement
        values.put(COL_NOM, user.getNom());
        values.put(COL_PRENOM, user.getPrenom());
        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_PASSWORD, user.getPassword());
        values.put(COL_USERRT, user.getUserrt());
        values.put(COL_PSWDRT, user.getPswdrt());



        return bdd.insert(TABLE_USER, null, values);
    }

    /**
     * Met à jour le contact en base de données
     *
     * @param id
     *            l'identifiant du contact à modifier
     * @param user
     *            le nouveau contact à associer à l'identifiant
     * @return le nombre de lignes modifiées
     */
    public int updateUser(int id, User user) {
        ContentValues values = new ContentValues();
        values.put(COL_NOM, user.getNom());
        values.put(COL_PRENOM, user.getPrenom());
        values.put(COL_USERRT, user.getUserrt());
        values.put(COL_PSWDRT, user.getPswdrt());
        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_PASSWORD, user.getPassword());
        return bdd.update(TABLE_USER, values, COL_ID + " = " + id, null);
    }
    public String getLogin(String user, String password)throws SQLException {

        Cursor c = bdd.rawQuery("SELECT * from table_user where EMAIL = '" + user + "' AND PASSWORD = '" + password + "'", null);

        String name = "";
        if(c!=null && c.moveToFirst() )
        {
             name = "OK";
            c.close();

        }

        return name;
    }

    public boolean getifexist()throws SQLException {

        Cursor c = bdd.rawQuery("SELECT * from table_user where NOM != ''", null);


        if(c!=null && c.moveToFirst() )
        {

            c.close();
            return true;

        }

        return false;
    }

    public void delete(){
        bdd.delete(TABLE_USER, null, null);
    }



    /**
     * Retourne le premier contact dont le numéro de téléphone correspond à
     * celui en paramètre
     *
     * @param userid
     *            le numéro de téléphone
     * @return le contact récupéré depuis la base de données
     */
    public User getFirstUser(String userid) {
        Cursor c = bdd.query(TABLE_USER, new String[] { COL_ID, COL_NOM,
                COL_PRENOM ,COL_EMAIL,COL_PASSWORD,COL_USERRT,COL_PSWDRT}, COL_ID + " = '"
                + userid + "'", null, null, null, null);
        return cursorToContact(c);
    }
    public User cursorToContact(Cursor c) {
        // si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        // Sinon on se place sur le premier élément
        c.moveToFirst();

        User user = new User();
        user.setId(c.getInt(NUM_COL_ID));
        user.setNom(c.getString(NUM_COL_NOM));
        user.setPrenom(c.getString(NUM_COL_PRENOM));
        user.setEmail(c.getString(NUM_COL_EMAIL));
        user.setPassword(c.getString(NUM_COL_PASSWORD));
        user.setUserrt(c.getString(NUM_COL_USERRT));
        user.setPswdrt(c.getString(NUM_COL_PSWDRT));




        c.close();

        return user;
    }
    /**
     * Convertit le cursor en contact
     *
     * @param c
     *            le cursor à convertir
     * @return le Contact créé à partir du Cursor
     */
    public User cursorToUser(Cursor c) {
        // si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        // Sinon on se place sur le premier élément
        c.moveToFirst();

        User user = new User();
        user.setId(c.getInt(NUM_COL_ID));
        user.setNom(c.getString(NUM_COL_NOM));
        user.setPrenom(c.getString(NUM_COL_PRENOM));
        user.setUserrt(c.getString(NUM_COL_USERRT));
        user.setPswdrt(c.getString(NUM_COL_PSWDRT));
        user.setPassword(c.getString(NUM_COL_PASSWORD));
        user.setEmail(c.getString(NUM_COL_EMAIL));


        c.close();

        return user;
    }

}