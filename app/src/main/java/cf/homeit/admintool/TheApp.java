package cf.homeit.admintool;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

public class TheApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Log.d("TheApp", "application created");
    }
}