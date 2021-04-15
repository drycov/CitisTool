package cf.homeit.admintool.Fragments.Notes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import cf.homeit.admintool.AbstractClases.AbstractNoteFragment;

import static cf.homeit.admintool.ExtendsClases.SupportVoids.getUid;

public class NoteFragment extends AbstractNoteFragment {
    public NoteFragment() {

    }

    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("notes").child(getUid(requireActivity().getApplicationContext()));
    }

}
