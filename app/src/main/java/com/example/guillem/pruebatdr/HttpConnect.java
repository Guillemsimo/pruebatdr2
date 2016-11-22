package com.example.guillem.pruebatdr;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by guillem on 14/11/2016.
 */

public class HttpConnect extends AsyncTask<String,Void,String> {
    private Context ctx;
    AlertDialog rabo;

    HttpConnect(Context ctx){
        HttpConnect.this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        rabo = new AlertDialog.Builder(HttpConnect.this.ctx.getApplicationContext()).create();
        rabo.setTitle("Login Information...");
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url="http://www.tdrtestnfc.esy.es/tdr_test/signup.php";
        String log_url="http://www.tdrtestnfc.esy.es/tdr_test/login.php";
        String info_url="http://www.tdrtestnfc.esy.es/tdr_test/pedirinfo.php";
        String method=params[0];
        if (method == "registrar"){
            String name=params[1];
            String email=params[2];
            String pass=params[3];
            try{
                URL url= new URL(reg_url);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream OS=conn.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+URLEncoder.encode("email","UTF-8")+"="+
                        URLEncoder.encode(email,"UTF-8")+"&"+URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS=conn.getInputStream();
                IS.close();
                return "Registre complet";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(method=="login"){
            String name=params[1];
            String pass=params[2];
            try{
                URL url= new URL(log_url);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream OS=conn.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS=conn.getInputStream();
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                String line="";
                String response="";
                while ((line =bufferedReader.readLine())!=null){
                    response+= line;
                }
                bufferedReader.close();
                IS.close();
                conn.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(method=="info"){
            String name=params[1];
            String pass=params[2];
            try{
                URL url= new URL(info_url);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream OS=conn.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS=conn.getInputStream();
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                String line="";
                String response="";
                while ((line =bufferedReader.readLine())!=null){
                    response+= line;
                }
                bufferedReader.close();
                IS.close();
                conn.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        if(s.equals("Registre complet")) {
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
        }else if(s.equals("Login Success. Hola user")){
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
        }
    }
}
