package com.example.alejandro.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import static com.android.volley.VolleyLog.TAG;

public class PruebaFoto extends Activity {
    private ImageView foto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_foto);
        foto = findViewById(R.id.tofo);
        obtenerFoto("");

    }
    private void  obtenerFoto(String URL){
        Log.d(TAG, "pushSisda: "+URL);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jO= new JSONObject(response);
                    JSONArray time = new JSONArray(jO.getString("time"));
                    String timestamp = time.getJSONObject(0).getString("now");
                }
                catch (JSONException e)
                {
                    Toast.makeText(PruebaFoto.this, "catch", Toast.LENGTH_SHORT).show();
                }
            }} , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PruebaFoto.this, "No se puede conectar "+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}
