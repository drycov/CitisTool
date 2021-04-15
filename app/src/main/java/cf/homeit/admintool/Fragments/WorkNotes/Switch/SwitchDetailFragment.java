package cf.homeit.admintool.Fragments.WorkNotes.Switch;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import cf.homeit.admintool.DataModels.Switch;
import cf.homeit.admintool.DataModels.Vlan;
import cf.homeit.admintool.R;

import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_EDIT_KEY;
import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_NOTE_KEY;
import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_SWITCH_KEY;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.getTime;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.getUid;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;

public class SwitchDetailFragment extends Fragment {
    private static final String TAG = "SwitchDetailActivity";
    private TextInputEditText switchPortCountAll, switchSN, switchSysName, switchVendor, switchModel, switchIp, switchLocation, switchDescr, switchSubDescr;
    private MaterialAutoCompleteTextView switchTypeItem;
    private ValueEventListener mPostListener;
    private FloatingActionButton saveBtn;
    private DatabaseReference mPostReference, mDatabase;
    private String mPostKey;
    NavController navController;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_switch_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        Bundle bundle = this.getArguments();
        mPostKey = bundle.getString(EXTRA_SWITCH_KEY);
        boolean ed = bundle.getBoolean(EXTRA_EDIT_KEY);
        if (!ed) {
            editsTextChanged();
        } else {
            switchSN.setEnabled(false);
            switchSN.setCursorVisible(false);
            switchSN.setBackgroundColor(Color.TRANSPARENT);
            switchSN.setKeyListener(null);
        }
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_NOTE_KEY");
        }
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("switches").child(mPostKey);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        saveBtn.setOnClickListener(v -> {
            submitPost();
        });
        switchPortCountAll.setOnClickListener(v -> {
            bundle.putString(EXTRA_SWITCH_KEY, mPostKey);
            navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
            navController.navigate(R.id.switchInterfaceFragment, bundle);
        });
    }

    private void init(View view) {
        switchSN = view.findViewById(R.id.switchSN);
        switchTypeItem = view.findViewById(R.id.switchTypeItem);
        switchSysName = view.findViewById(R.id.switchSysName);
        switchVendor = view.findViewById(R.id.switchVendor);
        switchModel = view.findViewById(R.id.switchModel);
        switchIp = view.findViewById(R.id.switchIp);
        switchLocation = view.findViewById(R.id.switchLocation);
        switchDescr = view.findViewById(R.id.switchDescr);
        switchSubDescr = view.findViewById(R.id.switchSubDescr);
        saveBtn = view.findViewById(R.id.svitchDetailSave);
        switchPortCountAll = view.findViewById(R.id.portStatusAll);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.switch_types_array, R.layout.item_drop_down);
        switchTypeItem.setAdapter(arrayAdapter);
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
                Switch am = dataSnapshot.getValue(Switch.class);
                // [START_EXCLUDE]
                if (am != null) {
                    switchSN.setText(am.switchSN);
                    switchSysName.setText(am.switchSysName);
                    switchTypeItem.setText(am.switchType);
                    switchVendor.setText(am.switchVendor);
                    switchModel.setText(am.switchModel);
                    switchIp.setText(am.switchIp);
                    switchLocation.setText(am.switchLocation);
                    switchDescr.setText(am.switchDescr);
                    switchSubDescr.setText(am.switchSubDescr);
                }

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

    public void editsTextChanged() {
        saveBtn.setVisibility(View.GONE);
        switchSN.setEnabled(false);
        switchSN.setCursorVisible(false);
        switchSN.setBackgroundColor(Color.TRANSPARENT);
        switchSN.setKeyListener(null);
        switchTypeItem.setEnabled(false);
        switchTypeItem.setCursorVisible(false);
        switchTypeItem.setBackgroundColor(Color.TRANSPARENT);
        switchTypeItem.setKeyListener(null);
        switchSysName.setEnabled(false);
        switchSysName.setCursorVisible(false);
        switchSysName.setBackgroundColor(Color.TRANSPARENT);
        switchSysName.setKeyListener(null);

        switchVendor.setEnabled(false);
        switchVendor.setCursorVisible(false);
        switchVendor.setBackgroundColor(Color.TRANSPARENT);
        switchVendor.setKeyListener(null);
        switchModel.setEnabled(false);
        switchModel.setCursorVisible(false);
        switchModel.setBackgroundColor(Color.TRANSPARENT);
        switchModel.setKeyListener(null);
        switchIp.setEnabled(false);
        switchIp.setCursorVisible(false);
        switchIp.setBackgroundColor(Color.TRANSPARENT);
        switchIp.setKeyListener(null);
        switchLocation.setEnabled(false);
        switchLocation.setCursorVisible(false);
        switchLocation.setBackgroundColor(Color.TRANSPARENT);
        switchLocation.setKeyListener(null);
        switchDescr.setEnabled(false);
        switchDescr.setCursorVisible(false);
        switchDescr.setBackgroundColor(Color.TRANSPARENT);
        switchDescr.setKeyListener(null);
        switchSubDescr.setEnabled(false);
        switchSubDescr.setCursorVisible(false);
        switchSubDescr.setBackgroundColor(Color.TRANSPARENT);
        switchSubDescr.setKeyListener(null);
    }

    private void submitPost() {

        final String switchIpStr = switchIp.getText().toString();
        final String switchVendorStr = switchVendor.getText().toString();
        final String switchModelStr = switchModel.getText().toString();
        final String switchSysNameStr = switchSysName.getText().toString();
        final String switchLocationStr = switchLocation.getText().toString();
        final String switchDescrStr = switchDescr.getText().toString();
        final String switchSNStr = switchSN.getText().toString();
        final String switchSubDescrStr = switchSubDescr.getText().toString();
        final String switchTypeStr = switchTypeItem.getText().toString();
        final String dateTime = getTime();
        if (TextUtils.isEmpty(switchIpStr) || TextUtils.isEmpty(switchSysNameStr) || TextUtils.isEmpty(switchLocationStr) || TextUtils.isEmpty(switchSNStr)) {
//            titleEt.setError(REQUIRED);
            showToast(requireActivity().getApplicationContext(), "Switch IP, SN, System name, Location is Required!!!");
            return;
        }

        // Disable button so there are no multi-posts
        showToast(requireActivity().getApplicationContext(), "Posting...");
        // [START single_value_read]
        String uid = getUid(requireActivity().getApplicationContext());
        writeNewPost(uid, switchIpStr, switchTypeStr, switchVendorStr, switchModelStr, switchSysNameStr, switchLocationStr, switchDescrStr, switchSNStr, switchSubDescrStr, dateTime);
        navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
        navController.navigate(R.id.workNotesFragment);
    }


    private void writeNewPost(String uid, String switchIpStr, String switchTypeStr, String switchVendorStr, String switchModelStr,
                              String switchSysNameStr, String switchLocationStr,
                              String switchDescrStr, String switchSNStr, String switchSubDescrStr, String time) {

        String key = mDatabase.child("switches").child(uid).push().getKey();
        Switch model = new Switch(key, switchIpStr, switchTypeStr, switchVendorStr, switchModelStr, switchSysNameStr, switchLocationStr, switchDescrStr, switchSNStr, switchSubDescrStr, uid, time);
        Map<String, Object> postValues = model.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/switches/" + switchSNStr, postValues);
        mDatabase.updateChildren(childUpdates);

    }


}
