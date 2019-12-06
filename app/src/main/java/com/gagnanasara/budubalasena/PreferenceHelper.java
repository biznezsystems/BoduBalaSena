package com.gagnanasara.budubalasena;

public class PreferenceHelper {

    public static void setValue(String key, String value) {
        MainActivity.preferences.edit().putString(key, value ).commit();
    }

    public static String getValue(String key) {
        return MainActivity.preferences.getString(key,"");
    }

}

