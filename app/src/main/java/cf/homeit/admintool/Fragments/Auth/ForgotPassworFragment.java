package cf.homeit.admintool.Fragments.Auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import cf.homeit.admintool.R;

public class ForgotPassworFragment extends Fragment {
    private TextInputEditText edtEmail;
    NavController navController;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_restore, container, false);

        edtEmail = view.findViewById(R.id.user_e_mail_editText);
        Button btnResetPassword = (Button) view.findViewById(R.id.letForgotPassword);
        LinearLayoutCompat btnBack = (LinearLayoutCompat) view.findViewById(R.id.forgot_back_button);

        mAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(v -> {

            String email = edtEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getActivity().getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity().getApplicationContext(), "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnBack.setOnClickListener(v -> {
            navController = Navigation.findNavController(getActivity(), R.id.first_nav_host);
            navController.navigate(R.id.loginAuthFragment);
        });
        return view;
    }

}