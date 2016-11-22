package com.example.guillem.pruebatdr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;


/**
 * Created by guillem on 11/11/2016.
 */
public class AdminTagLogin extends Activity {
    Button btn1,btn2,btn3;
    EditText etupn,etupe,etupp,etinn,etinp;
    TextView tv3;
    HttpConnect asyncTask;
    String test;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admintaglogin);
        btn1= (Button)findViewById(R.id.button);
        btn2=(Button)findViewById(R.id.button2);
        etupn=(EditText) findViewById(R.id.et1);
        etupe=(EditText) findViewById(R.id.et2);
        etupp=(EditText) findViewById(R.id.et3);
        etinn = (EditText) findViewById(R.id.et4);
        etinp = (EditText) findViewById(R.id.et5);
        tv3 = (TextView) findViewById(R.id.tv3);
        final BaseDeDatos bdduser = new BaseDeDatos(this, "BaseDeDatosOnline", null,1);
        SQLiteDatabase db = bdduser.getReadableDatabase();


        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Context context;
                context=getApplicationContext();
                String name= etupn.getText().toString();
                String email=etupe.getText().toString();
                String pass=etupp.getText().toString();
                String method="registrar";
                HttpConnect connect=new HttpConnect(context);
                connect.execute(method,name,email,pass);

            }});
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Context context;
                context=getApplicationContext();
                String lname=etinn.getText().toString();
                String lpass=etinp.getText().toString();
                String method="login";
                HttpConnect connect=new HttpConnect(context);
                try {
                    test=connect.execute(method,lname,lpass).get();
                    String test2="Login Complet. Hola ";
                    String nombre=test2+lname;
                    if(test.equals(nombre)){
                        SQLiteDatabase db = bdduser.getWritableDatabase();
                        if (db != null){
                            db.delete("BaseDeDatosOnline",null,null);
                        }
                        db.execSQL("INSERT INTO BaseDeDatosOnline (nombre,pass) " + "VALUES ('" + lname + "','" + lpass + "')");
                        db.close();
                        Intent admin;
                        admin=new Intent(AdminTagLogin.this,AdminTag.class);
                        startActivity(admin);
                        finish();
                    } else {
                        tv3.setText(test);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }});

    }


}

