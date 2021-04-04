package cf.homeit.admintool.ExtendsClases;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPref {


    public static final void setStringPref(Context context, String prefKey, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(prefKey, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static final String getStringPref(Context context, String prefName, String key) {
        SharedPreferences sp = context.getSharedPreferences(prefName, 0);
        return sp.getString(key, "");
    }

    public static final void logOut(Context context, String prefKey) {
        SharedPreferences sp = context.getSharedPreferences(prefKey, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear().apply();
    }
}
