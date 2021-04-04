package cf.homeit.admintool.ExtendsClases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm E");
        Date curDate = new Date();
        return format.format(curDate);
    }
    public static String getUid(Context context) {
        return AppPref.getStringPref(context, Constants.SHARED_PREF_NAME_USER, Constants.TAG_UID);
    }
    public static void deleteFromFirebase(final String id, final String child,Context context){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(child);
        Query hekkQuery = mDatabaseReference;

        hekkQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.child(id).getRef().removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showToast(context, "Delete: "+id + ". Success!" );
//                                deletePhotoFromStorage(id);
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        /////this is for firebase storage
//        Task<Void> task = mStorageReference.child(String.valueOf(id).concat("_Image")).delete();
    }

}