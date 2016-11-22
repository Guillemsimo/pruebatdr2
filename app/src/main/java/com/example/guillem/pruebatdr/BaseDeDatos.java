package com.example.guillem.pruebatdr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatos extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE BaseDeDatos (nombre TEXT, apellidos TEXT, email TEXT)";
    String sqlCreate2 = "CREATE TABLE BaseDeDatos (nombre TEXT, apellidos TEXT, email TEXT)";
    String sql2Create= "CREATE TABLE BaseDeDatosOnline(nombre TEXT, pass TEXT, email TEXT)";

    public BaseDeDatos(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version){
        super(context,nombre,factory,version);
    }



    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(sqlCreate);
        db.execSQL(sql2Create);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int versioantiga, int versionova) {
        db.execSQL("DROP TABLE IF EXISTS BaseDeDatos");
        db.execSQL(sqlCreate2);
    }

    public void deleteDataNombre(String string){
        this.getWritableDatabase().delete("BaseDeDatosOnline","nombre",null);
    }
    public void deleteDataPass(String string){
        this.getWritableDatabase().delete("BaseDeDatosOnline","pass",null);
    }
}
