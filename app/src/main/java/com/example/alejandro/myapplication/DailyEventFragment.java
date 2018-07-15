package com.example.alejandro.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyEventFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private GoogleMap mMap;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RequestQueue request;
    JSONArray jsArray;
    JSONObject jsObject;
    JsonObjectRequest JsonObjectRequest;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerViewSisda;
    private ArrayList<Sisda> eventList;

    public DailyEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyEventFragment newInstance(String param1, String param2) {
        DailyEventFragment fragment = new DailyEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
       // SupportMapFragment mapFragment = findFragmentById(R.id.map_detail);
        //mapFragment.getMapAsync(this);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Eventos Diarios");
        View view = inflater.inflate(R.layout.fragment_daily_event, container, false);
        eventList = new ArrayList<>();
        recyclerViewSisda = view.findViewById(R.id.recyclerview);
        recyclerViewSisda.setLayoutManager(new LinearLayoutManager(view.getContext()));
        pushSisda("http://"+getResources().getString(R.string.url_api)+"/adv/php/Get.php?id=eventosDiarios");
        //sisdaQuery("http://192.168.43.7/adv/EventosDiarios.php");
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(),eventList, "detail");
        Log.d(TAG, "hola");
        recyclerViewSisda.setAdapter(adapter);
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh,R.color.refresh1,R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    }
                },500);
                pushSisda("http://"+getResources().getString(R.string.url_api)+"/adv/php/Get.php?id=eventosDiarios");
                recyclerViewSisda.setAdapter(adapter);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng adv = new LatLng(-29.928119,-71.242348);
        mMap.addMarker(new MarkerOptions().position(adv).title("Aguas del Valle").snippet("San Joaquín").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adv,12));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void pushSisda(String URL){
        Log.d(TAG, "pushSisda: "+URL);

        final int enCaminoVerde = 1; //horas
        final int enCaminoAmarillo = 20; //minutos
        final int finHidraulicoP1 = 6; //horas
        final int finHidraulicoAmarillo = 60; //minutos
        final int finHidraulicoP2 = 12; //horas
        final int pavimento = 6; //dias
        final int pavimentoAmarillo = 1; //dias

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jO= new JSONObject(response);
                    JSONArray time = new JSONArray(jO.getString("time"));
                    String timestamp = time.getJSONObject(0).getString("now");

                    eventList.clear();
                    jsArray = new JSONArray(jO.getString("data"));
                    for (int i =0 ; i< jsArray.length() ; i++) {
                        jsObject = jsArray.getJSONObject(i);
                        String qS = jsObject.getString("sisda_event");
                        String qPE = "P"+jsObject.getString("priority_event");
                        String qLE = jsObject.getString("level_event");
                        String qOE = jsObject.getString("object_event");
                        String qFE = jsObject.getString("fault_event");
                        String qSE = jsObject.getString("status_event");
                        String qAC = jsObject.getString("address_customer");
                        String qNC = jsObject.getString("number_customer");
                        String qDN = jsObject.getString("department_customer");
                        String qCC = jsObject.getString("city_customer");
                        String qCCu = jsObject.getString("corner_customer");
                        String qRC = jsObject.getString("reference_customer");
                        String qLatC = jsObject.getString("latitude_customer");
                        String qLonC = jsObject.getString("longitude_customer");
                        String qPDC = jsObject.getString("pipe_diameter_customer");
                        String qPMC = jsObject.getString("pipe_material_customer");
                        String qSDC = jsObject.getString("sewer_diameter_customer");
                        String qSMC = jsObject.getString("sewer_material_customer");
                        String qWMQC = jsObject.getString("water_meter_quantity_customer");
                        String qSCC  = jsObject.getString("social_client_customer");
                        String qACu = jsObject.getString("activity_customer");
                        String qCCus = jsObject.getString("code_customer");
                        String qNP = jsObject.getString("name_person");
                        String qFPNP = jsObject.getString("first_phone_number_person");
                        String qSPNP = jsObject.getString("second_phone_number_person");
                        String qCDE = jsObject.getString("creation_date_event");
                        String qADE = jsObject.getString("arrival_date_event");
                        String qBJD = jsObject.getString("beginning_job_date_event");
                        String qHFD = jsObject.getString("hydraulic_finish_date_event");
                        String qDDE = jsObject.getString("definitive_date_event");
                        String qPDE = jsObject.getString("pavement_date_event");
                        String qNA = jsObject.getString("name_arrival");
                        String qFSA = jsObject.getString("fathersurname_arrival");
                        String qNB = jsObject.getString("name_beginning");
                        String qFSB = jsObject.getString("fathersurname_beginning");
                        String qNH = jsObject.getString("name_hydraulic");
                        String qFSH = jsObject.getString("fathersurname_hydraulic");
                        String qND = jsObject.getString("name_definitive");
                        String qFSD = jsObject.getString("fathersurname_definitive");
                        String qNPa = jsObject.getString("name_pavement");
                        String qFSP = jsObject.getString("fathersurname_pavement");
                        String qLatA = jsObject.getString("latitude_event");
                        String qLngA = jsObject.getString("longitude_event");
                        String qLatB = jsObject.getString("lat_beginning_event");
                        String qLngB = jsObject.getString("lng_beginning_event");
                        String qLatH = jsObject.getString("lat_hydraulic_event");
                        String qLngH = jsObject.getString("lng_hydraulic_event");
                        String qLatP = jsObject.getString("lat_pavement_event");
                        String qLngP = jsObject.getString("lng_pavement_event");
                        String qSS = jsObject.getString("synergia_status_event");
                        String qPR = jsObject.getString("pavement_required_event");

                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = format.parse(timestamp);
                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.setTime(date);
                        Calendar now = GregorianCalendar.getInstance();
                        now.setTime(date);

                        Date date2 = format.parse(qCDE);
                        Calendar creationTimeCal = GregorianCalendar.getInstance();
                        creationTimeCal.setTime(date2);
                        Calendar arrivalTimeCal = GregorianCalendar.getInstance();
                        Calendar hydraulicTimeCal = GregorianCalendar.getInstance();

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
                        String timeCalc="";
                        String delay = "";


                        if (qPE.equals("P1")){
                            switch (qSE.toLowerCase()){
                                case "en camino":
                                    creationTimeCal.add(Calendar.HOUR,1);
                                    creationTimeLimit = creationTimeCal.getTimeInMillis();
                                    if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>2400){
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(creationTimeLimit - serverTime) + " min. restantes";
                                        delay = "A Tiempo";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=2400 && TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>0){
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(creationTimeLimit - serverTime) + " min. restantes";
                                        delay = "A Tiempo A"; //amarillo
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=0 && TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>-3600){
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(creationTimeLimit - serverTime) + " minutos";
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=-3600 && TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>-86400) {
                                        if (TimeUnit.MILLISECONDS.toHours(creationTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(creationTimeLimit - serverTime) + " hora";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(creationTimeLimit - serverTime) + " horas";
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=-86400){
                                        if (TimeUnit.MILLISECONDS.toDays(creationTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toDays(creationTimeLimit - serverTime) + " día";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toDays(creationTimeLimit - serverTime) + " días";
                                        delay = "Retrasado";
                                    }

                                    break;
                                case "ejecucion":
                                    arrivalTimeCal.add(Calendar.HOUR,6);
                                    arrivalTimeLimit = arrivalTimeCal.getTimeInMillis();
                                    if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>3600){
                                        if (TimeUnit.MILLISECONDS.toHours(arrivalTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(arrivalTimeLimit - serverTime) + " hora restante";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(arrivalTimeLimit - serverTime) + " horas restantes";
                                        delay = "A Tiempo";
                                    } else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=3600 && TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>0){
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(arrivalTimeLimit - serverTime) + " min. restantes";
                                        delay = "A Tiempo A";//Amarillo
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=0 && TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>-3600){
                                        if (TimeUnit.MILLISECONDS.toMinutes(arrivalTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(arrivalTimeLimit - serverTime) + " minuto";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(arrivalTimeLimit - serverTime) + " minutos";
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=-3600 && TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>-86400) {
                                        if (TimeUnit.MILLISECONDS.toHours(arrivalTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(arrivalTimeLimit - serverTime) + " hora";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(arrivalTimeLimit - serverTime) + " horas";
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=-86400){
                                        if (TimeUnit.MILLISECONDS.toDays(arrivalTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toDays(arrivalTimeLimit - serverTime) + " día";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toDays(arrivalTimeLimit - serverTime) + " días";
                                        delay = "Retrasado";
                                    }
                                    break;
                                case "inspeccion":
                                    arrivalTimeCal.add(Calendar.HOUR,6);
                                    arrivalTimeLimit = arrivalTimeCal.getTimeInMillis();
                                    if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>3600){
                                        if (TimeUnit.MILLISECONDS.toHours(arrivalTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(arrivalTimeLimit - serverTime) + " hora restante";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(arrivalTimeLimit - serverTime) + " horas restantes";
                                        delay = "A Tiempo";
                                    } else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=3600 && TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>0){
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(arrivalTimeLimit - serverTime) + " min. restantes";
                                        delay = "A Tiempo A";//Amarillo
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=0 && TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>-3600){
                                        if (TimeUnit.MILLISECONDS.toMinutes(arrivalTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(arrivalTimeLimit - serverTime) + " minuto";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(arrivalTimeLimit - serverTime) + " minutos";
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=-3600 && TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)>-86400) {
                                        if (TimeUnit.MILLISECONDS.toHours(arrivalTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(arrivalTimeLimit - serverTime) + " hora";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(arrivalTimeLimit - serverTime) + " horas";
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(arrivalTimeLimit - serverTime)<=-86400){
                                        if (TimeUnit.MILLISECONDS.toDays(arrivalTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toDays(arrivalTimeLimit - serverTime) + " día";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toDays(arrivalTimeLimit - serverTime) + " días";
                                        delay = "Retrasado";
                                    }
                                    break;
                                case "pavimento":
                                    hydraulicTimeCal.add(Calendar.DAY_OF_MONTH,6);
                                    hydraulicTimeLimit = hydraulicTimeCal.getTimeInMillis();
                                    if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>86400){
                                        if (TimeUnit.MILLISECONDS.toDays(hydraulicTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toDays(hydraulicTimeLimit - serverTime) + " día restante";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toDays(hydraulicTimeLimit - serverTime) + " días restantes";
                                        delay = "A Tiempo";
                                    } else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=86400 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>3600){
                                        if (TimeUnit.MILLISECONDS.toHours(hydraulicTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(hydraulicTimeLimit - serverTime) + " hora restante";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(hydraulicTimeLimit - serverTime) + " horas restantes";
                                        delay = "A Tiempo A";
                                    } else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=3600 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>0){
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(hydraulicTimeLimit - serverTime) + " min. restantes";
                                        delay = "A Tiempo A";//Amarillo
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=0 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>-3600){
                                        if (TimeUnit.MILLISECONDS.toMinutes(hydraulicTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(hydraulicTimeLimit - serverTime) + " minuto";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(hydraulicTimeLimit - serverTime) + " minutos";
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=-3600 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>-86400) {
                                        if (TimeUnit.MILLISECONDS.toHours(hydraulicTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(hydraulicTimeLimit - serverTime) + " hora";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toHours(hydraulicTimeLimit - serverTime) + " horas";
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=-86400){
                                        if (TimeUnit.MILLISECONDS.toDays(hydraulicTimeLimit - serverTime)==1)
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toDays(hydraulicTimeLimit - serverTime) + " día";
                                        else
                                            timeCalc = "" + TimeUnit.MILLISECONDS.toDays(hydraulicTimeLimit - serverTime) + " días";
                                        delay = "Retrasado";
                                    }
                                    break;
                            }
                        }else if(qPE.equals("P2")){
                            if (qSE.equalsIgnoreCase("en camino") || qSE.equalsIgnoreCase("inspeccion") || qSE.equalsIgnoreCase("ejecucion")){
                                creationTimeCal.add(Calendar.HOUR,12);
                                creationTimeLimit = creationTimeCal.getTimeInMillis();
                                if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>3600){
                                    if (TimeUnit.MILLISECONDS.toHours(creationTimeLimit - serverTime)==1)
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toHours(creationTimeLimit - serverTime) + " hora restante";
                                    else
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toHours(creationTimeLimit - serverTime) + " horas restantes";
                                    delay = "A Tiempo";
                                } else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=3600 && TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>0){
                                    timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(creationTimeLimit - serverTime) + " min. restantes";
                                    delay = "A Tiempo A";//Amarillo
                                }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=0 && TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>-3600){
                                    if (TimeUnit.MILLISECONDS.toMinutes(creationTimeLimit - serverTime)==1)
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(creationTimeLimit - serverTime) + " minuto";
                                    else
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(creationTimeLimit - serverTime) + " minutos";
                                    delay = "Retrasado";
                                }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=-3600 && TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)>-86400) {
                                    if (TimeUnit.MILLISECONDS.toHours(creationTimeLimit - serverTime)==1)
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toHours(creationTimeLimit - serverTime) + " hora";
                                    else
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toHours(creationTimeLimit - serverTime) + " horas";
                                    delay = "Retrasado";
                                }else if (TimeUnit.MILLISECONDS.toSeconds(creationTimeLimit - serverTime)<=-86400){
                                    if (TimeUnit.MILLISECONDS.toDays(creationTimeLimit - serverTime)==1)
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toDays(creationTimeLimit - serverTime) + " día";
                                    else
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toDays(creationTimeLimit - serverTime) + " días";
                                    delay = "Retrasado";
                                }
                            }else if (qSE.equalsIgnoreCase("pavimento")){
                                Log.d(TAG, "onResponse: pavimento");
                                hydraulicTimeCal.add(Calendar.DAY_OF_MONTH,6);
                                hydraulicTimeLimit = hydraulicTimeCal.getTimeInMillis();
                                if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>86400){
                                    if (TimeUnit.MILLISECONDS.toDays(hydraulicTimeLimit - serverTime)==1)
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toDays(hydraulicTimeLimit - serverTime) + " día restante";
                                    else
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toDays(hydraulicTimeLimit - serverTime) + " días restantes";
                                    delay = "A Tiempo";
                                } else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=86400 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>3600){
                                    if (TimeUnit.MILLISECONDS.toHours(hydraulicTimeLimit - serverTime)==1)
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toHours(hydraulicTimeLimit - serverTime) + " hora restante";
                                    else
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toHours(hydraulicTimeLimit - serverTime) + " horas restantes";
                                    delay = "A Tiempo A";
                                } else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=3600 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>0){
                                    timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(hydraulicTimeLimit - serverTime) + " min. restantes";
                                    delay = "A Tiempo A";//Amarillo
                                }else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=0 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>-3600){
                                    if (TimeUnit.MILLISECONDS.toMinutes(hydraulicTimeLimit - serverTime)==1)
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(hydraulicTimeLimit - serverTime) + " minuto";
                                    else
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toMinutes(hydraulicTimeLimit - serverTime) + " minutos";
                                    delay = "Retrasado";
                                }else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=-3600 && TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)>-86400) {
                                    if (TimeUnit.MILLISECONDS.toHours(hydraulicTimeLimit - serverTime)==1)
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toHours(hydraulicTimeLimit - serverTime) + " hora";
                                    else
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toHours(hydraulicTimeLimit - serverTime) + " horas";
                                    delay = "Retrasado";
                                }else if (TimeUnit.MILLISECONDS.toSeconds(hydraulicTimeLimit - serverTime)<=-86400){
                                    if (TimeUnit.MILLISECONDS.toDays(hydraulicTimeLimit - serverTime)==1)
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toDays(hydraulicTimeLimit - serverTime) + " día";
                                    else
                                        timeCalc = "" + TimeUnit.MILLISECONDS.toDays(hydraulicTimeLimit - serverTime) + " días";
                                    delay = "Retrasado";
                                }
                            }
                        }


                        eventList.add(new Sisda(qS,qPE, convertUpper(qLE), convertUpper(qOE), convertUpper(qFE), convertUpper(qSE), convertUpper(qAC),qNC ,qDN, convertUpper(qCC),
                                convertUpper(qCCu), convertUpper(qRC),qLatC,qLonC,qPDC,qPMC,qSDC,qSMC,qWMQC, convertUpper(qSCC), convertUpper(qACu),qCCus, convertUpper(qNP),qFPNP,
                                qSPNP,EventDetail.dateConvert(qCDE),qADE,qBJD,qHFD,qDDE,qPDE, convertUpper(qNA) + " "+ convertUpper(qFSA), convertUpper(qNB) + " "+  convertUpper(qFSB),
                                convertUpper(qNH) +  " "+ convertUpper(qFSH), convertUpper(qND) + " "+  convertUpper(qFSD),
                                convertUpper(qNPa) + " "+  convertUpper(qFSP),delay,timeCalc, qLatA, qLngA,qLatB,qLngB,qLatH,qLngH,qLatP,qLngP,qSS,qPR));
                    }
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity().getApplicationContext(),eventList, "detail"); //view
                    recyclerViewSisda.setAdapter(adapter);
                }
                catch (JSONException e)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "catch", Toast.LENGTH_SHORT).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }} , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "No se puede conectar "+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void sisdaQuery(String url){
        Log.d(TAG, "LA URL ES: " + url);
        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        //JsonObjectRequest request = new JsonObjectRequest()


       // RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        // Initialize a new JsonArrayRequest instance
        //JsonArrayRequest request = new JsonArrayRequest(url, new JSONObject(), future, future);
       // requestQueue.add(request);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try{
                            for(int i=0;i<response.length();i++) {
                                // Get current json object
                                JSONObject sisda = response.getJSONObject(i);
                                Log.d(TAG, " " + response.length());
                                //eventList.add(new Sisda(sisda.getString("SISDA_EVENT"),"P"+sisda.getString("SISDA_EVENT"),sisda.getString("ADDRESS_COSTUMER")+" "+sisda.getString("NUMBER_COSTUMBER"),"Alcantarillado Sector",sisda.getString("STATE_EVENT"),"A Tiempo","0:54:01"));

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
        //requestQueue.add(jsonArrayRequest);
        Log.d(TAG, " holl");

    }
    private static String convertUpper(String word)
    //TODO agregar En Camino, Pavimento
    {
        if (word.equals(""))
        {
            word = word.replace("", "-");
            return word;
        }
        else {
            if (word.equals("EJECUCION")) {
                word = word.replace("EJECUCION", "EJECUCIÓN");
            } else if (word.equals("INSPECCION")) {
                word = word.replace("INSPECCION", "INSPECCIÓN");
            } else if (word.equals("HIDRAULICA")) {
                word = word.replace("HIDRAULICA", "HIDRÁULICA");
            }
            word = word.toUpperCase().charAt(0) + word.substring(1, word.length()).toLowerCase();
            char[] character = word.toCharArray();
            character[0] = Character.toUpperCase(character[0]);
            for (int i = 0; i < word.length() - 2; i++) {
                // Es 'palabra'
                if (character[i] == ' ' || character[i] == '.' || character[i] == ',') {
                    // Reemplazamos
                    character[i + 1] = Character.toUpperCase(character[i + 1]);
                }
            }
            String string = new String(character);
            return word;
        }
    }
}
