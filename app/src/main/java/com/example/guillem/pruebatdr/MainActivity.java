package com.example.guillem.pruebatdr;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by guillem on 30/08/2016.
 */
public class MainActivity extends Activity {
    Button btn3;
    Context context;
    AlertDialog prueba;
    private NfcAdapter adapter;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    String tagcontent,method,nombre,apell,email,timee;
    TextView tv11,tv22,tv33;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn3=(Button)findViewById(R.id.btn3);
        tv11 = (TextView) findViewById(R.id.tv11);
        tv22 = (TextView) findViewById(R.id.tv22);
        tv33 = (TextView) findViewById(R.id.tv33);
        Button bt1 = (Button) findViewById(R.id.bt1);
        BaseDeDatos bdd = new BaseDeDatos(this, "BaseDeDatos", null,1);
        SQLiteDatabase db = bdd.getReadableDatabase();
        final AlertDialog.Builder prueba=new AlertDialog.Builder(this);
        prueba.setTitle("si");
        adapter = adapter.getDefaultAdapter(this);


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

        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent admintag;
                admintag=new Intent(MainActivity.this,AdminTag.class);
                startActivity(admintag);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                prueba.setMessage("no tio");
                final AlertDialog rabogi= prueba.create();
                rabogi.show();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void notificacio(){
        NotificationManager notman=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notif=new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("NFCtest")
                .setContentText("Tag llegit")
                .build();
        notman.notify(0,notif);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, adapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, adapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("mal tipus de mime");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {
                method="registrar2";
                nombre=tv11.getText().toString();
                apell=tv22.getText().toString();
                email=tv33.getText().toString();
                Calendar date=Calendar.getInstance();
                timee=date.getTime().toString();
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefReaderTask ndefReaderTask= new NdefReaderTask();
                try {
                    tagcontent=ndefReaderTask.execute(tag).get().toString();
                    HttpConnect connect=new HttpConnect(context);
                    String textt=connect.execute(method,nombre,apell,email,timee,tagcontent).get().toString();
                    Toast tostada=Toast.makeText(this,textt, Toast.LENGTH_LONG);
                    tostada.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            } else {
                Log.d(TAG, "mal tipus de mime: " + type);
            }

        }
    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // el tag no suporta ndef
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }


        protected void onPostExecute(String result) {
        }
    }

}

