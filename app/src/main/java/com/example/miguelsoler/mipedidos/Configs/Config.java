package com.example.miguelsoler.mipedidos.Configs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * | ## |   FECHA           |     PROGRAMADOR            | TWITTER
 * |###|--MES/AÑO---|-------------------------------------------------
 * | 01 |  07/2016          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/

public class Config {
    private static final int SPLASH_DELAY = 2500;
    public static final String DATABASE_NAME = "mipedido.db";
    public static final int DATABASE_VERSION = 1;
    public static final String FILE_DIR = "MiPedido";
    public int Suma = 0;
    public boolean change = false;

    private boolean isScanner = false;

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public int getSuma() {
        return Suma;
    }

    public void setSuma(int suma) {
        Suma = suma;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }
        return phrase.toString();
    }

    public static void saveSuma(Activity context, int num) {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("suma", num);
        editor.apply();
    }

    public static int getSumas(Activity context) {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        return pref.getInt("suma", 0);
    }

    public static void saveScanner(Activity context, boolean isScanner) {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isScanner", isScanner);
        editor.apply();
    }

    public static boolean getScanner(Activity context) {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        return pref.getBoolean("isScanner", false);
    }

     public static void saveQR(Activity context, String QRresult) {
            SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("QRresult", QRresult);
            editor.apply();
        }

        public static String getQR(Activity context) {
            SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
            return pref.getString("QRresult", null);
        }

}
