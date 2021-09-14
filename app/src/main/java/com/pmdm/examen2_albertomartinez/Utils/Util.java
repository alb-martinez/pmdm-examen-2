package com.pmdm.examen2_albertomartinez.Utils;

// Clase para poder recuperar datos de cualquier SharedPreferences, pasandola como parametro

import android.content.SharedPreferences;

public class Util {

    // Metodo para recuperar el email
    public static String getUserMailPrefs(SharedPreferences shPreferences) {
        return shPreferences.getString("email", "");
    }

    // Metodo para recuperar el password
    public static String getUserPasswordPrefs(SharedPreferences shPreferences) {
        return shPreferences.getString("pass", "");
    }
}
