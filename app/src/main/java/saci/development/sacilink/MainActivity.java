package saci.development.sacilink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.things.pio.Gpio;
import com.google.firebase.auth.FirebaseAuth;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MqttCallback {

    DrawerLayout drawer;

    Intent intentService;
    TextView textViewStatusLuz;
    TextView textViewStatusRega;
    TextView textViewStatusVentila;
    TextView textViewStatusTrocaAr;
    TextView textViewStatusTemperatura;
    TextView textViewStatusUmidadeAr;
    TextView textViewStatusumidadeSolo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        /*try {
            MqttClient client = new MqttClient("192.168.1.4:1883", "SmatphoneA1Pro777", new MemoryPersistence());
            client.setCallback(this);
            client.connect();

            String topic = "umidade_e_temperatura";
            client.subscribe(topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }*/
        intentService= new Intent(this, Monitora.class);
        this.startService(intentService);

        textViewStatusLuz = (TextView) findViewById(R.id.statusLuz);
        textViewStatusRega = findViewById(R.id.statusIrriga);
        textViewStatusVentila = findViewById(R.id.statusVentila);
        // textViewStatusVentila = findViewById(R.id.statusTrocaAr);
        textViewStatusTemperatura = findViewById(R.id.textViewTemperatura);
        textViewStatusUmidadeAr = findViewById(R.id.textViewUmidadeAr);
        textViewStatusumidadeSolo = findViewById(R.id.textViewUmidadeSolo);



        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int ilumina = (int) intent.getExtras().get("ilumina");

                        if (ilumina == 0) {
                            textViewStatusLuz.setText("Iluminação desligada");
                        }else {
                            if (ilumina == 1) {
                                textViewStatusLuz.setText("Iluminação ligada");
                            }else {
                                textViewStatusLuz.setText("ERRO");
                                System.out.println(ilumina);
                            }

                        }

                        String temperatura = intent.getExtras().get("temperatura").toString();
                        textViewStatusTemperatura.setText(temperatura+" ºC");

                        String umidadeAr = intent.getExtras().get("umidade").toString();
                        textViewStatusUmidadeAr.setText(umidadeAr);

                    }
                }, new IntentFilter(Monitora.UPDATEVIEW)
        );
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_status) {

        } else if (id == R.id.nav_modoManual) {
            Intent goManual = new Intent(this, ModoManual.class);
            startActivity(goManual);
        } else if (id == R.id.nav_modoAutomatico) {

        }  else if (id == R.id.nav_conecta) {
            Intent goConexao = new Intent(this, conectarEstufa.class);
            startActivity(goConexao);
        } else if (id == R.id.nav_map) {
            Intent goMaps = new Intent(this, MapsActivity.class);
            startActivity(goMaps);
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Intent goLogin = new Intent(this, logInActivity.class);
            startActivity(goLogin);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
