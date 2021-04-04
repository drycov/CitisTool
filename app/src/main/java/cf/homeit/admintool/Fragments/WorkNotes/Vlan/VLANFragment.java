package cf.homeit.admintool.Fragments.WorkNotes.Vlan;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import cf.homeit.admintool.AbstractClases.AbstractVLANFragment;

public class VLANFragment extends AbstractVLANFragment {
    public VLANFragment(){

    }
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("vlans");
    }

}
