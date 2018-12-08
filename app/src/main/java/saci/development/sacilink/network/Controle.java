package saci.development.sacilink.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import saci.development.sacilink.CadastroActivity;
import saci.development.sacilink.MainActivity;
import saci.development.sacilink.transactions.User;


public class Controle extends Thread {

    MainActivity activity;
    HttpURLConnection conn;
    String comando;

    public Controle(String metodo ){
        this.activity = activity;
        comando = metodo;
    }
    public void run(){

        String stringURL = "http://192.168.1.9:3000/api/mosca/comando/"+comando;

        try {
            URL url = new URL(stringURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch( MalformedURLException ex ){
            ex.printStackTrace();
        } catch( IOException ex ){
            ex.printStackTrace();
        } finally {
        }

    }
}
