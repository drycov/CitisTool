package cf.homeit.admintool.Fragments.WorkNotes.Vlan;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import cf.homeit.admintool.DataModels.Vlan;
import cf.homeit.admintool.R;

import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_EDIT_KEY;
import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_VLAN_KEY;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.getTime;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.getUid;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;

public class VlanDetailFragment extends Fragment {
    private static final String TAG = "VlanDetailActivity";

    private TextInputEditText vlanId, vlanName, vlanDescr, valnIpInterface, vlanSubDescr, vlanAuthor;
    private MaterialAutoCompleteTextView vlanType;
    private FloatingActionButton fabAcceptChangesVlan;
    private String authorIdStr, username, email;
    private NavController navController;
    private ValueEventListener mPostListener, mAuthorListener;
    private DatabaseReference mPostReference, mDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_vlan_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String mPostKey = bundle.getString(EXTRA_VLAN_KEY);
        String ed = bundle.getString(EXTRA_EDIT_KEY);
        if (!ed.equals("edit")) {
            editsTextChanged();
        } else {
            vlanId.setEnabled(false);
            vlanId.setCursorVisible(false);
            vlanId.setBackgroundColor(Color.TRANSPARENT);
            vlanId.setKeyListener(null);
        }
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_VLAN_KEY");
        }
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("vlans").child(mPostKey);
        fabAcceptChangesVlan.setOnClickListener(v -> submitPost());
        //.child(authorIdStr)
    }


    private void init(View view) {
        vlanId = view.findViewById(R.id.vlanIdfd);
        vlanName = view.findViewById(R.id.vlanNamefd);
        vlanType = view.findViewById(R.id.vlanTypefd);
        vlanDescr = view.findViewById(R.id.vlanDescrfd);
        valnIpInterface = view.findViewById(R.id.valnIpInterfacefd);
        vlanSubDescr = view.findViewById(R.id.vlanSubDescrfd);
        fabAcceptChangesVlan = view.findViewById(R.id.fabAcceptChangesVlan);
        vlanAuthor = view.findViewById(R.id.vlanAuthor);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.vlan_types_array, R.layout.item_drop_down);
        vlanType.setAdapter(arrayAdapter);
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
                Vlan model = dataSnapshot.getValue(Vlan.class);
                vlanId.setText(model.vlanId);
                vlanName.setText(model.vlanName);
                vlanType.setText(model.vlanType);
                vlanDescr.setText(model.vlanDescr);
                valnIpInterface.setText(model.valnIpInterface);
                vlanSubDescr.setText(model.vlanSubDescr);
                authorIdStr = model.authorid;
                vlanAuthor.setText(model.authorid);
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
        mPostListener = postListener;
        mPostReference.addValueEventListener(postListener);
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
//        vlanDescr,valnIpInterface,vlanSubDescr,vlanType
//        vlanId,vlanName
        fabAcceptChangesVlan.setVisibility(View.GONE);
        vlanId.setEnabled(false);
        vlanId.setCursorVisible(false);
        vlanId.setBackgroundColor(Color.TRANSPARENT);
        vlanId.setKeyListener(null);
        vlanName.setEnabled(false);
        vlanName.setCursorVisible(false);
        vlanName.setBackgroundColor(Color.TRANSPARENT);
        vlanName.setKeyListener(null);

        vlanDescr.setEnabled(false);
        vlanDescr.setCursorVisible(false);
        vlanDescr.setBackgroundColor(Color.TRANSPARENT);
        vlanDescr.setKeyListener(null);
        valnIpInterface.setEnabled(false);
        valnIpInterface.setCursorVisible(false);
        valnIpInterface.setBackgroundColor(Color.TRANSPARENT);
        valnIpInterface.setKeyListener(null);
        vlanSubDescr.setEnabled(false);
        vlanSubDescr.setCursorVisible(false);
        vlanSubDescr.setBackgroundColor(Color.TRANSPARENT);
        vlanSubDescr.setKeyListener(null);
        vlanType.setEnabled(false);
        vlanType.setCursorVisible(false);
        vlanType.setBackgroundColor(Color.TRANSPARENT);
        vlanType.setKeyListener(null);
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
        setEditingEnabled();
        showToast(requireActivity().getApplicationContext(), "Posting...");


        // [START single_value_read]
        String uid = getUid(requireActivity().getApplicationContext());
        writeNewPost(uid, vlanIdStr, vlanNameStr, vlanTypeStr, vlanDescrStr, valnIpInterfaceStr, vlanSubDescrStr, dateTime);
        navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
        navController.navigate(R.id.workNotesFragment);
    }

    private void setEditingEnabled() {
        vlanId.setEnabled(false);
        vlanName.setEnabled(false);
        vlanType.setEnabled(false);
        vlanDescr.setEnabled(false);
        valnIpInterface.setEnabled(false);
        vlanSubDescr.setEnabled(false);

        if (false) {
            fabAcceptChangesVlan.show();
        } else {
            fabAcceptChangesVlan.hide();
        }
    }

    private void writeNewPost(String uid, String vlanId, String vlanName, String vlanType,
                              String vlanDescr, String valnIpInterface,
                              String vlanSubDescr, String time) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Vlan model = new Vlan(vlanId, vlanName, vlanType, vlanDescr, valnIpInterface, vlanSubDescr, uid, time);
        Map<String, Object> postValues = model.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/vlans/" + vlanId, postValues);
        mDatabase.updateChildren(childUpdates);
    }


}
