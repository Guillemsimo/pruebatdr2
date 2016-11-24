package com.example.guillem.pruebatdr;

import android.app.Activity;
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
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by guillem on 20/11/2016.
 */

public class AdminTag extends Activity {

    String test;
    TextView tv4,tv6;
    String nombre, pass;
    Button btn2;
    Switch cswitch;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    private NfcAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admintag);
        cswitch=(Switch) findViewById(R.id.switch1);
        adapter=adapter.getDefaultAdapter(this);
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
                if(cswitch.isChecked()){
                    Tag tag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    NdefMessage tagnombre=createTextMessage(nombre);
                    writeTag(tag,tagnombre);
                    Toast teste=Toast.makeText(this,"si",Toast.LENGTH_LONG);
                    teste.show();
                }
            } else {
                Log.d(TAG, "mal tipus de mime: " + type);
            }

        }
    }

    public void writeTag(Tag tag, NdefMessage message) {
        if (tag != null) {
            try {
                Ndef ndefTag = Ndef.get(tag);
                if (ndefTag == null)  {
                    // Let's try to format the Tag in NDEF
                    NdefFormatable nForm = NdefFormatable.get(tag);
                    if (nForm != null) {
                        nForm.connect();
                        nForm.format(message);
                        nForm.close();
                    }
                }
                else {
                    ndefTag.connect();
                    ndefTag.writeNdefMessage(message);
                    ndefTag.close();
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public NdefMessage createTextMessage(String content) {
        try {
            // Get UTF-8 byte
            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                    NdefRecord.RTD_TEXT, new byte[0],
                    payload.toByteArray());
            return new NdefMessage(new NdefRecord[]{record});
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    }
