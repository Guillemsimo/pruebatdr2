package com.example.guillem.pruebatdr;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by guillem on 30/08/2016.
 */
public class MainActivity extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv11 = (TextView) findViewById(R.id.tv11);
        TextView tv22 = (TextView) findViewById(R.id.tv22);
        TextView tv33 = (TextView) findViewById(R.id.tv33);
        BaseDeDatos bdd = new BaseDeDatos(this, "BaseDeDatos", null,1);
        SQLiteDatabase db = bdd.getReadableDatabase();



        if(db!=null){
            Cursor c =db.rawQuery("select * from BaseDeDatos",null);
            int cantidad=c.getCount();
            int i=0;
            String[] arreglo=new String[cantidad];
            if (c.moveToFirst()) {
                tv11.setText("");
                tv22.setText("");
                tv33.setText("");
                    String nombre=c.getString(0);
                    String apellidos=c.getString(1);
                    String email=c.getString(2);
                    tv11.append(nombre );
                    tv22.append(apellidos );
                    tv33.append(email );
                }
        }
        if(tv11.getText().toString()==""){
        Intent regist;
        regist=new Intent(MainActivity.this,RegistroActivity.class);
        startActivity(regist);


        }

    }}

