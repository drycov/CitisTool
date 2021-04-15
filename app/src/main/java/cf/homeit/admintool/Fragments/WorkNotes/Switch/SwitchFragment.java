package cf.homeit.admintool.Fragments.WorkNotes.Switch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import cf.homeit.admintool.AbstractClases.AbstractSwitchFragment;

public class SwitchFragment extends AbstractSwitchFragment {
    public SwitchFragment() {

    }

    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("switches");
    }

}
