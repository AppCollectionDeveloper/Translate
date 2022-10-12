package translator.text.all.languagetranslator.voice.translation;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import translator.text.all.languagetranslator.voice.translation.ad.ManageAds;

public class ProPreference {

    public static String PREF_ROOT_KEY = "keyPro";
    public static String KEY_PREMIUM = "premium";

    public static SharedPreferences proPref;

    public static boolean isPurchase(Context context) {
        proPref = context.getSharedPreferences(PREF_ROOT_KEY, MODE_PRIVATE);
        boolean isPurchase = proPref.getBoolean(KEY_PREMIUM, false);
        ManageAds.adEnabled = !isPurchase;
        return isPurchase;
    }

    public static void setPurchase(Context context) {
        proPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_ROOT_KEY, MODE_PRIVATE).edit();
        editor.putBoolean(KEY_PREMIUM, true);
        editor.apply();
        ManageAds.adEnabled = true;
    }

    public static void setSelectedLanguage(Context context, String key, int value) {
        proPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_ROOT_KEY, MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getSelectedLanguage(Context context, String key, int defaultValue) {
        proPref = context.getSharedPreferences(PREF_ROOT_KEY, MODE_PRIVATE);
        return proPref.getInt(key, defaultValue);
    }
}
