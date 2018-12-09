package saci.development.sacilink.network;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import saci.development.sacilink.CadastroActivity;
import saci.development.sacilink.MainActivity;
import saci.development.sacilink.ModoManual;
import saci.development.sacilink.logInActivity;
import saci.development.sacilink.transactions.User;


public class   NewUser extends Thread {

    User user;
    HttpURLConnection conn;
    CadastroActivity activity;

    public NewUser(User newUser, CadastroActivity activity ){
        user = newUser;
        this.activity = activity;
    }

    public void run(){

        String stringURL = "http://10.0.77.249:3000/api/usuarios";

        try {
            URL url = new URL(stringURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonParam = new JSONObject();

            jsonParam.put("email", user.getEmail());
            jsonParam.put("senha", user.getSenha());

            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            if(conn.getResponseMessage().equals("Created")) {
                activity.cadastrado();
            } else {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Não foi possível cadastrar o usuário", Toast.LENGTH_SHORT).show();
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
