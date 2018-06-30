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
import android.util.EventLog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDetail extends AppCompatActivity {
    private CoordinatorLayout mainlayout;
    private TextView codeSisda,dateCreation,dateArrival,dateBeg,dateHydraulic,datePavement;
    private TextView codeCostumer,nameCostumer,Phones;
    private TextView Address,numberAddress,numerDepto,city,corner,Reference;
    private TextView level,object,fault;
    private TextView pipeDiameter, pipeMaterial, sewerDiameter, sewerMaterial, waterMeterQuantity, socialClient, activityCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        toolbar.setTitle("Detalle");
        setSupportActionBar(toolbar);

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
        Sisda sisdaData = gson.fromJson(sisda,Sisda.class);

        codeSisda.setText(sisdaData.getSisda());
        dateCreation.setText(sisdaData.getDateCreation());//sisdaData.getDateCreation
        dateArrival.setText(DateConvert(sisdaData.getDateArrival()));
        dateBeg.setText(DateConvert(sisdaData.getDateBeginning()));
        dateHydraulic.setText(DateConvert(sisdaData.getDateHydraulic()));
        datePavement.setText(DateConvert(sisdaData.getDatePavement()));

        codeCostumer.setText(NullValue(sisdaData.getCodeCustomer()));
        nameCostumer.setText(NullValue(sisdaData.getClientName()));
        Phones.setText(NullValue(sisdaData.getClientPhone())+ " / "+NullValue(sisdaData.getClientPhoneTwo()));

        Address.setText(NullValue(sisdaData.getAddress()));
        numberAddress.setText(NullValue(sisdaData.getNumber()));
        numerDepto.setText(NullValue(sisdaData.getDepartment()));
        city.setText(NullValue(sisdaData.getCity()));
        corner.setText(NullValue(sisdaData.getCorner()));
        Reference.setText(NullValue(sisdaData.getReference()));

        level.setText(NullValue(sisdaData.getLevel()));
        object.setText(NullValue(sisdaData.getObject()));
        fault.setText(NullValue(sisdaData.getFault()));

        pipeDiameter.setText(NullValue(sisdaData.getPipeDiameter()));
        pipeMaterial.setText(NullValue(sisdaData.getPipeMaterial()));
        sewerDiameter.setText(NullValue(sisdaData.getSewerDiameter()));
        sewerMaterial.setText(NullValue(sisdaData.getSewerMaterial()));
        waterMeterQuantity.setText(NullValue(sisdaData.getWaterMeterQuantity()));
        socialClient.setText(NullValue(sisdaData.getSocialClient()));
        activityCustomer.setText(NullValue(sisdaData.getActivityCustomer()));


        //Toast.makeText(getApplicationContext(), sisda, Toast.LENGTH_LONG).show();
        FloatingActionButton fab = findViewById(R.id.fab_comments_detail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mainlayout, "Comentario", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    public static String DateConvert(String Fecha) {
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

    public static String NullValue(String Value) {
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
        }
        return Value;
    }

}