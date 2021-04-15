package cf.homeit.admintool.ExtendsClases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SupportVoids {
    public static DatabaseReference mDatabaseReference;

    public static boolean isReadOnly() {
        String storageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(storageState);
    }

    // проверяем есть ли доступ к внешнему хранилищу
    public static boolean isAvailable() {
        String storageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(storageState);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String getTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm E");
        Date curDate = new Date();
        return format.format(curDate);
    }

    public static String getUid(Context context) {
        return getStringPref(context, Constants.SHARED_PREF_NAME_USER, Constants.TAG_UID);
    }

    public static void deleteFromFirebase(final String id, final String child, Context context) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(child);
        Query hekkQuery = mDatabaseReference;

        hekkQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.child(id).getRef().removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showToast(context, "Delete: " + id + ". Success!");
//                                deletePhotoFromStorage(id);
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        /////this is for firebase storage
//        Task<Void> task = mStorageReference.child(String.valueOf(id).concat("_Image")).delete();
    }

    public static void setStringPref(Context context, String prefKey, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(prefKey, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getStringPref(Context context, String prefName, String key) {
        SharedPreferences sp = context.getSharedPreferences(prefName, 0);
        return sp.getString(key, "");
    }

    public static void logOut(Context context, String prefKey) {
        SharedPreferences sp = context.getSharedPreferences(prefKey, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear().apply();
    }

}