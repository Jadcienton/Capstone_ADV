package com.example.alejandro.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static final String StringPreference ="com.example.Sesion.myapplication";
    public static final String UsuarioLogin = "usuario.login";
    public static final String UsuarioLoginEmail = "usuarioEmail.login";

    public static void SavePreferenceStringName(Context c, String b, String Key){
        SharedPreferences preferences = c.getSharedPreferences(StringPreference,Context.MODE_PRIVATE);
        preferences.edit().putString(Key,b).apply();
    }
    public static void SavePreferenceStringEmail(Context c, String b, String Key){
        SharedPreferences preferences = c.getSharedPreferences(StringPreference,Context.MODE_PRIVATE);
        preferences.edit().putString(Key,b).apply();
    }
    public static String ObtenerPreferenceStringName(Context c, String Key){
        SharedPreferences preferences = c.getSharedPreferences(StringPreference,Context.MODE_PRIVATE);
        return preferences.getString(Key,"");
    }
    public static String ObtenerPreferenceStringEmail(Context c, String Key){
        SharedPreferences preferences = c.getSharedPreferences(StringPreference,Context.MODE_PRIVATE);
        return preferences.getString(Key,"");
    }
}