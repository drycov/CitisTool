package cf.homeit.admintool.Fragments.WorkNotes.Switch.SwitchInterface;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import cf.homeit.admintool.AbstractClases.AbstracttSwitchInterfaceFragment;

import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_SWITCH_KEY;

public class SwitchInterfaceFragment extends AbstracttSwitchInterfaceFragment {
    public Query getQuery(DatabaseReference databaseReference) {
        Bundle bundle = this.getArguments();
        String swId = bundle.getString(EXTRA_SWITCH_KEY);
        Log.d("DEBUG",swId);
        return databaseReference.child("ports").child(swId);
    }

}
