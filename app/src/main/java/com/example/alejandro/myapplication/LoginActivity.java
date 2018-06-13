package com.example.alejandro.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;

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


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    EditText rut, pass;
    Button lB;
    RequestQueue request;
    JSONArray jsArray;
    JSONObject jsObject;
    ProgressDialog progreso;
    private Session session;
    private static final String LOG_TAG = "LOGIN ACTIVITY";

    @Override //Pregunta si la sesión sigue iniciada
    protected void onCreate(Bundle savedInstanceState) {
        session = new Session(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (session.loggedin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        request = Volley.newRequestQueue(this);
        rut = findViewById(R.id.rut_usr);
        pass = findViewById(R.id.password);
        lB = findViewById(R.id.email_sign_in_button);
        lB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FillEmpty()) {
                    ConsultaClave("http://192.168.43.7/adv/Login.php?RUT_LOGIN=" + rut.getText().toString());
                }
            }
        });
    }

    public static boolean RutValidation(String rut) {

        boolean validacion = false;
        try {
            rut = rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return validacion;
    }

    public boolean FillEmpty() {
        boolean Empty = false;
        if (rut.getText().toString().trim().equalsIgnoreCase("") || pass.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Faltan campos por llenar", Toast.LENGTH_SHORT).show();
            Empty = true;
        }
        return Empty;
    }

    public void ConsultaClave(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
        progreso = new ProgressDialog(this);
        progreso.setMessage("Iniciando sesión...");
        progreso.show();

        if (!RutValidation(rut.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Dígito verificador no coincide con el rut ingresado", Toast.LENGTH_SHORT).show();
            progreso.hide();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        jsArray = new JSONArray(response);
                        jsObject = jsArray.getJSONObject(0);
                        String qPass = jsObject.getString("password_user");
                        String qRut = jsObject.getString("rut_user");
                        String qRol = jsObject.getString("rol_user");
                        if (qPass.equals(pass.getText().toString())) {
                            RolManagement(qRol, qRut);
                        } else {
                            Toast.makeText(getApplicationContext(), "Contraseña inválida", Toast.LENGTH_SHORT).show();
                            progreso.hide();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Usuario no existe", Toast.LENGTH_SHORT).show();
                        progreso.hide();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error en los servidores", Toast.LENGTH_SHORT).show();
                    progreso.hide();
                }
            });
            queue.add(stringRequest);
        }
    }

    public void RolManagement(String rol, String qRut) {

        if (rol.equals("Gerente")) {
            Toast.makeText(getApplicationContext(), "Gerencia", Toast.LENGTH_SHORT).show();
            session.setLoggedin(true);
            Intent login = new Intent(LoginActivity.this, MainActivity.class);
           /*Bundle miBundle = new Bundle();
            miBundle.putString("UserName",qRut);
            login.putExtras(miBundle)*/
            startActivity(login);
            finish();
        } else if (rol.equals("Contratista")) {
            Toast.makeText(getApplicationContext(), "Contratista", Toast.LENGTH_SHORT).show();
        } else if (rol.equals("Inspector")) {
            Toast.makeText(getApplicationContext(), "Inspector", Toast.LENGTH_SHORT).show();
        }
        progreso.hide();
    }
}