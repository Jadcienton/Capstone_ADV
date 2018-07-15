package com.example.alejandro.myapplication;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EventDetail extends AppCompatActivity {
    private static final String TAG = "Event Detail";
    private CoordinatorLayout mainlayout;
    private TextView codeSisda,dateCreation,dateArrival,dateBeg,dateHydraulic,datePavement;
    private TextView codeCostumer,nameCostumer,Phones;
    private TextView Address,numberAddress,numerDepto,city,corner,Reference;
    private TextView level,object,fault;
    private TextView pipeDiameter, pipeMaterial, sewerDiameter, sewerMaterial, waterMeterQuantity, socialClient, activityCustomer;
    private TextView timeRemaining,remainingDetail;
    private CountDownTimer countDownTimer;
    private RelativeLayout delayDetail;
    private Chronometer chrono;
    long timeLeftInMilliseconds = 0;
    Sisda sisdaData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        toolbar.setTitle("Detalle");
        setSupportActionBar(toolbar);
        timeRemaining = findViewById(R.id.time_remaining);
        remainingDetail = findViewById(R.id.remaining_detail);
        delayDetail = findViewById(R.id.layout_delay_detail);
        chrono = findViewById(R.id.chrono_detail);
        // timeLeftInMilliseconds = ;


        now("http://"+getResources().getString(R.string.url_api)+"/adv/php/Get.php?id=serverTime");



        mainlayout = findViewById(R.id.coordinatorlayout_detail);
        codeSisda = findViewById(R.id.sisda_detail);
        dateCreation= findViewById(R.id.creation_date_detail);
        dateArrival = findViewById(R.id.arrival_date_detail);
        dateBeg = findViewById(R.id.beginning_date_detail);
        dateHydraulic = findViewById(R.id.hydraulic_date_detail);
        datePavement = findViewById(R.id.pavement_date_detail);

        codeCostumer = findViewById(R.id.customer_number_detail);
        nameCostumer = findViewById(R.id.customer_detail);
        Phones = findViewById(R.id.phone_detail);

        Address = findViewById(R.id.address_detail);
        numberAddress = findViewById(R.id.number_detail);
        numerDepto = findViewById(R.id.dep_detail);
        city = findViewById(R.id.city_detail);
        corner = findViewById(R.id.corner_detail);
        Reference = findViewById(R.id.ref_detail);

        level = findViewById(R.id.level_detail);
        object = findViewById(R.id.object_detail);
        fault = findViewById(R.id.fault_detail);

        pipeDiameter = findViewById(R.id.diameter_pipe_detail);
        pipeMaterial = findViewById(R.id.material_pipe_detail);
        sewerDiameter= findViewById(R.id.diameter_sewer_detail);
        sewerMaterial = findViewById(R.id.material_sewer_detail);
        waterMeterQuantity = findViewById(R.id.water_meter_number_detail);
        socialClient = findViewById(R.id.social_customer_detail);
        activityCustomer = findViewById(R.id.activity_detail);


        Gson gson = new Gson();
        Bundle mibundle = this.getIntent().getExtras();
        String sisda = mibundle.getString("detailSisda");
        sisdaData = gson.fromJson(sisda,Sisda.class);
        if (sisdaData.getPriority().equals("P1")){
            if (!sisdaData.getDateHydraulic().equals("null")){
                remainingDetail.setText("PAVIMENTACIÓN");
            } else if (!sisdaData.getDateArrival().equals("null")){
                remainingDetail.setText("FIN. HIDRÁULICA");
            }else {
                remainingDetail.setText("LLEGADA A TERRENO");
            }
        }else if (sisdaData.getPriority().equals("P2")){
            if (!sisdaData.getDateHydraulic().equals("null")){
                remainingDetail.setText("PAVIMENTACIÓN");
            } else{
                remainingDetail.setText("FIN. HIDRÁULICA");
            }
        }
        if (sisdaData.getStatus().equalsIgnoreCase("finalizado"))
            delayDetail.setVisibility(View.GONE);

        codeSisda.setText(sisdaData.getSisda());
        dateCreation.setText(sisdaData.getDateCreation());//sisdaData.getDateCreation
        dateArrival.setText(dateConvert(sisdaData.getDateArrival()));
        dateBeg.setText(dateConvert(sisdaData.getDateBeginning()));
        dateHydraulic.setText(dateConvert(sisdaData.getDateHydraulic()));
        datePavement.setText(dateConvert(sisdaData.getDatePavement()));

        codeCostumer.setText(nullValue(sisdaData.getCodeCustomer()));
        nameCostumer.setText(nullValue(sisdaData.getClientName()));
        Phones.setText(nullValue(sisdaData.getClientPhone())+ " / "+ nullValue(sisdaData.getClientPhoneTwo()));

        Address.setText(nullValue(sisdaData.getAddress()));
        numberAddress.setText(nullValue(sisdaData.getNumber()));
        numerDepto.setText(nullValue(sisdaData.getDepartment()));
        city.setText(nullValue(sisdaData.getCity()));
        corner.setText(nullValue(sisdaData.getCorner()));
        Reference.setText(nullValue(sisdaData.getReference()));

        level.setText(nullValue(sisdaData.getLevel()));
        object.setText(nullValue(sisdaData.getObject()));
        fault.setText(nullValue(sisdaData.getFault()));

        pipeDiameter.setText(nullValue(sisdaData.getPipeDiameter()));
        pipeMaterial.setText(nullValue(sisdaData.getPipeMaterial()));
        sewerDiameter.setText(nullValue(sisdaData.getSewerDiameter()));
        sewerMaterial.setText(nullValue(sisdaData.getSewerMaterial()));
        waterMeterQuantity.setText(nullValue(sisdaData.getWaterMeterQuantity()));
        socialClient.setText(nullValue(sisdaData.getSocialClient()));
        activityCustomer.setText(nullValue(sisdaData.getActivityCustomer()));


        //Toast.makeText(getApplicationContext(), sisda, Toast.LENGTH_LONG).show();
        FloatingActionButton fab = findViewById(R.id.fab_comments_detail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mainlayout, "Comentario", Snackbar.LENGTH_LONG).show();
            }
        });

    }
    public void updateTimer(){
        Calendar timer = GregorianCalendar.getInstance();
        timer.setTimeInMillis(timeLeftInMilliseconds);

        int sec = (int) timeLeftInMilliseconds/1000;
        int day = sec/86400;
        int hour=(sec-(day*86400))/3600;
        int min=(sec-(day*86400)-(3600*hour))/60;
        sec=sec-((hour*3600)+(min*60)+(day*86400));
        String textTime;
        if (day==1)
            textTime = ""+day+" día  "+hour+":";
        else if (day>1)
            textTime = ""+day+" días  "+hour+":";
        else
            textTime = hour+":";
        if (min<10)
            textTime+="0"+min+":";
        else
            textTime+=min+":";
        if (sec<10)
            textTime+="0"+sec;
        else
            textTime+=sec;
        timeRemaining.setText(textTime);
    }

    public static String dateConvert(String Fecha) {
        String realDate = "";
        if (Fecha.equals("")) {
            realDate = Fecha.replace("","-");
        } else {

            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat formatterOut = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date = format.parse(Fecha);
                realDate = formatterOut.format(date).toString();
            } catch (java.text.ParseException e) {
                e.printStackTrace();

            }
        }
        return realDate;
    }

    public static String nullValue(String Value) {
        if (Value.equals("")) {
            Value = Value.replace("", "-");
        }
        else if (Value.equals("Null"))
        {
            Value = Value.replace("Null", "-");
        }
        else if (Value.equals("null"))
        {
            Value = Value.replace("null", "-");
        }else if (Value.equals("Null Null"))
        {
            Value = Value.replace("Null Null", "-");
        }
        return Value;
    }

    public void now(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsArray = new JSONArray(response);
                    JSONObject jsObject = jsArray.getJSONObject(0);
                    String time = jsObject.getString("now");
                    Log.d(TAG, "onResponse: now: "+time);
                    Log.d(TAG, "onResponse: delay: "+sisdaData.getDelay());
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = format.parse(time);
                    Calendar now = GregorianCalendar.getInstance();
                    now.setTime(date);

                    DateFormat format2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                    Date date2 = format2.parse(sisdaData.getDateCreation());
                    Calendar creationTimeCal = GregorianCalendar.getInstance();
                    creationTimeCal.setTime(date2);
                    Calendar arrivalTimeCal = GregorianCalendar.getInstance();
                    Calendar hydraulicTimeCal = GregorianCalendar.getInstance();

                    if (sisdaData.getDateArrival().equals("null")){
                        Log.d(TAG, "onResponse: Arrival Nulo");
                    }else {
                        Date date3 = format.parse(sisdaData.getDateArrival()); //qADE
                        arrivalTimeCal.setTime(date3);
                    }
                    //qHFD
                    if (sisdaData.getDateHydraulic().equals("null")){
                        Log.d(TAG, "onResponse: Hydraulic Nulo");
                    }else {
                        Date date4 = format.parse(sisdaData.getDateHydraulic()); //qADE
                        Log.d(TAG, "onResponse: hyd date: "+sisdaData.getDateHydraulic());
                        hydraulicTimeCal.setTime(date4);
                    }

                    long serverTime = now.getTimeInMillis();
                    long creationTimeLimit;
                    long arrivalTimeLimit;
                    long hydraulicTimeLimit;

                    if(sisdaData.getDelay().toLowerCase().equals("a tiempo a")){
                        delayDetail.setBackgroundColor(getResources().getColor(R.color.progess_caution));
                    }else if(sisdaData.getDelay().toLowerCase().equals("a tiempo")){
                        delayDetail.setBackgroundColor(getResources().getColor(R.color.progress_good));
                    }else if(sisdaData.getDelay().toLowerCase().equals("retrasado")){
                        delayDetail.setBackgroundColor(getResources().getColor(R.color.progress_warning));
                    }

                   if (sisdaData.getPriority().equals("P1")){
                       if (sisdaData.getStatus().toLowerCase().equals("en camino")){
                           creationTimeCal.add(Calendar.HOUR,1);
                           creationTimeLimit = creationTimeCal.getTimeInMillis();
                           if (creationTimeLimit - serverTime > 0) {
                               timeLeftInMilliseconds = creationTimeLimit - serverTime;
                               countDown();
                           }else{
                               timeLeftInMilliseconds = serverTime - creationTimeLimit;
                               delayDetail.setBackgroundColor(getResources().getColor(R.color.progress_warning));
                               chrono.setBase(SystemClock.elapsedRealtime()-timeLeftInMilliseconds);
                               chrono.setVisibility(View.VISIBLE);
                               timeRemaining.setVisibility(View.GONE);
                               chrono.setFormat("-%s");
                               chrono.start();
                           }
                       }else if (sisdaData.getStatus().toLowerCase().equals("ejecución")||sisdaData.getStatus().toLowerCase().equals("inspección")){
                           arrivalTimeCal.add(Calendar.HOUR,6);
                           arrivalTimeLimit = arrivalTimeCal.getTimeInMillis();
                           if (arrivalTimeLimit - serverTime > 0) {
                               timeLeftInMilliseconds = arrivalTimeLimit - serverTime;
                               countDown();
                           }else{
                               timeLeftInMilliseconds = serverTime - arrivalTimeLimit;
                               delayDetail.setBackgroundColor(getResources().getColor(R.color.progress_warning));
                               chrono.setBase(SystemClock.elapsedRealtime()-timeLeftInMilliseconds);
                               chrono.setVisibility(View.VISIBLE);
                               timeRemaining.setVisibility(View.GONE);
                               chrono.setFormat("-%s");
                               chrono.start();
                           }
                       }else if (sisdaData.getStatus().toLowerCase().equals("pavimento")){
                           Log.d(TAG, "onResponse: if pavimento "+hydraulicTimeCal);
                           hydraulicTimeCal.add(Calendar.HOUR,144);
                           Log.d(TAG, "onResponse: add "+hydraulicTimeCal);
                           hydraulicTimeLimit = hydraulicTimeCal.getTimeInMillis();
                           if (hydraulicTimeLimit - serverTime > 0) {
                               Log.d(TAG, "onResponse: if >0");
                               timeLeftInMilliseconds = hydraulicTimeLimit - serverTime;
                               countDown();
                           }else{
                               Log.d(TAG, "onResponse: else "+hydraulicTimeLimit+" server "+serverTime);
                               timeLeftInMilliseconds = serverTime - hydraulicTimeLimit;
                               delayDetail.setBackgroundColor(getResources().getColor(R.color.progress_warning));
                               chrono.setBase(SystemClock.elapsedRealtime()-timeLeftInMilliseconds);
                               chrono.setVisibility(View.VISIBLE);
                               timeRemaining.setVisibility(View.GONE);
                               chrono.setFormat("-%s");
                               chrono.start();
                           }
                       }
                   }else if (sisdaData.getPriority().equals("P2")){
                       if (sisdaData.getStatus().toLowerCase().equals("ejecución")|| sisdaData.getStatus().toLowerCase().equals("inspección") || sisdaData.getStatus().toLowerCase().equals("en camino")){
                           creationTimeCal.add(Calendar.HOUR,12);
                           creationTimeLimit = creationTimeCal.getTimeInMillis();
                           if (creationTimeLimit - serverTime > 0) {
                               timeLeftInMilliseconds = creationTimeLimit - serverTime;
                               countDown();
                           }else{
                               timeLeftInMilliseconds = serverTime - creationTimeLimit;
                               delayDetail.setBackgroundColor(getResources().getColor(R.color.progress_warning));
                               chrono.setBase(SystemClock.elapsedRealtime()-timeLeftInMilliseconds);
                               chrono.setVisibility(View.VISIBLE);
                               timeRemaining.setVisibility(View.GONE);
                               chrono.setFormat("-%s");
                               chrono.start();
                           }
                       }else if (sisdaData.getStatus().toLowerCase().equals("pavimento")){
                           Log.d(TAG, "onResponse: if pavimento "+hydraulicTimeCal);
                           hydraulicTimeCal.add(Calendar.HOUR,144);
                           Log.d(TAG, "onResponse: add "+hydraulicTimeCal);
                           hydraulicTimeLimit = hydraulicTimeCal.getTimeInMillis();
                           if (hydraulicTimeLimit - serverTime > 0) {
                               Log.d(TAG, "onResponse: if >0");
                               timeLeftInMilliseconds = hydraulicTimeLimit - serverTime;
                               countDown();
                           }else{
                               Log.d(TAG, "onResponse: else "+hydraulicTimeLimit+" server "+serverTime);
                               timeLeftInMilliseconds = serverTime - hydraulicTimeLimit;
                               delayDetail.setBackgroundColor(getResources().getColor(R.color.progress_warning));
                               chrono.setBase(SystemClock.elapsedRealtime()-timeLeftInMilliseconds);
                               chrono.setVisibility(View.VISIBLE);
                               timeRemaining.setVisibility(View.GONE);
                               chrono.setFormat("-%s");
                               chrono.start();
                           }
                       }
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
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
    public void countDown(){
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                delayDetail.setBackgroundColor(getResources().getColor(R.color.progress_warning));
                timeRemaining.setVisibility(View.GONE);
                chrono.setBase(SystemClock.elapsedRealtime());
                chrono.setVisibility(View.VISIBLE);
                chrono.setFormat("-%s");
                chrono.start();


            }
        }.start();
    }

}