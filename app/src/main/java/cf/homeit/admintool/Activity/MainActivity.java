package cf.homeit.admintool.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import cf.homeit.admintool.ExtendsClases.SharedPrefManager;
import cf.homeit.admintool.R;

import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Navigation.findNavController(this, R.id.first_nav_host);
        navController = Navigation.findNavController(MainActivity.this, R.id.first_nav_host);
    }


    private void setupBottomNavigation() {
        bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.note:
                    navController = Navigation.findNavController(MainActivity.this, R.id.first_nav_host);
                    navController.navigate(R.id.todoListFragment);
                    return true;
                case R.id.wn:
                    navController = Navigation.findNavController(MainActivity.this, R.id.first_nav_host);
                    navController.navigate(R.id.workNotesFragment);
                    return true;
                case R.id.nu:
                    navController = Navigation.findNavController(MainActivity.this, R.id.first_nav_host);
                    navController.navigate(R.id.calculateFragment);
                    return true;
                case R.id.settings:
                    navController = Navigation.findNavController(MainActivity.this, R.id.first_nav_host);
                    navController.navigate(R.id.settingsFragment);
                    return true;
                case R.id.exit:
                    SharedPrefManager.getInstance(this).logout();
                    finish();
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            navController.navigate(R.id.todoListFragment);
            setupBottomNavigation();
            bottomNav.setVisibility(View.VISIBLE);
        }else{
            setupBottomNavigation();
            bottomNav.setVisibility(View.GONE);
            navController.navigate(R.id.loginAuthFragment);
        }
    }
}
