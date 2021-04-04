package cf.homeit.admintool.Fragments.Notes;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import cf.homeit.admintool.Activity.MainActivity;
import cf.homeit.admintool.DataModels.Note;
import cf.homeit.admintool.DataModels.User;
import cf.homeit.admintool.R;

import static cf.homeit.admintool.ExtendsClases.SupportVoids.getTime;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.getUid;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;

public class NoteNewFragment extends Fragment{
    private static final String TAG = "NewPostActivity";
    private static final CharSequence REQUIRED = "Required";
    private EditText titleEt;
    private EditText contentEt;
    private DatabaseReference mDatabase;
    private FloatingActionButton saveBtn;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_note_new,container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        init(view);
    }

    private void init(View view) {
        titleEt = view.findViewById(R.id.note_title);
        contentEt = view.findViewById(R.id.note_content);
        saveBtn = view.findViewById(R.id.noteSave);
        saveBtn.setOnClickListener(view1 -> {
            submitPost();
        });
    }



    

    private void setEditingEnabled(boolean enabled) {
        titleEt.setEnabled(enabled);
        contentEt.setEnabled(enabled);
        if (enabled) {
            saveBtn.show();
        } else {
            saveBtn.hide();
        }
    }

    private void submitPost() {
        final String title = titleEt.getText().toString();
        final String body = contentEt.getText().toString();
        final String dateTime = getTime();
        // Title is required
        if (TextUtils.isEmpty(title)) {
            titleEt.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            contentEt.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        showToast(requireActivity().getApplicationContext(), "Posting...");


        // [START single_value_read]
        String uid = getUid(requireActivity().getApplicationContext());
        mDatabase.child("users").child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + uid + " is unexpectedly null");
                            showToast(getActivity().getApplicationContext(),
                                    "Error: could not fetch user.");
                        } else {
                            // Write new post
                            writeNewPost(uid, title, body, dateTime);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
                        navController.navigate(R.id.todoListFragment);
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }
    // [START write_fan_out]
    private void writeNewPost(String uid, String title, String body,String time) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("notes").child(uid).push().getKey();
        Note note = new Note(key, title, body, time, uid);
        Map<String, Object> postValues = note.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notes/" +uid +"/" + key, postValues);
        mDatabase.updateChildren(childUpdates);
    }

}
