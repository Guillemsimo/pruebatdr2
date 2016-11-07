package com.example.guillem.pruebatdr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatos extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE BaseDeDatos (nombre TEXT, apellidos TEXT, email TEXT)";
    String sqlCreate2 = "CREATE TABLE BaseDeDatos (nombre TEXT, apellidos TEXT, email TEXT)";

    public BaseDeDatos(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version){
        super(context,nombre,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versioantiga, int versionova) {
        db.execSQL("DROP TABLE IF EXISTS BaseDeDatos");
        db.execSQL(sqlCreate2);
    }
}
