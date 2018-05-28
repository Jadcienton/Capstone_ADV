package com.example.alejandro.myapplication;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

public class Main2Activity extends Activity implements DailyEventFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
