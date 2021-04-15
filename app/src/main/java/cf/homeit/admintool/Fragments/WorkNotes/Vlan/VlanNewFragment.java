package cf.homeit.admintool.Fragments.WorkNotes.Vlan;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import cf.homeit.admintool.DataModels.User;
import cf.homeit.admintool.DataModels.Vlan;
import cf.homeit.admintool.R;

import static cf.homeit.admintool.ExtendsClases.SupportVoids.getTime;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.getUid;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;

public class VlanNewFragment extends Fragment {
    private static final String TAG = "NewSwitchActivity";
    private TextInputEditText vlanId, vlanName, vlanDescr, valnIpInterface, vlanSubDescr;
    private MaterialAutoCompleteTextView vlanType;

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
        return inflater.inflate(R.layout.fragment_vlan_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        init(view);
    }

    private void init(View view) {
        vlanId = view.findViewById(R.id.fieldVLANID);
        vlanName = view.findViewById(R.id.fieldVLANName);
        vlanType = view.findViewById(R.id.fieldVLANType);
        vlanDescr = view.findViewById(R.id.fieldVLANDescription);
        valnIpInterface = view.findViewById(R.id.fieldVLANIpInterface);
        vlanSubDescr = view.findViewById(R.id.fieldVLANSubDescription);
        saveBtn = view.findViewById(R.id.fabSaveVlan);
        saveBtn.setOnClickListener(view1 -> submitPost());
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.vlan_types_array, R.layout.item_drop_down);
        vlanType.setAdapter(arrayAdapter);
    }

    private void setEditingEnabled(boolean enabled) {
        vlanId.setEnabled(enabled);
        vlanName.setEnabled(enabled);
        vlanType.setEnabled(enabled);
        vlanDescr.setEnabled(enabled);
        valnIpInterface.setEnabled(enabled);
        vlanSubDescr.setEnabled(enabled);

        if (enabled) {
            saveBtn.show();
        } else {
            saveBtn.hide();
        }
    }

    private void submitPost() {
//        vlanIdStr,vlanNameStr,vlanTypeStr,vlanDescrStr,valnIpInterfaceStr,vlanSubDescrStr
//        vlanId,vlanName,vlanType,vlanDescr,valnIpInterface,vlanSubDescr
        final String vlanIdStr = vlanId.getText().toString();
        final String vlanNameStr = vlanName.getText().toString();
        final String vlanTypeStr = vlanType.getText().toString();
        final String vlanDescrStr = vlanDescr.getText().toString();
        final String valnIpInterfaceStr = valnIpInterface.getText().toString();
        final String vlanSubDescrStr = vlanSubDescr.getText().toString();

        final String dateTime = getTime();
        // Title is required
        if (TextUtils.isEmpty(vlanIdStr) || TextUtils.isEmpty(vlanNameStr)) {
//            titleEt.setError(REQUIRED);
            showToast(requireActivity().getApplicationContext(), "Vlan Id, name, is Required!!!");
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

                            writeNewPost(uid, vlanIdStr, vlanNameStr, vlanTypeStr, vlanDescrStr, valnIpInterfaceStr, vlanSubDescrStr, dateTime);
                            navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
                            navController.navigate(R.id.workNotesFragment);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);

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
//    vlanId,vlanName,vlanType,vlanDescr,valnIpInterface,vlanSubDescr,authorid,creationTime
    private void writeNewPost(String uid, String vlanId, String vlanName, String vlanType,
                              String vlanDescr, String valnIpInterface,
                              String vlanSubDescr, String time) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
//        String vlanId,String vlanName, String vlanType, String vlanDescr,
//                String valnIpInterface, String vlanSubDescr,String authorid,String creationTime
//        String key = mDatabase.child("notes").child(uid).push().getKey();
        Vlan model = new Vlan(vlanId, vlanName, vlanType, vlanDescr, valnIpInterface, vlanSubDescr, uid, time);
        Map<String, Object> postValues = model.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/vlans/" + vlanId, postValues);
        mDatabase.updateChildren(childUpdates);
    }

}