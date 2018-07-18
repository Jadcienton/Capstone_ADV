package com.example.alejandro.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private static final String stringPreference ="com.example.Sesion.myapplication";
    public static final String usuarioLogin = "usuario.login";
    public static final String usuarioLoginEmail = "usuarioEmail.login";
    public static final String usuarioRut = "usuarioRut.login";
    public static final String usuarioRol= "usuarioRol.login";

    public static void setPreferenceStringName(Context c, String b, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        preferences.edit().putString(Key,b).apply();
    }
    public static void setPreferenceStringEmail(Context c, String b, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        preferences.edit().putString(Key,b).apply();
    }
    public static String getPreferenceStringName(Context c, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        return preferences.getString(Key,"");
    }
    public static String getPreferenceStringEmail(Context c, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        return preferences.getString(Key,"");
    }
    public static void setPreferenceStringRut(Context c, String b, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        preferences.edit().putString(Key,b).apply();
    }
    public static String getPreferenceStringRut(Context c, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        return preferences.getString(Key,"");
    }
    public static void setPreferenceStringRol(Context c, String b, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        preferences.edit().putString(Key,b).apply();
    }
    public static String getPreferenceStringRol(Context c, String Key){
        SharedPreferences preferences = c.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
        return preferences.getString(Key,"");
    }
}