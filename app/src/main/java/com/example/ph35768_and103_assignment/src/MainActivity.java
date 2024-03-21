package com.example.ph35768_and103_assignment.src;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.databinding.ActivityMainBinding;
import com.example.ph35768_and103_assignment.fragment.CartFragment;
import com.example.ph35768_and103_assignment.fragment.FavoriteFragment;
import com.example.ph35768_and103_assignment.fragment.HomeFragment;
import com.example.ph35768_and103_assignment.fragment.MeFragment;
import com.example.ph35768_and103_assignment.fragment.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        frameLayout = findViewById(R.id.fragment_container);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    loadFragment(new HomeFragment(),false);
                } else if (id == R.id.heart) {
                    loadFragment(new FavoriteFragment(),false);
                } else if (id == R.id.cart) {
                    loadFragment(new CartFragment(),false);
                } else if (id == R.id.noti) {
                    loadFragment(new NotificationFragment(),false);
                } else if (id == R.id.me) {
                    loadFragment(new MeFragment(),false);
                }
                loadFragment(new HomeFragment(), true);
                return true;
            }
        });
    }
    private void loadFragment(Fragment fragment, Boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isAppInitialized) {
            transaction.add(R.id.fragment_container, fragment);
        } else {
            transaction.replace(R.id.fragment_container, fragment);
        }
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }
}