package com.example.alejandro.myapplication;

import android.app.VoiceInteractor;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoricEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoricEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoricEventFragment extends Fragment  {
    private static final String TAG = "HistoricEventFragment";
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

    public HistoricEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoricEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoricEventFragment newInstance(String param1, String param2) {
        HistoricEventFragment fragment = new HistoricEventFragment();
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
        getActivity().setTitle("Eventos Históricos");
        View view = inflater.inflate(R.layout.fragment_historic_event, container, false);
        eventList = new ArrayList<>();
        recyclerViewSisda = view.findViewById(R.id.recyclerview);
        recyclerViewSisda.setLayoutManager(new LinearLayoutManager(view.getContext()));
        pushSisda("http://192.168.15.35/adv/EventosHistoricos.php");
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
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest  stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsArray = new JSONArray(response);
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

                        eventList.add(new Sisda(qS,qPE,ConvertUpper(qLE),ConvertUpper(qOE),ConvertUpper(qFE),ConvertUpper(qSE),ConvertUpper(qAC),qNC ,qDN,ConvertUpper(qCC),ConvertUpper(qCCu),ConvertUpper(qRC),qLatC,qLonC,qPDC,qPMC,qSDC,qSMC,qWMQC,ConvertUpper(qSCC),ConvertUpper(qACu),qCCus,ConvertUpper(qNP),qFPNP,qSPNP,EventDetail.DateConvert(qCDE),qADE,qBJD,qHFD,qDDE,qPDE,ConvertUpper(qNA) + " "+ ConvertUpper(qFSA),ConvertUpper(qNB) + " "+  ConvertUpper(qFSB),ConvertUpper(qNH) +  " "+ ConvertUpper(qFSH),ConvertUpper(qND) + " "+  ConvertUpper(qFSD),ConvertUpper(qNPa) + " "+  ConvertUpper(qFSP),"",""));
                    }
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity().getApplicationContext(),eventList); //view
                    recyclerViewSisda.setAdapter(adapter);
                }
                catch (JSONException e)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "catch", Toast.LENGTH_SHORT).show();

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
