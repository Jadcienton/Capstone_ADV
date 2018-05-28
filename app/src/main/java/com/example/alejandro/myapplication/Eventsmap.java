package com.example.alejandro.myapplication;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesUtil;
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

import java.util.ArrayList;

public class Eventsmap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng adv = new LatLng(-29.928119,-71.242348);
        LatLng casa = new LatLng(-29.916580,-71.255452);
        ArrayList<LatLng> markers = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            markers.add(new LatLng(-29.916580+(1/1+i),-71.255452+(1/1+i)));
            mMap.addMarker(new MarkerOptions().position(markers.get(i)).title("Aguas del valle").snippet("hola").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_ejecucion_a_tiempo))));
        }


        /*//mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(adv).title("Aguas del valle").snippet("hola").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_ejecucion_a_tiempo))));
        mMap.addMarker(new MarkerOptions().position(casa).title("Sisda: 135135, Ejecución").snippet("hola").icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_en_camino_a_tiempo))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adv,12));*/

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
}
