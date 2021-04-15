package cf.homeit.admintool.Fragments.Notes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cf.homeit.admintool.DataModels.Note;
import cf.homeit.admintool.ExtendsClases.Constants;
import cf.homeit.admintool.R;

import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_NOTE_KEY;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.getStringPref;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;

public class NoteDetailFragment extends Fragment {
    private static final String TAG = "PostDetailActivity";
    private TextView titleEt;
    private TextView contentEt;
    private TextView dateEt;
    private ValueEventListener mPostListener;
    private FloatingActionButton saveBtn;
    private DatabaseReference mPostReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        Bundle bundle = this.getArguments();
        String mPostKey = bundle.getString(EXTRA_NOTE_KEY);

        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_NOTE_KEY");
        }
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("notes").child(getUid()).child(mPostKey);
    }

    private void init(View view) {
        titleEt = view.findViewById(R.id.note_Detail_title);
        contentEt = view.findViewById(R.id.note_Detail_content);
        dateEt = view.findViewById(R.id.note_Detail_time);
        saveBtn = view.findViewById(R.id.noteDetailSave);
    }

    public String getUid() {
        return getStringPref(requireActivity().getApplicationContext(), Constants.SHARED_PREF_NAME_USER, Constants.TAG_UID);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Note am = dataSnapshot.getValue(Note.class);
                // [START_EXCLUDE]
                titleEt.setText(am.noteTitle);
                contentEt.setText(am.noteContent);
                dateEt.setText(am.noteTime);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                showToast(requireContext().getApplicationContext(), "Failed to load post.");
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;

        // Listen for comments
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

    }

}
