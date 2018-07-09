package com.example.alejandro.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventRegistration extends Activity implements OnMapReadyCallback{

    private static final String TAG = "EventRegistration";
    private TextView timeRemaining;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 600000;
    private boolean timerRunning;
    private MapFragment mapFragment;
    private Marker mCenterMarker;

    //variables camara
    private ImageButton arrivalButton;
    private static final String mainFile = "MisImagenesADV/";
    private static final String imgFile = "Finalizaciones";
    private static final String dirFile = mainFile + imgFile;
    private ImageView fotito;
    private static  File file;
    private static  File fileImg;
    private String path;
    Bitmap bitmap;

    private static final int codeSelection = 10;
    private static final int codePhoto = 20;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration);
        arrivalButton = (ImageButton) findViewById(R.id.bt_pic_beginning);
        fotito= (ImageView) findViewById(R.id.img_beginning);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            arrivalButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        //CAMARA
        arrivalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 9999);
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyCameraImages");
                imagesFolder.mkdirs();
                File image = new File(imagesFolder, "image.jpg");
                Uri uriSavedImage = Uri.fromFile(image);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                //cameraOpen();
            }
        });







        mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_registration));
        mapFragment.getMapAsync(this);


        time("http://192.168.43.7/adv/time.php");
        timeRemaining = findViewById(R.id.time_remaining_reg);
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                arrivalButton.setEnabled(true);
            }
        }
    }


/*    private void cameraOpen() {
        File myFile =new File(Environment.getExternalStorageDirectory(),dirFile);
        boolean isCreate = myFile.exists();

        if (isCreate == false )
        {
            isCreate= myFile.mkdirs();
            Toast.makeText(getApplicationContext(), "False", Toast.LENGTH_SHORT).show();
        }
        if (isCreate == true )
        {
        Long consecutive = System.currentTimeMillis()/1000;
        String name =  consecutive.toString()+".jpg";

        path = Environment.getExternalStorageDirectory()+
                File.separator+dirFile+File.separator+name; //ruta de almacenamiento
        fileImg=new File(path);

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImg));
        startActivityForResult(intent,codePhoto);
        }

    }*/


/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case codeSelection:
                Uri miPath = data.getData();
                fotito.setImageURI(miPath);
                break;
            case codePhoto:
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path", "" + path);
                            }
                        });
                bitmap = BitmapFactory.decodeFile(path);
                fotito.setImageBitmap(bitmap);
                break;
        }
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9999 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            fotito.setImageBitmap(photo);
        }
    }


    public void updateTimer(){
        int minutes = (int) timeLeftInMilliseconds/60000;
        int seconds = (int) timeLeftInMilliseconds % 60000/1000;
        String timeLeftText;
        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if(seconds<10) timeLeftText += "0";
        timeLeftText += seconds;
        timeRemaining.setText(timeLeftText);
    }

    public void time(String URL){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray =  new JSONArray(response);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSXXX");
                    Log.d(TAG, "onResponse: "+jsonArray.getJSONObject(0).getString("now"));
                    Date date = sdf.parse(jsonArray.getJSONObject(0).getString("now"),new ParsePosition(0));
                    Log.d(TAG, "onResponse: "+date);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error en los servidores", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng adv = new LatLng(-29.928119,-71.242348);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adv,20));
        mCenterMarker = googleMap.addMarker(new MarkerOptions().position(adv).anchor(0.5f, .05f).title("Casa Mila").snippet("ADV"));
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                Intent intent = new Intent(EventRegistration.this,Eventsmap.class);
                startActivity(intent);
            }
        });



    }
}
