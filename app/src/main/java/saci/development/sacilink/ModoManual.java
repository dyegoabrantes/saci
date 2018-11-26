package saci.development.sacilink;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.eclipse.paho.client.mqttv3.MqttCallback;

import java.util.ArrayList;
import java.util.List;

public class ModoManual extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ToggleButton alteraIluminacao;
    ToggleButton alteraCorrente;
    ToggleButton alteraTroca;
    ToggleButton alteraRega;

    DrawerLayout drawer;
    NavigationView navigationView;

    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_manual2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_modo_maual);

        drawer = (DrawerLayout) findViewById(R.id.modo_manual_drawer);

        navigationView = (NavigationView) findViewById(R.id.nav_view_modo_manual);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);

        alteraIluminacao = findViewById(R.id.alteraIluminacao);
        alteraCorrente = findViewById(R.id.alteraCorrente);
        alteraTroca = findViewById(R.id.alteraTroca);
        alteraRega = findViewById(R.id.alteraRega);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        alteraIluminacao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });

        alteraCorrente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });

        alteraTroca.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });

        alteraRega.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_modo_manual2_drawer, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_status) {
            Intent goHome = new Intent(this, MainActivity.class);
            goHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goHome);
        } else if (id == R.id.nav_modoManual) {

        } else if (id == R.id.nav_modoAutomatico) {

        } else if (id == R.id.nav_conecta) {
            Intent goConexao = new Intent(this, conectarEstufa.class);
            startActivity(goConexao);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
