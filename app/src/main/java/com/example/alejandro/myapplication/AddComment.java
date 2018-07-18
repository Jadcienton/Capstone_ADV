package com.example.alejandro.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddComment extends AppCompatActivity {
    Button addButton;
    Spinner type;
    String typeSelected;
    EditText comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        Toolbar toolbar = findViewById(R.id.toolbar_add_comments);
        toolbar.setTitle("Agregar comentarios");
        setSupportActionBar(toolbar);
        type= findViewById(R.id.spinner_type_comments);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.type,android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        addButton = findViewById(R.id.add_button);
        comment = findViewById(R.id.editText);
        type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeSelected = parent.getItemAtPosition(position).toString();
            }

        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertComment("http://"+getResources().getString(R.string.url_api)+"/adv/php/Post.php");
            }
        });


    }

    private void insertComment(String URL) {
        RequestQueue request = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddComment.this, "Error en los servidores", Toast.LENGTH_SHORT).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Map<String,String> parametros = new HashMap<>();
                try {
                    //TODO
                    parametros.put("comment_comments", comment.getText().toString());
                    if (typeSelected.equalsIgnoreCase(""))
                    parametros.put("type_comments", "");
                    parametros.put("date_comments", "");
                    parametros.put("fk_login_comments", "");
                    parametros.put("fk_event_comments", "");
                    JSONObject userJSON = new JSONObject(parametros);
                    params.put("id", "insertarComentario");
                    params.put("data", userJSON.toString());
                    params.put("table", "comments");
                } catch (Exception e) {

                }
                return params;
            }
        };
        request.add(stringRequest);
    }
}
