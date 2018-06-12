package com.example.alejandro.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class EventDetail extends AppCompatActivity {

    private CoordinatorLayout mainlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        toolbar.setTitle("Detalle");
        setSupportActionBar(toolbar);

        mainlayout = findViewById(R.id.coordinatorlayout_detail);


        FloatingActionButton fab = findViewById(R.id.fab_comments_detail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mainlayout, "Comentario", Snackbar.LENGTH_LONG).show();
            }
        });

    }


}
