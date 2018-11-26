package saci.development.sacilink;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Monitora extends IntentService {

    public Monitora() {
        super("Monitora");
    }

    Intent intent;
    public static final String UPDATEVIEW = "0";
    HttpURLConnection conn;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(UPDATEVIEW);
    }

    private void sendBroadcastMessage(int res) {
        intent.putExtra("luz_estado", res);
        intent.setAction(UPDATEVIEW);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    String stringURL = "http://192.168.1.6:3000/api/mosca/getstate";
                    String estadoAtual;
                    try {
                        URL url = new URL(stringURL);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept","application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        JSONObject jsonParam = new JSONObject();

                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                        //   os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                        os.writeBytes(jsonParam.toString());

                        os.flush();
                        os.close();
                        Handler myHandler = new Handler(Looper.getMainLooper());

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG" , conn.getResponseMessage());

                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuffer result = new StringBuffer();

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        String json = result.toString();
                        JSONObject jo = new JSONObject(json);
                        int resultado = jo.getInt("estado");
                        sendBroadcastMessage(resultado);
                        in.close();
                        conn.disconnect();
                    } catch( MalformedURLException ex ){
                        ex.printStackTrace();
                    } catch( IOException ex ){
                        ex.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        conn.disconnect();
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        return Service.START_STICKY;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

        }
    }

}
