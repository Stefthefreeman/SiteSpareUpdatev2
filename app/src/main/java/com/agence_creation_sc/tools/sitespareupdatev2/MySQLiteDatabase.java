package com.agence_creation_sc.tools.sitespareupdatev2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteDatabase extends SQLiteOpenHelper {

    private static final String TABLE_MOUVEMENTS = "table_mouvements";
    private static final String COL_ID = "ID";
    private static final String COL_SNOK = "SERIALOK";
    private static final String COL_PNHS = "PARTHS";
    private static final String COL_SNHS = "SERIALHS";
    private static final String COL_QROK = "QRCODEOK";
    private static final String COL_QRHS = "QRCODEHS";
    private static final String COL_LONGITUDE= "LONGITUDE";
    private static final String COL_LATITUDE = "LATITUDE";
    private static final String COL_TIMESTAMPS= "TIMESTAMPS";
    private static final String COL_TICKET= "TICKET";

    private static final String CREATE_TABLE_MOUVEMENTS = "CREATE TABLE "
            + TABLE_MOUVEMENTS + " (" + COL_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_SNOK
            + " TEXT NOT NULL, " + COL_PNHS+ " TEXT NOT NULL, "
            + COL_SNHS + " TEXT NOT NULL, " + COL_QROK + " TEXT NOT NULL, " + COL_QRHS + " TEXT NOT NULL, " + COL_LONGITUDE + " TEXT NOT NULL,"
    + COL_LATITUDE + " TEXT NOT NULL, " + COL_TIMESTAMPS + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + COL_TICKET + " TEXT NOT NULL);";
                     

    public MySQLiteDatabase(Context context, String name, CursorFactory factory,
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
        db.execSQL(CREATE_TABLE_MOUVEMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on supprime la table table_contacts de la BDD et on recrée la BDD
        db.execSQL("DROP TABLE " + TABLE_MOUVEMENTS + ";");
        onCreate(db);
    }

}
