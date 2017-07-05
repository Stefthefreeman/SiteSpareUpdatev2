package com.agence_creation_sc.tools.sitespareupdatev2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseUser extends SQLiteOpenHelper {

    private static final String TABLE_USER = "table_user";
    private static final String COL_ID = "ID";
    private static final String COL_NOM = "NOM";
    private static final String COL_PRENOM = "PRENOM";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_PASSWORD = "PASSWORD";
    private static final String COL_USERRT = "USERRT";
    private static final String COL_PSWDRT = "PSWDRT";



    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + " (" + COL_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOM
            + " TEXT NOT NULL, " + COL_PRENOM + " TEXT NOT NULL, " + COL_EMAIL + " TEXT NOT NULL,"+ COL_PASSWORD + " TEXT NOT NULL,"
            + COL_USERRT + " TEXT NOT NULL, " + COL_PSWDRT + " TEXT NOT NULL);";


    public DataBaseUser (Context context, String name, CursorFactory factory,
                            int version) {
        super(context, name, factory, version);
    }

    /**
     * Cette méthode est appelée lors de la toute première création de la base
     * de données. Ici, on doit créer les tables et éventuellement les populer.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on crée la table table_contacts dans la BDD
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on supprime la table table_contacts de la BDD et on recrée la BDD
        db.execSQL("DROP TABLE " + TABLE_USER + ";");
        onCreate(db);
    }

}
