package com.example.alejandro.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Comments extends AppCompatActivity {
    private ArrayList<Comment> commentsList;
    private RecyclerView recyclerViewComments;
    private String sisda,roleUser;
    private LinearLayout noCommentsRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar_comments);
        Bundle myBundle = this.getIntent().getExtras();
        sisda = myBundle.getString("sisda");
        toolbar.setTitle("SISDA: "+ sisda);
        setSupportActionBar(toolbar);
        noCommentsRefresh= findViewById(R.id.comments_update_message);
        getComments("http://"+getResources().getString(R.string.url_api)+"/adv/php/Post.php");
        commentsList = new ArrayList<>();
        recyclerViewComments = findViewById(R.id.recyclerview_comments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        final RecyclerViewAdapterComments adapter = new RecyclerViewAdapterComments(this,commentsList);
        recyclerViewComments.setAdapter(adapter);
        roleUser = Preferences.getPreferenceStringRol(this,Preferences.usuarioRol);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_comments);
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
                getComments("http://"+getResources().getString(R.string.url_api)+"/adv/php/Post.php");
                recyclerViewComments.setAdapter(adapter);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_comments_add);
        if (roleUser.equalsIgnoreCase("Gerente"))
            fab.setVisibility(View.GONE);
        Toast.makeText(this, ""+roleUser, Toast.LENGTH_SHORT).show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Comments.this,AddComment.class);
                intent.putExtra("sisda",sisda);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        getComments("http://"+getResources().getString(R.string.url_api)+"/adv/php/Post.php");
        final RecyclerViewAdapterComments adapter = new RecyclerViewAdapterComments(this,commentsList);
        recyclerViewComments.setAdapter(adapter);
    }

    private void getComments(String URL) {
        RequestQueue request = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Comments.this, ""+response, Toast.LENGTH_SHORT).show();
                if (response.trim().equalsIgnoreCase("null")){
                    noCommentsRefresh.setVisibility(View.VISIBLE);
                }else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        commentsList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat formatterOut = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                            Date date = format.parse(jsonObject.getString("date_comments"));

                            commentsList.add(new Comment(formatterOut.format(date), jsonObject.getString("type_comments"), jsonObject.getString("name_user") + " " + jsonObject.getString("fathersurname_user"), jsonObject.getString("comment_comments")));
                        }
                        RecyclerViewAdapterComments adapter = new RecyclerViewAdapterComments(getApplicationContext(), commentsList);
                        recyclerViewComments.setAdapter(adapter);
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Comments.this, "Error en los servidores", Toast.LENGTH_SHORT).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Map<String,String> parametros = new HashMap<>();
                    try {
                        parametros.put("sisda_event", sisda);
                        JSONObject userJSON = new JSONObject(parametros);
                        params.put("id", "obtenerComentarios");
                        params.put("data", userJSON.toString());
                    } catch (Exception e) {

                    }
                return params;
            }
        };
        request.add(stringRequest);
    }

}
