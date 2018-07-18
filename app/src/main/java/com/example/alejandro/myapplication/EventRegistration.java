package com.example.alejandro.myapplication;



import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


public class EventRegistration extends Activity implements OnMapReadyCallback{

    private static final String TAG = "EventRegistration";
    private TextView timeRemaining;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 600000;
    private boolean timerRunning;
    private MapFragment mapFragment;
    private Marker mCenterMarker;

    //variables camara
    private ImageButton beginningCameraB,hydraulicCameraB,pavementCameraB;
    private static final String mainFile = "MisImagenesADV/";
    private static final String imgFile = "Finalizaciones";
    private static final String dirFile = mainFile + imgFile;
    private static final int Camera_Select = 20;
    private ImageView picBeginning,picHydraulic,picPavement;
    private static File file;
    private static File fileImg;
    private String path;
    Bitmap bitmap;
    private static final int pictureBeginning = 122;
    private static final int pictureHydraulic = 121;
    private static final int picturePavement = 120;
    private ContentValues values;
    private Uri imageUri;
    private String imageurl;

    // Variables Latitud y Longitud
    TextView longitude, latitude;
    double longitudeGPS, latitudeGPS;


    //Variable guardar foto

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;
    Button arrivalB, beginningB,hydraulicB,pavementB;
    //VARIABLES PUSH
    private TextView codeSisda,dateCreation,dateArrival,dateBeg,dateHydraulic,datePavement,arrivalUser,beginningUser,hydraulicUser,pavementUser;
    private TextView level,object,fault,priority, status;
    String arrivalUserSet;
    //variables cargar información
    public String statusSisda, numberSisda,statusSynergia,ad,bd,hd,pd,cd;
    public Sisda sisdaData;
    public RelativeLayout pavementQuestion,pavementLayout,finishLayout;
    public RadioButton yesRadio,noRadio;
    public RadioGroup radioGroup;
    public boolean displayQuestionPavement = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration);
        beginningCameraB = (ImageButton) findViewById(R.id.bt_pic_beginning);
        hydraulicCameraB = (ImageButton) findViewById(R.id.bt_pic_hydraulic);
        pavementCameraB= (ImageButton) findViewById(R.id.bt_pic_pavement);
        picBeginning = (ImageView) findViewById(R.id.img_beginning);
        picHydraulic = (ImageView) findViewById(R.id.img_hydraulic);
        picPavement = (ImageView) findViewById(R.id.img_pavement);
//      longitude = (TextView) findViewById(R.id.level_registration);
//      latitude = (TextView) findViewById(R.id.object_registration);


        // PUSH
        codeSisda = findViewById(R.id.sisda_detail);
        dateCreation= findViewById(R.id.creation_date_detail);
        dateArrival = findViewById(R.id.arrival_date_detail);
        dateBeg = findViewById(R.id.beginning_date_detail);
        dateHydraulic = findViewById(R.id.hydraulic_date_detail);
        datePavement = findViewById(R.id.pavement_date_detail);

        level = findViewById(R.id.level_registration);
        object = findViewById(R.id.object_registration);
        fault = findViewById(R.id.fault_registration);

        arrivalUser = findViewById(R.id.arrival_date_name_detail);
        beginningUser= findViewById(R.id.beginning_date_name_detail);
        hydraulicUser= findViewById(R.id.hydraulic_date_name_detail);
        pavementUser= findViewById(R.id.pavement_date_name_detail);

        priority= findViewById(R.id.sisda_priority);
        status= findViewById(R.id.sisda_status);

        Gson gson = new Gson();
        Bundle mibundle = this.getIntent().getExtras();
        String sisda = mibundle.getString("detailSisda");
        sisdaData = gson.fromJson(sisda,Sisda.class);

        codeSisda.setText(sisdaData.getSisda());
        dateCreation.setText(sisdaData.getDateCreation());//sisdaData.getDateCreation
        dateArrival.setText(EventDetail.dateConvert(sisdaData.getDateArrival()));
        dateBeg.setText(EventDetail.dateConvert(sisdaData.getDateBeginning()));
        dateHydraulic.setText(EventDetail.dateConvert(sisdaData.getDateHydraulic()));
        datePavement.setText(EventDetail.dateConvert(sisdaData.getDatePavement()));

        level.setText(EventDetail.nullValue(sisdaData.getLevel()));
        object.setText(EventDetail.nullValue(sisdaData.getObject()));
        fault.setText(EventDetail.nullValue(sisdaData.getFault()));

        arrivalUser.setText(EventDetail.nullValue(sisdaData.getArrivalUser()));
        beginningUser.setText(EventDetail.nullValue(sisdaData.getBeginningUser()));
        hydraulicUser.setText(EventDetail.nullValue(sisdaData.getHydraulicUser()));
        pavementUser.setText(EventDetail.nullValue(sisdaData.getPavementUser()));

        priority.setText(EventDetail.nullValue(sisdaData.getPriority()));
        status.setText(EventDetail.nullValue(sisdaData.getStatus()));

        arrivalUserSet=sisdaData.getArrivalUser().toString();
        statusSisda= sisdaData.getStatus().toString();
        numberSisda= sisdaData.getSisda().toString();
        statusSynergia=sisdaData.getSynergiaStatus().toString();
        cd= sisdaData.getDateCreation().toString();
        ad =sisdaData.getDateArrival().toString();
        bd =sisdaData.getDateBeginning().toString();
        hd =sisdaData.getDateHydraulic().toString();
        pd =sisdaData.getDatePavement().toString();

        //FIN

        // BLOQUEAR Y DESBLOQUEAR BOTONES
        arrivalB = (Button) findViewById(R.id.bt_arrival) ;
        beginningB = (Button) findViewById(R.id.bt_beginning);
        hydraulicB = (Button) findViewById(R.id.bt_hydraulic);
        pavementB =  findViewById(R.id. bt_pavement);
        pavementQuestion= findViewById(R.id.pavement_question_layout) ;
        pavementLayout= findViewById(R.id.pavement_layout);
        finishLayout=findViewById(R.id.finish_layout);
        yesRadio=findViewById(R.id.pavement_yes);
        noRadio=findViewById(R.id.pavement_no);
        radioGroup=findViewById(R.id.radio_group);

        if(cd.toString().trim().equalsIgnoreCase("null"))
        {
            arrivalB.setEnabled(false);
            beginningB.setEnabled(false);
            beginningCameraB.setEnabled(false);
            hydraulicB.setEnabled(false);
            hydraulicCameraB.setEnabled(false);
            pavementB.setEnabled(false);
            pavementCameraB.setEnabled(false);
        } else if (ad.toString().trim().equalsIgnoreCase("null") && !cd.toString().trim().equalsIgnoreCase("null")  )
        {
            arrivalB.setEnabled(true);
            beginningB.setEnabled(false);
            hydraulicB.setEnabled(false);
            pavementB.setEnabled(false);
        } else if(bd.toString().trim().equalsIgnoreCase("null") && !ad.toString().trim().equalsIgnoreCase("null") )
        {
            arrivalB.setEnabled(false);
            beginningB.setEnabled(true);
            beginningCameraB.setEnabled(true);
            hydraulicB.setEnabled(false);
            hydraulicCameraB.setEnabled(false);
            pavementB.setEnabled(false);
            pavementCameraB.setEnabled(false);
        }else if(hd.toString().trim().equalsIgnoreCase("null") && !bd.toString().trim().equalsIgnoreCase("null") )
        {
            arrivalB.setEnabled(false);
            beginningB.setEnabled(false);
            beginningCameraB.setEnabled(true);
            hydraulicB.setEnabled(true);
            hydraulicCameraB.setEnabled(true);
            pavementB.setEnabled(false);
            pavementCameraB.setEnabled(false);

        }else if(pd.toString().trim().equalsIgnoreCase("null") && !hd.toString().trim().equalsIgnoreCase("null"))
        {
            arrivalB.setEnabled(false);
            beginningB.setEnabled(false);
            beginningCameraB.setEnabled(false);
            hydraulicB.setEnabled(false);
            hydraulicCameraB.setEnabled(false);
            pavementB.setEnabled(true);
            pavementCameraB.setEnabled(true);
        }

        displayQuestionPavement = Boolean.valueOf(sisdaData.getPavementRequired().toString());
        if(displayQuestionPavement== true && pd.toString().trim().equalsIgnoreCase("null") ) {
            pavementQuestion.setVisibility(View.VISIBLE);
            pavementB.setEnabled(true);
            changedOption();
        }
        if(displayQuestionPavement== true && !pd.toString().trim().equalsIgnoreCase("null") ) {
            pavementQuestion.setVisibility(View.VISIBLE);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(yesRadio.isChecked()==true)
                    {
                        pavementLayout.setVisibility(View.VISIBLE);
                        pavementB.setEnabled(false);
                        finishLayout.setVisibility(View.VISIBLE);
                    }else
                    {
                        finishLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Verifica permisos para Android 6.0+
            if (!checkExternalStoragePermission()) {
                return;
            }
        }
        beginningCameraB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, pictureBeginning);
            }
        });
        hydraulicCameraB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, pictureHydraulic);
            }
        });
        pavementCameraB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, picturePavement);
            }
        });



               /*values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, PICTURE_RESULT);*/


        //PIXELADO
               /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 9999);
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyCameraImages");
                imagesFolder.mkdirs();
                File image = new File(imagesFolder, "image.jpg");
                Uri uriSavedImage = Uri.fromFile(image);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);*/


//FIN CAMARA

//MAPA
        mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_registration));
        mapFragment.getMapAsync(this);
//FIN MAPA

//TIMER
        time("http://192.168.43.7/adv/time.php");
        timeRemaining = findViewById(R.id.time_remaining_reg);
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
//FIN TIMER

//Arrival

        ActivityCompat.requestPermissions(EventRegistration.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        arrivalB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPStracker g = new GPStracker(getApplicationContext());
                Location location =g.getLocation();
                if(location != null)
                {
                    longitudeGPS= location.getLongitude();
                    latitudeGPS = location.getLatitude();
                    String Name = Preferences.getPreferenceStringName(EventRegistration.this,Preferences.usuarioLogin);
                    String Rut = Preferences.getPreferenceStringRut(EventRegistration.this,Preferences.usuarioRut);
                    updateStatusEvent(String.valueOf(latitudeGPS),String.valueOf(longitudeGPS),Rut,Name);

                }
            }
        });

// BEGINNING
        beginningB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPStracker g = new GPStracker(getApplicationContext());
                Location location = g.getLocation();
                if (location != null) {
                    longitudeGPS = location.getLongitude();
                    latitudeGPS = location.getLatitude();
                    String Name = Preferences.getPreferenceStringName(EventRegistration.this, Preferences.usuarioLogin);
                    String Rut = Preferences.getPreferenceStringRut(EventRegistration.this, Preferences.usuarioRut);
                    if (picBeginning.getDrawable() != null) {
                        updateStatusEvent(String.valueOf(latitudeGPS), String.valueOf(longitudeGPS), Rut, Name);
                    }else{
                        Toast.makeText(EventRegistration.this, "Por favor tome fotografía", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
// Hydraulic
        hydraulicB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPStracker g = new GPStracker(getApplicationContext());
                Location location =g.getLocation();
                if(location != null)
                {
                    longitudeGPS= location.getLongitude();
                    latitudeGPS = location.getLatitude();
                    String Name = Preferences.getPreferenceStringName(EventRegistration.this,Preferences.usuarioLogin);
                    String Rut = Preferences.getPreferenceStringRut(EventRegistration.this,Preferences.usuarioRut);
                    if(picHydraulic.getDrawable() != null) {
                        updateStatusEvent(String.valueOf(latitudeGPS), String.valueOf(longitudeGPS), Rut, Name);
                    }else{
                        Toast.makeText(EventRegistration.this, "Por favor tome fotografía", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

      /*   radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
         public void onCheckedChanged(RadioGroup group, int checkedId) {
             if(yesRadio.isChecked()==true)
            {
               pavementLayout.setVisibility(View.VISIBLE);
                finishLayout.setVisibility(View.GONE);
            }else
                {
            pavementLayout.setVisibility(View.GONE);
            finishLayout.setVisibility(View.VISIBLE);
        }
    }
});*/

//PAVEMENT
        pavementB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPStracker g = new GPStracker(getApplicationContext());
                Location location =g.getLocation();
                if(location != null)
                {
                    longitudeGPS= location.getLongitude();
                    latitudeGPS = location.getLatitude();
                    String Name = Preferences.getPreferenceStringName(EventRegistration.this,Preferences.usuarioLogin);
                    String Rut = Preferences.getPreferenceStringRut(EventRegistration.this,Preferences.usuarioRut);
                    if(picPavement.getDrawable() != null) {
                        updateStatusEvent(String.valueOf(latitudeGPS), String.valueOf(longitudeGPS), Rut, Name);
                    }else{
                        Toast.makeText(EventRegistration.this, "Por favor tome fotografía", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void abrirCamara() {
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "MyPicture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PICTURE_RESULT);*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case pictureBeginning:
                if (requestCode == pictureBeginning)
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            bitmap = redimensionarImagen(bitmap, 600, 800);
                            picBeginning.setImageBitmap(bitmap);
                            //Obtiene la ruta donde se encuentra guardada la imagen.
                            imageurl = getRealPathFromURI(imageUri);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            case pictureHydraulic:
                if (requestCode == pictureHydraulic)
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            bitmap = redimensionarImagen(bitmap, 600, 800);
                            picHydraulic.setImageBitmap(bitmap);
                            //Obtiene la ruta donde se encuentra guardada la imagen.
                            imageurl = getRealPathFromURI(imageUri);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            case picturePavement:
                if (requestCode == picturePavement)
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            bitmap = redimensionarImagen(bitmap, 600, 800);
                            picPavement.setImageBitmap(bitmap);
                            //Obtiene la ruta donde se encuentra guardada la imagen.
                            imageurl = getRealPathFromURI(imageUri);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
        }
    }

        /*super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Camera_Select:
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("Path: ", path);
                    }
                });
                bitmap = BitmapFactory.decodeFile(path);
                picArrival.setImageBitmap(bitmap);

                break;
        }
        bitmap = redimensionarImagen(bitmap,600,800);*/

    // PIXELADO
       /* if (requestCode == 9999 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            fotito.setImageBitmap(photo);
        }*/


    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto= altoNuevo/alto;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{
            return bitmap;
        }


    }

    public void changedOption()
    {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(yesRadio.isChecked()==true)
                {
                    pavementLayout.setVisibility(View.VISIBLE);
                    pavementB.setEnabled(true);
                    finishLayout.setVisibility(View.GONE);
                }else
                {
                    pavementLayout.setVisibility(View.GONE);
                    finishLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void updateStatusEvent(final String latitudeS, final String longitudeS, final String rut,final String name)  {
        String url =  "http://192.168.43.7/adv/php/Post.php";
        RequestQueue request = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.trim().equals("true")) {
                    //PAVEMENT
                    if (!arrivalUser.getText().toString().equalsIgnoreCase("-") && !beginningUser.getText().toString().equalsIgnoreCase("-") && !hydraulicUser.getText().toString().equalsIgnoreCase("-") && pavementUser.getText().toString().equalsIgnoreCase("-")) {
                        pavementQuestion.setVisibility(View.VISIBLE);
                        displayQuestionPavement=true;
                        sisdaData.setStatus("Finalizado");
                        status.setText("Finalizado");
                        pavementUser.setText(name);
                        pavementB.setEnabled(false);
                        noRadio.setEnabled(false);
                        finishLayout.setVisibility(View.VISIBLE);
                    }
                    //HYDRAULIC
                    if (!arrivalUser.getText().toString().equalsIgnoreCase("-") && !beginningUser.getText().toString().equalsIgnoreCase("-") && hydraulicUser.getText().toString().equalsIgnoreCase("-")) {
                        if (sisdaData.getStatus().equals("Ejecución") || sisdaData.getStatus().equals("Inspección")) {
                            pavementQuestion.setVisibility(View.VISIBLE);
                            displayQuestionPavement=true;
                            sisdaData.setStatus("Pavimento");
                            status.setText("Pavimento");
                        }
                        hydraulicUser.setText(name);
                        hydraulicB.setEnabled(false);
                        pavementB.setEnabled(true);
                        changedOption();
                    }
                    //BEGINNING
                    if (!arrivalUser.getText().toString().equalsIgnoreCase("-") && beginningUser.getText().toString().equalsIgnoreCase("-")/*sisdaData.getStatus().equals("Ejecución") || sisdaData.getStatus().equals("Inspección")*/) {
                        if (sisdaData.getStatus().equals("Ejecución")) {
                            sisdaData.setStatus("Ejecución");
                            status.setText("Ejecución");
                        } else {
                            sisdaData.setStatus("Inspección");
                            status.setText("Inspección");
                        }

                        beginningUser.setText(name);
                        beginningB.setEnabled(false);
                        hydraulicB.setEnabled(true);
                    }
                    //ARRIVAL
                    if (arrivalUser.getText().toString().equalsIgnoreCase("-")/*sisdaData.getStatus().equals("En camino")*/) {
                        if (sisdaData.getSynergiaStatus().equals("EJECUCION")) {
                            sisdaData.setStatus("Ejecución");
                            status.setText("Ejecución");
                        } else {
                            sisdaData.setStatus("Inspección");
                            status.setText("Inspección");
                        }

                        arrivalUser.setText(name);
                        arrivalB.setEnabled(false);
                        beginningB.setEnabled(true);
                    }

                    Toast.makeText(EventRegistration.this, "registro exitoso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EventRegistration.this, "No se pudo registrar", Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EventRegistration.this, "Error en los servidores", Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // String sisdaBD = sisda;
                //String statusSisdaBD = statusSisda;
                String longitudeDB = latitudeS;
                String latitudeDB = longitudeS;
                String rutPersonDB = rut;
                // String statusSynergiaBD= statusSynegia;

                //  String dateBD= "NOW()";

                Map<String, String> params = new HashMap<String, String>();
                Map<String,String> parametros = new HashMap<>();
                if(arrivalUser.getText().toString().equalsIgnoreCase("-")/*sisdaData.getStatus().equals("En camino")*/)
                {
                    try{
                        parametros.put("sisda_event",sisdaData.getSisda());
                        if(sisdaData.getSynergiaStatus().equals("INSPECCION")) {
                            parametros.put("status_event", "INSPECCION");
                        }else if (sisdaData.getSynergiaStatus().equals("EJECUCION")){
                            parametros.put("status_event", "EJECUCION");
                        }
                        //parametros.put("arrival_pic_event",imagenBD);
                        parametros.put("latitude_event",latitudeDB);
                        parametros.put("longitude_event",longitudeDB);
                        parametros.put("fk_arrival_rut_user_event",rutPersonDB);
                        parametros.put("arrival_date_event","NOW()");
                        // parametros.put("type","a");
                        JSONObject userJSON = new JSONObject(parametros);
                        params.put("id", "eventRegister");
                        params.put("data", userJSON.toString());
                        Log.d(TAG, "getParams: "+userJSON.toString());
                    } catch (Exception e) {
                        Toast.makeText(EventRegistration.this, "Ocurrío un problema, por favor inténtelo nuevamente", Toast.LENGTH_SHORT).show();
                    }
                }
                //BEGINNING
                if( !arrivalUser.getText().toString().equalsIgnoreCase("-") && beginningUser.getText().toString().equalsIgnoreCase("-")/*sisdaData.getStatus().equals("Inspección") || sisdaData.getStatus().equals("Ejecución")*/ )
                {
                    try{
                        String imagenBeginnig = StringConvert(bitmap);

                        parametros.put("sisda_event",sisdaData.getSisda());
                        parametros.put("arrival_pic_event",imagenBeginnig);
                        parametros.put("lat_beginning_event",latitudeDB);
                        parametros.put("lng_beginning_event",longitudeDB);
                        parametros.put("fk_beginning_rut_user_event",rutPersonDB);
                        parametros.put("beginning_job_date_event","NOW()");

                        JSONObject userJSON = new JSONObject(parametros);
                        params.put("id", "eventRegister");
                        params.put("data", userJSON.toString());
                        Log.d(TAG, "getParams: "+userJSON.toString());
                    } catch (Exception e) {
                        Toast.makeText(EventRegistration.this, "Ocurrío un problema, por favor inténtelo nuevamente", Toast.LENGTH_SHORT).show();
                    }
                }
                //HYDRAULIC
                if(!arrivalUser.getText().toString().equalsIgnoreCase("-") && !beginningUser.getText().toString().equalsIgnoreCase("-") && hydraulicUser.getText().toString().equalsIgnoreCase("-")/*sisdaData.getStatus().equals("Inspección") || sisdaData.getStatus().equals("Ejecución")*/ )
                {

                    try {
                        String imagenHydraulic = StringConvert(bitmap);

                        parametros.put("sisda_event",sisdaData.getSisda());
                        parametros.put("hydraulic_pic_event",imagenHydraulic);
                        parametros.put("lat_hydraulic_event",latitudeDB);
                        parametros.put("lng_hydraulic_event",longitudeDB);
                        parametros.put("status_event", "PAVIMENTO");
                        parametros.put("fk_hydraulic_rut_user_event",rutPersonDB);
                        parametros.put("fk_definitive_rut_user_event",rutPersonDB);
                        parametros.put("hydraulic_finish_date_event","NOW()");
                        parametros.put("definitive_date_event","NOW()");
                        parametros.put("pavement_required_event","true");

                        JSONObject userJSON = new JSONObject(parametros);
                        params.put("id", "eventRegister");
                        params.put("data", userJSON.toString());
                        Log.d(TAG, "getParams: "+userJSON.toString());
                    } catch (Exception e) {
                        Toast.makeText(EventRegistration.this, "Ocurrío un problema, por favor inténtelo nuevamente", Toast.LENGTH_SHORT).show();
                    }

                }
                //PAVEMENT
                if(!arrivalUser.getText().toString().equalsIgnoreCase("-") && !beginningUser.getText().toString().equalsIgnoreCase("-") && !hydraulicUser.getText().toString().equalsIgnoreCase("-") && pavementUser.getText().toString().equalsIgnoreCase("-") /*sisdaData.getStatus().equals("Inspección") || sisdaData.getStatus().equals("Ejecución")*/ )
                {
                    try {
                        String imagenPavement = StringConvert(bitmap);


                        parametros.put("sisda_event",sisdaData.getSisda());
                        parametros.put("pavement_pic_event",imagenPavement);
                        parametros.put("lat_pavement_event",latitudeDB);
                        parametros.put("lng_pavement_event",longitudeDB);
                        parametros.put("status_event", "FINALIZADO");
                        parametros.put("fk_pavement_rut_user_event",rutPersonDB);
                        parametros.put("pavement_date_event","NOW()");

                        JSONObject userJSON = new JSONObject(parametros);
                        params.put("id", "eventRegister");
                        params.put("data", userJSON.toString());
                        Log.d(TAG, "getParams: "+userJSON.toString());
                    } catch (Exception e) {
                        Toast.makeText(EventRegistration.this, "Ocurrío un problema, por favor inténtelo nuevamente", Toast.LENGTH_SHORT).show();
                    }
                }
                return params;
            }
        };
        request.add(stringRequest);
    }




    private String StringConvert(Bitmap bitmap) {

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte= array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);
        return imagenString;
    }


    private boolean checkExternalStoragePermission()  {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para leer!");
            return true;
        }

        return false;
    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
        Gson gson = new Gson();
        Bundle mibundle = this.getIntent().getExtras();
        String sisda = mibundle.getString("detailSisda");
        sisdaData = gson.fromJson(sisda,Sisda.class);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng adv = new LatLng(-29.960729 ,-71.293332);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adv,18));
        googleMap.addMarker(new MarkerOptions().position(adv).anchor(0.5f, .05f).title("Aguas del Valle").snippet("San joaquín"));
        if(!sisdaData.getLngArrival().equalsIgnoreCase("null") && !sisdaData.getLatArrival().equalsIgnoreCase("null"))
        {
            double lngA= Double.valueOf(sisdaData.getLngArrival());
            double latA= Double.valueOf(sisdaData.getLatArrival());
            LatLng arrivalLatLng = new LatLng(lngA,latA);
            googleMap.addMarker(new MarkerOptions().position(arrivalLatLng).anchor(0.5f, .05f).title("Llegada a Terreno").snippet(sisdaData.getArrivalUser().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        }if(!sisdaData.getLngBeginning().equalsIgnoreCase("null") && !sisdaData.getLatBeginning().equalsIgnoreCase("null"))
        {
            double lngB= Double.valueOf(sisdaData.getLngBeginning());
            double latB= Double.valueOf(sisdaData.getLatBeginning());
            LatLng arrivalLatLng = new LatLng(lngB,latB);
            googleMap.addMarker(new MarkerOptions().position(arrivalLatLng).anchor(0.5f, .05f).title("Inicio de Faenas").snippet(sisdaData.getArrivalUser().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        if(!sisdaData.getLngHydraulic().equalsIgnoreCase("null") && !sisdaData.getLatHydraulic().equalsIgnoreCase("null"))
        {
            double lngH= Double.valueOf(sisdaData.getLngHydraulic());
            double latH= Double.valueOf(sisdaData.getLatHydraulic());
            LatLng arrivalLatLng = new LatLng(lngH,latH);
            googleMap.addMarker(new MarkerOptions().position(arrivalLatLng).anchor(0.5f, .05f).title("Finalización hidráulica").snippet(sisdaData.getArrivalUser().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
        if(!sisdaData.getLngPavement().equalsIgnoreCase("null") && !sisdaData.getLatPavement().equalsIgnoreCase("null"))
        {
            double lngP= Double.valueOf(sisdaData.getLngPavement());
            double latP= Double.valueOf(sisdaData.getLatPavement());
            LatLng arrivalLatLng = new LatLng(lngP,latP);
            googleMap.addMarker(new MarkerOptions().position(arrivalLatLng).anchor(0.5f, .05f).title("Fin Pavimento").snippet(sisdaData.getArrivalUser().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
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
