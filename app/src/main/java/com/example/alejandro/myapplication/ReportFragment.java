package com.example.alejandro.myapplication;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView indicatorOne;
    private TextView indicatorTwo;
    private TextView indicatorThree;
    private TextView indicatorFour;
    private TextView indicatorFive;
    private TextView indicatorSix;
    private TextView indicatorSeven;

    private OnFragmentInteractionListener mListener;

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
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
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        indicatorOne= view.findViewById(R.id.indicator_one);
        indicatorTwo= view.findViewById(R.id.indicator_two);
        indicatorThree= view.findViewById(R.id.indicator_three);
        indicatorFour= view.findViewById(R.id.indicator_four);
        indicatorFive= view.findViewById(R.id.indicator_five);
        indicatorSix= view.findViewById(R.id.indicator_six);
        indicatorSeven= view.findViewById(R.id.indicator_seven);

        getIndicators();
        // Inflate the layout for this fragment
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_report);
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
                getIndicators();
            }
        });


        return view;
    }

    private void getIndicators() {
        String url =  "http://192.168.43.7/adv/php/Post.php";
        RequestQueue request = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jA = new JSONArray(response);
                    JSONObject jO = new JSONObject(jA.getString(0));
                    indicatorOne.setText(EventDetail.nullValue(jO.getString("arrival_ontime"))+"%");
                    indicatorTwo.setText(EventDetail.nullValue(jO.getString("arrival_avg_pone")));
                    indicatorThree.setText(EventDetail.nullValue(jO.getString("done_jobs_time"))+"%");
                    indicatorFour.setText(EventDetail.nullValue(jO.getString("avg_time_pone")));
                    indicatorFive.setText(EventDetail.nullValue(jO.getString("avg_time_ptwo")));
                    indicatorSix.setText(EventDetail.nullValue(jO.getString("pavement_qty"))+"%");
                    indicatorSeven.setText(EventDetail.nullValue(jO.getString("pavement_delayed"))+"%");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error en los servidores", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Map<String,String> parametros = new HashMap<>();
                try{
                    JSONObject userJSON = new JSONObject(parametros);
                    params.put("id", "indicadoresHistoricos");
                    //  params.put("data", userJSON.toString());
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error al cargar la información", Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };
        request.add(stringRequest);
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
