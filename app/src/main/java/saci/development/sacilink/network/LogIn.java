package saci.development.sacilink.network;

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

    MainActivity activity;
    String email;
    String senha;
    HttpURLConnection conn;

    public LogIn(String uemail, String usenha ){
        email = uemail;
        senha = usenha;
        this.activity = activity;
    }

    public void run(){

        String stringURL = "http://169.254.228.61:3000/api/login";

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
         //   os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();
            Handler myHandler = new Handler(Looper.getMainLooper());

            /*if (conn.getResponseCode() == 200){
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(context, "Hello World!", Toast.LENGTH.SHORT).show();
                    }
                });
            }*/

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
