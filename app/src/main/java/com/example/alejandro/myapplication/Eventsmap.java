package com.example.alejandro.myapplication;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.util.Log;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class Eventsmap extends FragmentActivity implements OnMapReadyCallback{
    private static final String TAG = "EventMap";
    private GoogleMap mMap;
    public JSONArray addresses = new JSONArray();
    public ArrayList<LatLng> markers = new ArrayList<>();
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
        sisdaQuery("http://192.168.15.35/adv/EventosDiarios.php");
        Log.d(TAG, "onMapReady: "+ markers.size());
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng adv = new LatLng(-29.928119,-71.242348);
        mMap.addMarker(new MarkerOptions().position(new LatLng(-29.9430602,-71.2565423)).title("SISDA: 1254862 P1 ").snippet("En Camino").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_en_camino_a_tiempo))));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-29.9165558,-71.2577062)).title("SISDA: 1254863 P2 ").snippet("En Ejecución").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_ejecucion_a_tiempo))));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-71.2577062,-71.2956232)).title("SISDA: 1254836 P1 ").snippet("En Inspección").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_inspeccion_a_tiempo))));
        mMap.addMarker(new MarkerOptions().position(adv).title("Aguas del Valle").snippet("San Joaquín").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adv,12));


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
