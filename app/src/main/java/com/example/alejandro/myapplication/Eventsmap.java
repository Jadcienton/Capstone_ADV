package com.example.alejandro.myapplication;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class Eventsmap extends FragmentActivity implements OnMapReadyCallback{
    private static final String TAG = "EventMap";
    private GoogleMap mMap;
    public JSONArray addresses = new JSONArray();
    public ArrayList<LatLng> markers = new ArrayList<>();
    JSONArray jsArray;
    JSONObject jsObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventsmap);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        String url =  "http://"+getResources().getString(R.string.url_api)+"/adv/php/Get.php?id=eventosMapa";
        RequestQueue queue = Volley.newRequestQueue(this);
        mMap = googleMap;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jO = new JSONObject(response);
                    JSONArray time = new JSONArray(jO.getString("time"));
                    String timestamp = time.getJSONObject(0).getString("now");
                    jsArray = new JSONArray(jO.getString("data"));
                    Log.d(TAG, "onMapReady: " + markers.size());
                    Log.d(TAG, "onMapReady: " + markers.size());mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    LatLng adv = new LatLng(-29.928119, -71.242348);
                    mMap.addMarker(new MarkerOptions().position(adv).title("Aguas del Valle").snippet("San Joaquín").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    for (int i =0 ; i< jsArray.length() ; i++) {
                        jsObject = jsArray.getJSONObject(i);
                        double qLatC = jsObject.getDouble("latitude_customer");
                        double qLonC = jsObject.getDouble("longitude_customer");
                        String qSE = jsObject.getString("status_event");
                        String qS = jsObject.getString("sisda_event");
                        String qCDE = jsObject.getString("creation_date_event");
                        String qADE = jsObject.getString("arrival_date_event");
                        String qHFD = jsObject.getString("hydraulic_finish_date_event");

                        String qPE = "P"+jsObject.getString("priority_event");
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = format.parse(timestamp);
                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.setTime(date);
                        Calendar now = GregorianCalendar.getInstance();
                        now.setTime(date);


                        Date date2 = format.parse(qCDE);
                        Calendar creationTimeCal = GregorianCalendar.getInstance();

                        Calendar arrivalTimeCal = GregorianCalendar.getInstance();
                        Calendar hydraulicTimeCal = GregorianCalendar.getInstance();

                        creationTimeCal.setTime(date2);
                        if (qADE.equals("null")){
                        }else {
                            Date date3 = format.parse(qADE);
                            arrivalTimeCal.setTime(date3);
                        }
                        if (qHFD.equals("null")){
                        }else {
                            Date date4 = format.parse(qHFD);
                            hydraulicTimeCal.setTime(date4);
                        }

                        long serverTime = now.getTimeInMillis();
                        long creationTimeLimit;
                        long arrivalTimeLimit;
                        long hydraulicTimeLimit;
                        String delay = "";

                        if (qPE.equals("P1")){
                            switch (qSE.toLowerCase()){
                                case "en camino":
                                    creationTimeCal.add(Calendar.HOUR,1);
                                    creationTimeLimit = creationTimeCal.getTimeInMillis();
                                    if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>2400){
                                        delay = "A Tiempo";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=2400 && TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>0){
                                        delay = "A Tiempo A"; //amarillo
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=0 && TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>-3600){
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=-3600 && TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>-86400) {
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=-86400){
                                        delay = "Retrasado";
                                    }

                                    break;
                                case "ejecucion":
                                    arrivalTimeCal.add(Calendar.HOUR,6);
                                    arrivalTimeLimit = arrivalTimeCal.getTimeInMillis();
                                    if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>3600){
                                        delay = "A Tiempo";
                                    } else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=3600 && TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>0){
                                        delay = "A Tiempo A";//Amarillo
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=0 && TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>-3600){
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=-3600 && TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>-86400) {
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=-86400){
                                        delay = "Retrasado";
                                    }
                                    break;
                                case "inspeccion":
                                    arrivalTimeCal.add(Calendar.HOUR,6);
                                    arrivalTimeLimit = arrivalTimeCal.getTimeInMillis();
                                    if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>3600){
                                        delay = "A Tiempo";
                                    } else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=3600 && TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>0){
                                        delay = "A Tiempo A";//Amarillo
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=0 && TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>-3600){
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=-3600 && TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>-86400) {
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=-86400){
                                        delay = "Retrasado";
                                    }
                                    break;
                                case "pavimento":
                                    hydraulicTimeCal.add(Calendar.DAY_OF_MONTH,6);
                                    hydraulicTimeLimit = hydraulicTimeCal.getTimeInMillis();
                                    if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>86400){
                                        delay = "A Tiempo";
                                    } else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=86400 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>3600){
                                        delay = "A Tiempo A";
                                    } else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=3600 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>0){
                                        delay = "A Tiempo A";//Amarillo
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=0 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>-3600){
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=-3600 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>-86400) {
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=-86400){
                                        delay = "Retrasado";
                                    }
                                    break;
                            }
                        }else if(qPE.equals("P2")){
                            if (qSE.equalsIgnoreCase("en camino") || qSE.equalsIgnoreCase("inspeccion") || qSE.equalsIgnoreCase("ejecucion")){
                                creationTimeCal.add(Calendar.HOUR,12);
                                creationTimeLimit = creationTimeCal.getTimeInMillis();
                                if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>3600){
                                    delay = "A Tiempo";
                                } else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=3600 && TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>0){
                                    delay = "A Tiempo A";//Amarillo
                                }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=0 && TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>-3600){
                                    delay = "Retrasado";
                                }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=-3600 && TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>-86400) {
                                    delay = "Retrasado";
                                }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=-86400){
                                    delay = "Retrasado";
                                }
                            }else if (qSE.equalsIgnoreCase("pavimento")){
                                Log.d(TAG, "onResponse: pavimento");
                                hydraulicTimeCal.add(Calendar.DAY_OF_MONTH,6);
                                hydraulicTimeLimit = hydraulicTimeCal.getTimeInMillis();
                                if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>86400){
                                    delay = "A Tiempo";
                                } else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=86400 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>3600){
                                    delay = "A Tiempo A";
                                } else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=3600 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>0){
                                    delay = "A Tiempo A";//Amarillo
                                }else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=0 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>-3600){
                                    delay = "Retrasado";
                                }else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=-3600 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>-86400) {
                                    delay = "Retrasado";
                                }else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=-86400){
                                    delay = "Retrasado";
                                }
                            }
                        }
                        Log.d(TAG, "onResponse: "+qLatC+" "+qLonC);

                            if (qSE.equalsIgnoreCase("EN CAMINO")) {
                                if (delay.equalsIgnoreCase("a tiempo")) {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(qLatC, qLonC)).title("SISDA: " + qS).snippet("En Camino").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_en_camino_a_tiempo))));
                                }else if (delay.equalsIgnoreCase("a tiempo a")){
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(qLatC, qLonC)).title("SISDA: " + qS).snippet("En Camino").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_en_camino_a_tiempo_amarillo))));
                                }else if (delay.equalsIgnoreCase("retrasado")){
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(qLatC, qLonC)).title("SISDA: " + qS).snippet("En Camino").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_en_camino_retrasado))));
                                }
                            }
                            if (qSE.equalsIgnoreCase("EJECUCION")) {
                                if (delay.equalsIgnoreCase("a tiempo")) {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(qLatC,qLonC)).title("SISDA: " + qS).snippet("En Ejecución").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_ejecucion_a_tiempo))));
                                }else if (delay.equalsIgnoreCase("a tiempo a")){
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(qLatC,qLonC)).title("SISDA: " + qS).snippet("En Ejecución").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_ejecucion_amarillo))));
                                }else if (delay.equalsIgnoreCase("retrasado")){
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(qLatC,qLonC)).title("SISDA: " + qS).snippet("En Ejecución").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_ejecucion_retrasado))));
                                }
                            }
                            if (qSE.equalsIgnoreCase("INSPECCION")) {
                                if (delay.equalsIgnoreCase("a tiempo")) {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(qLatC,qLonC)).title("SISDA: " + qS).snippet("En Inspección").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_inspeccion_a_tiempo))));
                                }else if (delay.equalsIgnoreCase("a tiempo a")){
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(qLatC,qLonC)).title("SISDA: " + qS).snippet("En Inspección").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_inspeccion_amarillo))));
                                }else if (delay.equalsIgnoreCase("retrasado")){
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(qLatC,qLonC)).title("SISDA: " + qS).snippet("En Inspección").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_inspeccion_retrasado))));
                                }
                            }
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adv, 12));
                }catch (JSONException e)
                {
                    Toast.makeText(Eventsmap.this, "Catch", Toast.LENGTH_SHORT).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }
    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    public void sisdaQuery(String url){
        Log.d(TAG, "LA URL ES: " + url);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try{
                            addresses=response;
                            for(int i=0;i<response.length();i++) {
                                // Get current json object
                                JSONObject sisda = addresses.getJSONObject(i);
                                LatLng latlng = new LatLng(sisda.getDouble("6"), sisda.getDouble("7"));
                                markers.add(latlng);
                                Log.d(TAG, "LA URL ES: " +sisda.getString("state_event"));
                                switch (sisda.getString("state_event")) {
                                    case "En Camino":
                                        mMap.addMarker(new MarkerOptions().position(markers.get(i)).title("SISDA: " + sisda.getString("sisda_event") + "  P" + sisda.getInt("priority_event")).snippet(sisda.getString("status_event")).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_en_camino_a_tiempo))));
                                        break;
                                    case "Ejecución":
                                        mMap.addMarker(new MarkerOptions().position(markers.get(i)).title("SISDA: " + sisda.getString("sisda_event") + "  P" + sisda.getInt("priority_event")).snippet(sisda.getString("status_event")).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_ejecucion_a_tiempo))));
                                        break;
                                    case "En Inspección":
                                        mMap.addMarker(new MarkerOptions().position(markers.get(i)).title("SISDA: " + sisda.getString("sisda_event") + "  P" + sisda.getInt("priority_event")).snippet(sisda.getString("status_event")).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_inspeccion_a_tiempo))));
                                        break;
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                    }
                }
        );
        requestQueue.add(jsonArrayRequest);

    }
}