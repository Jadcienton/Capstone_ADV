package com.example.alejandro.myapplication;


import android.app.ProgressDialog;
import android.arch.lifecycle.ReportFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DailyEventFragment.OnFragmentInteractionListener, HistoricEventFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,EventRegisterFragment.OnFragmentInteractionListener {
    private Session session;
    private ProgressDialog progress;
    private String name ="", role ="";
    private String email ="";
    private TextView userTest, emailTest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new Session(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        name = Preferences.getPreferenceStringName(this,Preferences.usuarioLogin);
        email = Preferences.getPreferenceStringEmail(this,Preferences.usuarioLoginEmail);
        role = Preferences.getPreferenceStringRol(this,Preferences.usuarioRol);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (role.equals("Gerente")){
            navigationView.getMenu().setGroupVisible(R.id.contractor,false);
            navigationView.getMenu().setGroupVisible(R.id.inspector,false);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        }else if (role.equals("Contratista")){
            navigationView.getMenu().setGroupVisible(R.id.management,false);
            navigationView.getMenu().setGroupVisible(R.id.inspector,false);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new EventRegisterFragment()).commit();
        }else if (role.equals("Inspector")){
            navigationView.getMenu().setGroupVisible(R.id.contractor,false);
            navigationView.getMenu().setGroupVisible(R.id.management,false);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new DailyEventFragment()).commit();
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        userTest = findViewById(R.id.name_header);
        emailTest = findViewById(R.id.email_header);
        userTest.setText(name);
        emailTest.setText(email);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //DailyEventFragment fragmento = null;


        int id = item.getItemId();

        if (id == R.id.home) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        } else if (id == R.id.daily_ev || id == R.id.daily_ev_i) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new DailyEventFragment()).commit();

        } else if (id == R.id.historic_ev || id == R.id.historic_ev_i) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HistoricEventFragment()).commit();

        } else if (id == R.id.map_ev || id == R.id.map_ev_c || id == R.id.map_ev_i) {
            Intent intent = new Intent(this,Comments.class);
            startActivity(intent);
        } else if (id == R.id.register_ev_c || id == R.id.register_ev_i) {
            //Intent intent = new Intent(this,EventRegistration.class);
            //startActivity(intent);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new EventRegisterFragment()).commit();


        } else if (id == R.id.report) {
            //getFragmentManager().beginTransaction().replace(R.id.fragment_container,new ReportFragment()).commit();
        } else if (id == R.id.nav_logout) {
            progress = new ProgressDialog(this);
            progress.setMessage("Cerrando sesión...");
            progress.show();
            if (session.loggedin()) {
                logout();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    private void logout(){
        session.setLoggedin(false);
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
