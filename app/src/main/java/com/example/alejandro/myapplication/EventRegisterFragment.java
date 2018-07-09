package com.example.alejandro.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventRegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventRegisterFragment extends Fragment {
    private static final String TAG = "EventRegisterFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
    RecyclerView recyclerViewSisda;
    ArrayList<Sisda> eventList;


    public EventRegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventRegisterFragment newInstance(String param1, String param2) {
        EventRegisterFragment fragment = new EventRegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Registro de Eventos");
        View view = inflater.inflate(R.layout.fragment_historic_event, container, false);
        eventList = new ArrayList<>();
        recyclerViewSisda = view.findViewById(R.id.recyclerview);
        recyclerViewSisda.setLayoutManager(new LinearLayoutManager(view.getContext()));
        pushSisda("http://"+getResources().getString(R.string.url_api)+"/adv/php/Get.php?id=eventosHistoricos");
        //RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity().getApplicationContext(),eventList); //view
        //recyclerViewSisda.setAdapter(adapter);
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

                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = format.parse(timestamp);
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTime(date);
/*
                    Calendar inicio = GregorianCalendar.getInstance();
                    inicio.set(2018,7,2,19,40,0);
                    Calendar fin = GregorianCalendar.getInstance();
                    fin.setTime(date);

                        long end = inicio.getTimeInMillis();
                        long start = fin.getTimeInMillis();


*/


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


                        Calendar now = GregorianCalendar.getInstance();
                        now.setTime(date);

                        Date date2 = format.parse(qCDE);
                        Calendar creationTimeCal = GregorianCalendar.getInstance();
                        creationTimeCal.setTime(date2);
                        // TODO change qCDE to qADE
                        Date date3 = format.parse(qCDE); //qADE
                        Calendar arrivalTimeCal = GregorianCalendar.getInstance();
                        arrivalTimeCal.setTime(date3);
                        //qHFD
                        Date date4 = format.parse(qCDE); //qADE
                        Calendar hydraulicTimeCal = GregorianCalendar.getInstance();
                        hydraulicTimeCal.setTime(date4);

                        long serverTime = now.getTimeInMillis();
                        long creationTime = creationTimeCal.getTimeInMillis();
                        long arrivalTime = arrivalTimeCal.getTimeInMillis();
                        long hydraulicTime = hydraulicTimeCal.getTimeInMillis();
                        //Log.d(TAG, "onResponse: time "+TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - creationTime)));
                        String timeCalc="";
                        String delay = "";


                        if (qPE.equals("P1")){
                            switch (qSE.toLowerCase()){
                                case "en camino":
                                    if(TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - creationTime))<enCaminoVerde) {
                                        if (TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - creationTime))>=60-enCaminoAmarillo){
                                            timeCalc = "" + (60 - TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - creationTime))) + " min. restantes";
                                            delay = "A Tiempo A"; //amarillo
                                        }else if(TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - creationTime))<60-enCaminoAmarillo) {
                                            timeCalc = "" + (60 - TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - creationTime))) + " min. restantes";
                                            delay = "A Tiempo";
                                        }
                                    }else if(TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - creationTime))<2){
                                        timeCalc = "" + (60 - TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - creationTime))) + " min.";
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - creationTime))>24) {
                                        timeCalc = "" + (-TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - creationTime)))+" días";
                                        delay = "Retrasado";
                                    } else {
                                        timeCalc = "" + (1-TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - creationTime)))+" horas";
                                        delay = "Retrasado";
                                    }
                                    break;
                                case "ejecucion":
                                    if (TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - arrivalTime))<finHidraulicoP1){
                                        if (TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - arrivalTime))>=360-finHidraulicoAmarillo){
                                            timeCalc = "" + (360 - TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - arrivalTime))) + " min. restantes";
                                            delay = "A Tiempo A"; //amarillo
                                        }else if(TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - arrivalTime))<360-finHidraulicoAmarillo) {
                                            timeCalc = "" + (6 - TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - arrivalTime))) + " horas restantes";
                                            delay = "A Tiempo";
                                        }
                                    } else if (TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - arrivalTime))<7){
                                        timeCalc = "" + (360 - TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - arrivalTime))) + " min.";
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - arrivalTime))>24){
                                        timeCalc = "" + (-TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - arrivalTime)))+" días";
                                        delay = "Retrasado";
                                    }else {
                                        timeCalc = "" + (6-TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - arrivalTime)))+" horas";
                                        delay = "Retrasado";
                                    }
                                    break;
                                case "inspeccion":
                                    if (TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - arrivalTime))<finHidraulicoP1){
                                        if (TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - arrivalTime))>=360-finHidraulicoAmarillo){
                                            timeCalc = "" + (360 - TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - arrivalTime))) + " min. restantes";
                                            delay = "A Tiempo A"; //amarillo
                                        }else if(TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - arrivalTime))<360-finHidraulicoAmarillo) {
                                            timeCalc = "" + (6 - TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - arrivalTime))) + " horas restantes";
                                            delay = "A Tiempo";
                                        }
                                    } else if (TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - arrivalTime))<7){
                                        timeCalc = "" + (360 - TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - arrivalTime))) + " min.";
                                        delay = "Retrasado";
                                    }else if (TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - arrivalTime))>24){
                                        timeCalc = "" + (-TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - arrivalTime)))+" días";
                                        delay = "Retrasado";
                                    }else {
                                        timeCalc = "" + (6-TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - arrivalTime)))+" horas";
                                        delay = "Retrasado";
                                    }
                                    break;
                                //TODO arreglar pavimentacion
                                case "pavimento":
                                    if (TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - hydraulicTime))<pavimento) {//A Tiempo
                                        Log.d(TAG, "onResponse: A tiempo " + TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - hydraulicTime)) + " sisda: " + qS);
                                        if (TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - hydraulicTime)) == pavimento - pavimentoAmarillo) {
                                            Log.d(TAG, "onResponse: A tiempo Amarillo ==" + TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - hydraulicTime)) + " sisda: " + qS);
                                            if (TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - hydraulicTime)) > ((pavimento * 60 * 24) - (pavimentoAmarillo * 60))) {//1
                                                Log.d(TAG, "onResponse: 1: " + TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - hydraulicTime)) + " sisda: " + qS);
                                                timeCalc = "" + (8640 - TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - hydraulicTime))) + " min. restantes";
                                            } else if (TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - hydraulicTime)) == ((pavimento * 60 * 24) - (pavimentoAmarillo * 60))){//2
                                                Log.d(TAG, "onResponse: 2: " + TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - hydraulicTime)) + " sisda: " + qS);
                                                timeCalc = "" + (144 - TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - hydraulicTime))) + " hora restante";
                                            } else if (TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - hydraulicTime)) == ((pavimento - pavimentoAmarillo) * 60 * 24)){//3
                                                Log.d(TAG, "onResponse: 3: " + TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - hydraulicTime)) + " sisda: " + qS);
                                                timeCalc = "" + (144 - TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - hydraulicTime))) + " día restante";
                                            }else {//else
                                                Log.d(TAG, "onResponse: else: " + TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - hydraulicTime)) + " sisda: " + qS);
                                                timeCalc = "" + (144 - TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - hydraulicTime))) + " horas restantes";
                                            }
                                            delay = "A Tiempo A"; //amarillo
                                        }else if(TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - hydraulicTime))<(pavimento-pavimentoAmarillo)) {
                                            Log.d(TAG, "onResponse: A tiempo elseif" + TimeUnit.MILLISECONDS.toMinutes(Math.abs(serverTime - hydraulicTime))+" sisda: "+qS);
                                            timeCalc = "" + (6 - TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - hydraulicTime))) + " días restantes";
                                            delay = "A Tiempo";
                                        }



                                    }else if (TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - hydraulicTime))>=pavimento){
                                        Log.d(TAG, "onResponse: retra: "+TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - hydraulicTime))+" sisda: "+qS);
                                        timeCalc = "" + (-TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - hydraulicTime)))+" días";
                                        delay = "Retrasado";
                                    }else {
                                        Log.d(TAG, "onResponse: retrasado: "+TimeUnit.MILLISECONDS.toDays(Math.abs(serverTime - hydraulicTime))+" sisda: "+qS);
                                        timeCalc = "" + (6-TimeUnit.MILLISECONDS.toHours(Math.abs(serverTime - hydraulicTime)))+" horas";
                                        delay = "Retrasado";
                                    }
                                    break;
                            }
                        } else if(qPE.equals("P2")){

                        }


                        eventList.add(new Sisda(qS,qPE,ConvertUpper(qLE),ConvertUpper(qOE),ConvertUpper(qFE),ConvertUpper(qSE),ConvertUpper(qAC),qNC ,qDN,ConvertUpper(qCC),
                                ConvertUpper(qCCu),ConvertUpper(qRC),qLatC,qLonC,qPDC,qPMC,qSDC,qSMC,qWMQC,ConvertUpper(qSCC),ConvertUpper(qACu),qCCus,ConvertUpper(qNP),qFPNP,
                                qSPNP,EventDetail.dateConvert(qCDE),qADE,qBJD,qHFD,qDDE,qPDE,ConvertUpper(qNA) + " "+ ConvertUpper(qFSA),ConvertUpper(qNB) + " "+  ConvertUpper(qFSB),
                                ConvertUpper(qNH) +  " "+ ConvertUpper(qFSH),ConvertUpper(qND) + " "+  ConvertUpper(qFSD),
                                ConvertUpper(qNPa) + " "+  ConvertUpper(qFSP),delay,timeCalc, qLatA, qLngA,qLatB,qLngB,qLatH,qLngH,qLatP,qLngP,qSS));
                    }
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity().getApplicationContext(),eventList, "register"); //view
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

    private static String ConvertUpper(String word)
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
