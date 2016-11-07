package com.example.guillem.pruebatdr;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegistroActivity extends AppCompatActivity {



    Button btaceptar;
    Intent siguiente;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrio);
        BaseDeDatos bdd = new BaseDeDatos(this, "BaseDeDatos", null,1);
        SQLiteDatabase db = bdd.getReadableDatabase();

        btaceptar = (Button) findViewById(R.id.button);
        btaceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Context context = getApplicationContext();
                BaseDeDatos bdd = new BaseDeDatos(context, "BaseDeDatos", null,1);
                SQLiteDatabase db = bdd.getWritableDatabase();
                String nombre = ((EditText)findViewById(R.id.et1)).getText().toString();
                String apellidos = ((EditText)findViewById(R.id.et2)).getText().toString();
                String email = ((EditText)findViewById(R.id.et3)).getText().toString();
                db.execSQL("INSERT INTO BaseDeDatos (nombre,apellidos,email) " + "VALUES ('" + nombre + "','" + apellidos + "','" + email + "')");
                db.close();
                siguiente = new Intent(RegistroActivity.this, MainActivity.class);
                startActivity(siguiente);

            }


            });}}











