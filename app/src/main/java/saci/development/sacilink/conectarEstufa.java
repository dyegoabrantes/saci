package saci.development.sacilink;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

public class  conectarEstufa extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Intent intent = getIntent();
    String nav_id;

    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conectar_estufa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_conectarEstufa);

        drawer  = (DrawerLayout) findViewById(R.id.conectar_estufa_drawer);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_conectar);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(3).setChecked(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


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
        getMenuInflater().inflate(R.menu.activity_conectar_estufa_drawer, menu);
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
            Intent goManual = new Intent(this, ModoManual.class);
            startActivity(goManual);
        } else if (id == R.id.nav_modoAutomatico) {

        } else if (id == R.id.nav_conecta) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
