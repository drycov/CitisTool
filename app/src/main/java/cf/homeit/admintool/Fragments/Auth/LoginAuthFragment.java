package cf.homeit.admintool.Fragments.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import cf.homeit.admintool.Activity.MainActivity;
import cf.homeit.admintool.DataModels.User;
import cf.homeit.admintool.ExtendsClases.AppPref;
import cf.homeit.admintool.ExtendsClases.Constants;
import cf.homeit.admintool.ExtendsClases.SharedPrefManager;
import cf.homeit.admintool.R;

public class LoginAuthFragment extends Fragment {
    private static final String TAG = LoginAuthFragment.class.getSimpleName();
    NavController navController;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextInputEditText userEmail,userPassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userEmail = view.findViewById(R.id.login_phone_number_editText);
        userPassword = view.findViewById(R.id.login_password_editText);
        view.findViewById(R.id.letTheUserLogIn).setOnClickListener(view1 -> {
            String email = String.valueOf(userEmail.getText()).trim();
            String password = String.valueOf(userPassword.getText()).trim();
            if (email.isEmpty()) {
                userEmail.setError(getString(R.string.userEmailError));
                userEmail.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                userEmail.setError(getString(R.string.userEmailError));
                userEmail.requestFocus();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("userEmail", email);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                Log.d(TAG,uid);
                                AppPref.setStringPref(requireActivity().getApplicationContext(),  Constants.SHARED_PREF_NAME_USER, Constants.TAG_UID, uid);

                                mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.getValue() == null) {
                                            navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
                                            navController.navigate(R.id.createProfileFragment);
                                        }else{
                                            getUserProfile(uid);
                                            AppPref.setStringPref(requireActivity().getApplicationContext(),  Constants.SHARED_PREF_NAME_USER, Constants.TAG_UID, uid);
                                            SharedPrefManager.getInstance(requireActivity().getApplicationContext()).uidWrite(uid);
//                                            navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
//                                            navController.navigate(R.id.todoListFragment);
                                            requireActivity().finish();
                                            startActivity(new Intent(requireActivity().getApplicationContext(), MainActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.w(TAG, "addListenerForSingleValueEvent:failure", error.toException());
                                    }
                                });

                            }



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(requireActivity().getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        view.findViewById(R.id.letTheForgotPassword).setOnClickListener(v -> {
            navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
            navController.navigate(R.id.forgotPassworFragment);
        });
    }

    private void getUserProfile(String uid){
        mDatabase.child("users").child(uid).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {

                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                Log.d(TAG, String.valueOf(task.getResult()));
                String middleName =  String.valueOf(task.getResult().child("middleName").getValue());
                String eMail =  String.valueOf(task.getResult().child("eMail").getValue());
                String lastName =  String.valueOf(task.getResult().child("lastName").getValue());
                String firstName =  String.valueOf(task.getResult().child("firstName").getValue());
                String userPhone = String.valueOf(task.getResult().child("phoneNumber").getValue());
                String creationTime = String.valueOf(task.getResult().child("creationTime").getValue());
                String appName = requireActivity().getApplicationContext().getApplicationInfo().loadLabel(requireActivity().getApplicationContext().getPackageManager()).toString();
                AppPref.setStringPref(requireActivity().getApplicationContext(), uid, Constants.KEY_MNAME,middleName);
                AppPref.setStringPref(requireActivity().getApplicationContext(), uid, Constants.KEY_EMAIL, eMail);
                AppPref.setStringPref(requireActivity().getApplicationContext(), uid, Constants.KEY_LNAME, lastName);
                AppPref.setStringPref(requireActivity().getApplicationContext(), uid, Constants.KEY_FNAME, firstName);
                AppPref.setStringPref(requireActivity().getApplicationContext(), uid, Constants.KEY_PHONE_NUM, userPhone);
                AppPref.setStringPref(requireActivity().getApplicationContext(), uid, Constants.KEY_CREATION_TIME, creationTime);
                AppPref.setStringPref(requireActivity().getApplicationContext(), uid, Constants.APP_NAME, appName);
                new User(
                         firstName, lastName,
                        middleName, eMail, userPhone,creationTime); }
        });
    }
}