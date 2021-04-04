package cf.homeit.admintool.Fragments.WorkNotes.Switch;

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

import cf.homeit.admintool.DataModels.Switch;
import cf.homeit.admintool.DataModels.User;
import cf.homeit.admintool.R;

import static cf.homeit.admintool.ExtendsClases.SupportVoids.getTime;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.getUid;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;

public class SwitchNewFragment extends Fragment {
    private static final String TAG = "NewSwitchActivity";
    private TextInputEditText switchIp,switchVendor,switchModel,switchSysName,switchLocation,switchDescr,switchSN,switchSubDescr;
    private MaterialAutoCompleteTextView switchType;

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
        return inflater.inflate(R.layout.fragment_switch_new,container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        init(view);
    }

    private void init(View view) {
        switchIp = view.findViewById(R.id.fieldSwitchIp);
        switchVendor = view.findViewById(R.id.fieldSwitchVendor);
        switchType = view.findViewById(R.id.fieldSwitchType);
        switchModel = view.findViewById(R.id.fieldSwitchModel);
        switchSysName = view.findViewById(R.id.fieldSwitchSysName);
        switchLocation = view.findViewById(R.id.fieldSwitchLocation);
        switchDescr = view.findViewById(R.id.fieldSwitchDescr);
        switchSN = view.findViewById(R.id.fieldSwitchSN);
        switchSubDescr = view.findViewById(R.id.fieldSwitchSubDescr);
        saveBtn = view.findViewById(R.id.fabSaveSwitch);
        saveBtn.setOnClickListener(view1 -> submitPost());
        ArrayAdapter<CharSequence> arrayAdapter =ArrayAdapter.createFromResource(requireActivity(),R.array.switch_types_array,R.layout.item_drop_down);
        switchType.setAdapter(arrayAdapter);
    }

    private void setEditingEnabled(boolean enabled) {
        switchIp.setEnabled(enabled);
        switchVendor.setEnabled(enabled);
        switchModel.setEnabled(enabled);
        switchSysName.setEnabled(enabled);
        switchLocation.setEnabled(enabled);
        switchDescr.setEnabled(enabled);
        switchSN.setEnabled(enabled);
        switchSubDescr.setEnabled(enabled);
        switchType.setEnabled(enabled);
        if (enabled) {
            saveBtn.show();
        } else {
            saveBtn.hide();
        }
    }

    private void submitPost() {
//        switchIpStr,switchVendorStr,switchModelStr,switchSysNameStr,switchLocationStr,switchDescrStr,switchSNStr,switchSubDescrStr
        final String switchIpStr = switchIp.getText().toString();
        final String switchVendorStr = switchVendor.getText().toString();
        final String switchModelStr = switchModel.getText().toString();
        final String switchSysNameStr = switchSysName.getText().toString();
        final String switchLocationStr = switchLocation.getText().toString();
        final String switchDescrStr = switchDescr.getText().toString();
        final String switchSNStr = switchSN.getText().toString();
        final String switchSubDescrStr = switchSubDescr.getText().toString();
        final String switchTypeStr = switchType.getText().toString();
//        final String switchVendorStr = contentEt.getText().toString();
        final String dateTime = getTime();
        // Title is required
        if (TextUtils.isEmpty(switchIpStr)||TextUtils.isEmpty(switchSysNameStr)||TextUtils.isEmpty(switchLocationStr)||TextUtils.isEmpty(switchSNStr)) {
//            titleEt.setError(REQUIRED);
            showToast(requireActivity().getApplicationContext(),"Switch IP, SN, System name, Location is Required!!!");
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
                            writeNewPost(uid, switchIpStr,switchTypeStr,switchVendorStr,switchModelStr,switchSysNameStr,switchLocationStr,switchDescrStr,switchSNStr,switchSubDescrStr, dateTime);
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
    private void writeNewPost(String uid, String switchIpStr,String switchTypeStr,String switchVendorStr,String switchModelStr,
                              String switchSysNameStr,String switchLocationStr,
                              String switchDescrStr,String switchSNStr,String switchSubDescrStr,String time) {

        String key = mDatabase.child("switches").child(uid).push().getKey();
        Switch model = new Switch(key, switchIpStr,switchTypeStr,switchVendorStr,switchModelStr,switchSysNameStr,switchLocationStr,switchDescrStr,switchSNStr,switchSubDescrStr, uid, time);
        Map<String, Object> postValues = model.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/switches/"  + switchSNStr, postValues);
        mDatabase.updateChildren(childUpdates);
    }

}

