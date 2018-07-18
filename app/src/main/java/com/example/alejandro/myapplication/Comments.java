package com.example.alejandro.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
    String sisda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar_comments);
        Bundle mibundle = this.getIntent().getExtras();
        sisda = mibundle.getString("sisda");
        toolbar.setTitle("SISDA: "+ sisda);
        setSupportActionBar(toolbar);
        getComments("http://"+getResources().getString(R.string.url_api)+"/adv/php/Post.php");
        commentsList = new ArrayList<>();
        recyclerViewComments = findViewById(R.id.recyclerview_comments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        final RecyclerViewAdapterComments adapter = new RecyclerViewAdapterComments(this,commentsList);
        recyclerViewComments.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.fab_comments_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Comments.this,AddComment.class);
                intent.putExtra("sisda",sisda);
                startActivity(intent);
            }
        });
    }

    private void getComments(String URL) {
        RequestQueue request = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat formatterOut = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        Date date = format.parse(jsonObject.getString("date_comments"));

                        commentsList.add(new Comment(formatterOut.format(date),jsonObject.getString("type_comments"),jsonObject.getString("name_user")+" "+jsonObject.getString("fathersurname_user"),jsonObject.getString("comment_comments")));
                    }
                    RecyclerViewAdapterComments adapter = new RecyclerViewAdapterComments(getApplicationContext(),commentsList);
                    recyclerViewComments.setAdapter(adapter);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
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
