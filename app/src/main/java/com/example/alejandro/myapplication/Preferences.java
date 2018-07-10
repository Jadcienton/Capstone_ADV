package com.example.alejandro.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static final String stringPreference ="com.example.Sesion.myapplication";
    public static final String usuarioLogin = "usuario.login";
    public static final String usuarioLoginEmail = "usuarioEmail.login";
    public static final String usuarioRut = "usuarioRut.login";
    public static final String usuarioRol= "usuarioRol.login";

    public static void savePreferenceStringName(Context c, String b, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        preferences.edit().putString(Key,b).apply();
    }
    public static void savePreferenceStringEmail(Context c, String b, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        preferences.edit().putString(Key,b).apply();
    }
    public static String obtenerPreferenceStringName(Context c, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        return preferences.getString(Key,"");
    }
    public static String obtenerPreferenceStringEmail(Context c, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        return preferences.getString(Key,"");
    }
    public static void savePreferenceStringRut(Context c, String b, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        preferences.edit().putString(Key,b).apply();
    }
    public static String obtenerPreferenceStringRut(Context c, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        return preferences.getString(Key,"");
    }
    public static void savePreferenceStringRol(Context c, String b, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        preferences.edit().putString(Key,b).apply();
    }
    public static String obtenerPreferenceStringRol(Context c, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        return preferences.getString(Key,"");
    }

}