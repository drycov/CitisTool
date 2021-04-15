package cf.homeit.admintool.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Field;

import cf.homeit.admintool.ExtendsClases.SharedPrefManager;
import cf.homeit.admintool.R;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Navigation.findNavController(this, R.id.first_nav_host);
        navController = Navigation.findNavController(MainActivity.this, R.id.first_nav_host);
        navController.navigate(R.id.todoListFragment);
    }


    @SuppressLint({"NonConstantResourceId", "RestrictedApi", "ClickableViewAccessibility"})
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
                case R.id.more:
                    PopupMenu popup = new PopupMenu(this, bottomNav);
                    popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) item -> {
                        switch (item.getItemId()) {
                            case R.id.settings:
                                navController = Navigation.findNavController(MainActivity.this, R.id.first_nav_host);
                                navController.navigate(R.id.settingsFragment);
                                return true;
                            case R.id.aboutMe:
                                navController = Navigation.findNavController(MainActivity.this, R.id.first_nav_host);
                                navController.navigate(R.id.aboutMeFragment);
                                return true;
                            case R.id.exit:
                                SharedPrefManager.getInstance(this).logout();
                                finish();
                        }
                        return false;
                    });
                    // here you can inflate your menu
                    popup.inflate(R.menu.popup_bottom_submenu);
                    popup.setGravity(Gravity.END);

                    // if you want icon with menu items then write this try-catch block.
                    try {
                        Field mFieldPopup = popup.getClass().getDeclaredField("mPopup");
                        mFieldPopup.setAccessible(true);
                        MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
                        mPopup.setForceShowIcon(true);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    popup.show();
                    return false;
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
        } else {
            setupBottomNavigation();
            bottomNav.setVisibility(View.GONE);
            navController.navigate(R.id.loginAuthFragment);
        }
    }
}

//