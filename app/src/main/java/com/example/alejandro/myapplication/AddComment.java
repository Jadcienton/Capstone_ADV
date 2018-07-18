package com.example.alejandro.myapplication;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddComment extends AppCompatActivity {
    private Button addButton;
    private Spinner type;
    private String typeSelected;
    private EditText comment;
    private String sisda,rutUser;
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
        rutUser = Preferences.getPreferenceStringRut(this,Preferences.usuarioRut);
        Bundle myBundle = this.getIntent().getExtras();
        sisda = myBundle.getString("sisda");
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeSelected.equalsIgnoreCase("Seleccionar tipo")) {
                    Toast.makeText(AddComment.this, "Seleccionar tipo de comentario", Toast.LENGTH_SHORT).show();
                }else {
                    insertComment("http://" + getResources().getString(R.string.url_api) + "/adv/php/Post.php");
                }
            }
        });


    }

    private void insertComment(String URL) {
        RequestQueue request = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddComment.this, ""+response, Toast.LENGTH_SHORT).show();
                if (response.trim().equalsIgnoreCase("true")){
                    AddComment.this.finish();
                }else {
                    Toast.makeText(AddComment.this, "Fallo al agregar comentario. Volver a intentar", Toast.LENGTH_SHORT).show();
                }
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
                    parametros.put("comment_comments", comment.getText().toString());
                    parametros.put("type_comments", typeSelected);
                    parametros.put("date_comments", "NOW()");
                    parametros.put("fk_login_comments", rutUser);
                    parametros.put("fk_event_comments", sisda);
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
