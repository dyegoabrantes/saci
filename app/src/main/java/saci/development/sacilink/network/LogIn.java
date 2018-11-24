package saci.development.sacilink.network;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import saci.development.sacilink.CadastroActivity;
import saci.development.sacilink.MainActivity;
import saci.development.sacilink.logInActivity;
import saci.development.sacilink.transactions.User;

import static android.support.v4.content.ContextCompat.startActivity;


public class LogIn extends Thread {

    logInActivity activity;
    String email;
    String senha;
    HttpURLConnection conn;

    public LogIn(String uemail, String usenha, logInActivity activity ){
        email = uemail;
        senha = usenha;
        this.activity = activity;
    }

    public void run(){

        String stringURL = "http://192.168.0.7:3000/api/login";

        try {
            URL url = new URL(stringURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonParam = new JSONObject();

            jsonParam.put("email", email);
            jsonParam.put("senha", senha);

            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();
            Handler myHandler = new Handler(Looper.getMainLooper());

            if(conn.getResponseMessage().equals("OK")) {
                activity.logado();
            } else {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Usuário e/ou senha incorreto", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch( MalformedURLException ex ){
            ex.printStackTrace();
        } catch( IOException ex ){
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
        }

    }

}
