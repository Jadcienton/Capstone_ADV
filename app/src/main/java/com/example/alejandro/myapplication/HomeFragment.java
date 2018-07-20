package com.example.alejandro.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static DecimalFormat df1 = new DecimalFormat(".#");
    private TextView qtyOnway, percentOnway, qtyExe, percentExe, qtyInsp, percentInsp, qtyLate, percentLate;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        getActivity().setTitle("Inicio");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        qtyOnway = view.findViewById(R.id.qty_onway);
        percentOnway = view.findViewById(R.id.percent_onway);
        qtyExe = view.findViewById(R.id.qty_exe);
        percentExe = view.findViewById(R.id.percent_exe);
        qtyInsp = view.findViewById(R.id.qty_insp);
        percentInsp = view.findViewById(R.id.percent_insp);
        qtyLate = view.findViewById(R.id.qty_late);
        percentLate = view.findViewById(R.id.percent_late);
        getIndicators("http://"+getResources().getString(R.string.url_api)+"/adv/php/Get.php?id=indicador");
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_home);
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
                getIndicators("http://"+getResources().getString(R.string.url_api)+"/adv/php/Get.php?id=indicador");
            }
        });

        return view;

    }

    private void getIndicators(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jA= new JSONArray(response);
                    JSONObject jO = new JSONObject(jA.getString(0));
                    double qtyOW = jO.getDouble("qty_en_camino");
                    double qtyE = jO.getDouble("qty_ejecucion");
                    double qtyI = jO.getDouble("qty_inspeccion");
                    double lateO = jO.getDouble("late_en_camino");
                    double lateE = jO.getDouble("late_ejecucion");
                    double lateI = jO.getDouble("late_inspeccion");
                    qtyOnway.setText(jO.getString("qty_en_camino"));
                    qtyExe.setText(jO.getString("qty_ejecucion"));
                    qtyInsp.setText(jO.getString("qty_inspeccion"));
                    String sumLate = ""+((int)lateE+(int)lateI+(int)lateO);
                    String pO = df1.format(((qtyOW) / (qtyOW + qtyE + qtyI)*100))+"%";
                    String pE = df1.format(((qtyE) / (qtyOW + qtyE + qtyI)*100))+"%";
                    String pI = df1.format(((qtyI) / (qtyOW + qtyE + qtyI)*100))+"%";
                    String pL = df1.format(((lateE+lateI+lateO) / (qtyOW + qtyE + qtyI)*100))+"%";
                    percentOnway.setText(pO);
                    percentExe.setText(pE);
                    percentInsp.setText(pI);
                    qtyLate.setText(sumLate);
                    percentLate.setText(pL);

                }
                catch (JSONException e) {
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
}
