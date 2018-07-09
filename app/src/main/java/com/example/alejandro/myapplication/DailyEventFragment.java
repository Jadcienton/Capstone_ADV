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

import java.util.ArrayList;

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
        pushSisda();
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
                pushSisda();
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
    private void pushSisda(){

/*
        eventList.add(new Sisda("152489","P1","Estadio 3333, La Serena","Alcantarillado Sector","En Camino","A Tiempo","0:54:01"));
        eventList.add(new Sisda("456813","P1","Larrondo 1281, Coquimbo","Agua Potable Sector","En Ejecución","Retrasado","-0:54:01"));
        eventList.add(new Sisda("152789","P2","Gabriela Mistral 4001, La Serena","Alcantarillado Sector","En Camino","A Tiempo","1:48:21"));
        eventList.add(new Sisda("152465","P2","Balmaceda 328, La Serena","Agua Potable Domicilio","En Ejecución","A Tiempo","1:00:00"));
        eventList.add(new Sisda("152424","P1","Larrain Alcalde SN, La Serena","Alcantarillado Sector","En Camino","A Tiempo","0:57:51"));
        eventList.add(new Sisda("152489","P1","Los Girasoles 5383, La Serena","Agua Potable Domicilio","En Camino","A Tiempo","0:32:41"));
        eventList.add(new Sisda("456813","P1","Larrondo 1281, Coquimbo","Agua Potable Sector","En Ejecución","Retrasado","-0:54:01"));
        eventList.add(new Sisda("152789","P2","Gabriela Mistral 4001, La Serena","Alcantarillado Sector","En Camino","A Tiempo","1:48:21"));
        eventList.add(new Sisda("152465","P2","Balmaceda 328, La Serena","Agua Potable Domicilio","En Ejecución","A Tiempo","1:00:00"));
        eventList.add(new Sisda("152424","P1","Larrain Alcalde SN, La Serena","Alcantarillado Sector","En Camino","A Tiempo","0:54:01"));

*/

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
}
