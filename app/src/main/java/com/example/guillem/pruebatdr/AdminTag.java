package com.example.guillem.pruebatdr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

/**
 * Created by guillem on 20/11/2016.
 */

public class AdminTag extends Activity {

    String test;
    TextView tv4,tv6;
    String nombre, pass;
    Button btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admintag);
        BaseDeDatos bdduser = new BaseDeDatos(this,"BaseDeDatosOnline", null,1);
        final SQLiteDatabase db = bdduser.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from BaseDeDatosOnline",null);
            if (cursor != null && cursor.moveToFirst()) {
                nombre = cursor.getString(0);
                pass = cursor.getString(1);
                cursor.close();
                Context context = getApplicationContext();
                HttpConnect connect = new HttpConnect(context);
                try {
                    String method = "info";
                    String lname = nombre;
                    String lpass = pass;
                    test = connect.execute(method, lname, lpass).get();
                    String test2 = "algo faia amigo";
                    if (test.equals(test2)) {
                        Intent malLog;
                        malLog = new Intent(AdminTag.this, AdminTagLogin.class);
                        startActivity(malLog);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                tv4 = (TextView) findViewById(R.id.tv4);
                tv6 = (TextView) findViewById(R.id.tv6);
                tv4.setText(nombre);
                tv6.setText(test);
                btn2 = (Button) findViewById(R.id.btn2);
                btn2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){
                        db.delete("BaseDeDatosOnline",null,null);
                        Intent sinReg;
                        sinReg = new Intent(AdminTag.this, AdminTagLogin.class);
                        startActivity(sinReg);
                        finish();

                    }


                });
            } else {
                db.delete("BaseDeDatosOnline",null,null);
                Intent sinReg;
                sinReg = new Intent(AdminTag.this, AdminTagLogin.class);
                startActivity(sinReg);
                finish();
            }


        }


    }
