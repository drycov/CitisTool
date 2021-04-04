package cf.homeit.admintool.Fragments.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

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

import cf.homeit.admintool.Activity.MainActivity;
import cf.homeit.admintool.DataModels.User;
import cf.homeit.admintool.DataModels.Vlan;
import cf.homeit.admintool.R;

import static cf.homeit.admintool.ExtendsClases.SupportVoids.getTime;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.getUid;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;

public class CreateProfileFragment extends Fragment {
    private static final String TAG = "CreateProfileFragment";
    private TextInputEditText firstName, lastName,middleName, phoneNumber,eMail;


    private DatabaseReference mDatabase;
    private Button saveBtn;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_auth_profile,container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        init(view);
    }

    private void init(View view) {
//        firstName, lastName,middleName, phoneNumber,eMail;
        firstName = view.findViewById(R.id.profile_f_name_editText);
        lastName = view.findViewById(R.id.profile_l_name_editText);
        middleName = view.findViewById(R.id.profile_m_name_editText);
        phoneNumber = view.findViewById(R.id.profile_user_phone);
        eMail = view.findViewById(R.id.profile_e_mail_editText);
        saveBtn = view.findViewById(R.id.letTheCreateUserProfile);
        saveBtn.setOnClickListener(view1 -> submitPost());
    }

    private void submitPost() {

        final String firstNameStr = firstName.getText().toString();
        final String lastNameStr = lastName.getText().toString();
        final String middleNameStr = middleName.getText().toString();
        final String phoneNumbreStr = phoneNumber.getText().toString();
        final String eMailStr = eMail.getText().toString();


        final String dateTime = getTime();
        // Title is required
        if (TextUtils.isEmpty(firstNameStr)||TextUtils.isEmpty(lastNameStr)||TextUtils.isEmpty(middleNameStr)||TextUtils.isEmpty(phoneNumbreStr)||TextUtils.isEmpty(eMailStr)) {
//            titleEt.setError(REQUIRED);
            showToast(requireActivity().getApplicationContext(),"All fields is Required!!!");
            return;
        }

        // Disable button so there are no multi-posts
        showToast(requireActivity().getApplicationContext(), "Posting...");
        String uid = getUid(requireActivity().getApplicationContext());
        writeNewUser(uid,firstNameStr, lastNameStr,middleNameStr,eMailStr ,phoneNumbreStr, dateTime);
        // Finish this Activity, back to the stream
//        navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
//        navController.navigate(R.id.todoListFragment);
        requireActivity().finish();
        startActivity(new Intent(requireActivity().getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // [END single_value_read]
    }
    // [START write_fan_out]
    private void writeNewPost(String uid, String a,String b,String c,
                              String d,String e,String time) {

        User model = new User(a, b,c,d,e, time);
        Map<String, Object> postValues = model.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/"  + uid, postValues);
        mDatabase.updateChildren(childUpdates);
    }

    private void writeNewUser(String uid, String firstName, String lastName, String middleName, String eMail,String phoneNumber, String dateTim) {
        User user = new User(firstName,lastName,middleName,eMail,phoneNumber,dateTim);
        mDatabase.child("users").child(uid).setValue(user);
    }

}
