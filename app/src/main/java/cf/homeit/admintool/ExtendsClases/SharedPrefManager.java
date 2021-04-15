package cf.homeit.admintool.ExtendsClases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

import cf.homeit.admintool.Activity.MainActivity;

import static cf.homeit.admintool.ExtendsClases.SupportVoids.getStringPref;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.logOut;

public class SharedPrefManager {
    @SuppressLint("StaticFieldLeak")
    private static SharedPrefManager mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences

    //    public void userLogin(User user) {
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME_USER, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(Constants.KEY_FNAME, user.getFirstName());
//        editor.putString(Constants.KEY_LNAME, user.getLastName());
//        editor.putString(Constants.KEY_MNAME, user.getMiddleName());
//        editor.putString(Constants.KEY_EMAIL, user.getEmail());
//        editor.putString(Constants.KEY_PHONE_NUM, user.getPhoneNumber());
//        editor.apply();
//    }
    public void uidWrite(String uid) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME_USER, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TAG_UID, uid);
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME_USER, Context.MODE_PRIVATE);
        logOut(mCtx, getStringPref(mCtx, Constants.SHARED_PREF_NAME_USER, Constants.TAG_UID));
        SharedPreferences.Editor SPeditor = sharedPreferences.edit();
        SPeditor.clear();
        SPeditor.apply();
        FirebaseAuth.getInstance().signOut();

        mCtx.startActivity(new Intent(mCtx, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME_FCM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME_FCM, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TAG_TOKEN, null);
    }


}