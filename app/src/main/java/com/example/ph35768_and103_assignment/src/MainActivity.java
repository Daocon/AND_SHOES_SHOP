package com.example.ph35768_and103_assignment.src;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.databinding.ActivityMainBinding;
import com.example.ph35768_and103_assignment.fragment.CartFragment;
import com.example.ph35768_and103_assignment.fragment.FavoriteFragment;
import com.example.ph35768_and103_assignment.fragment.HomeFragment;
import com.example.ph35768_and103_assignment.fragment.NotificationFragment;
import com.example.ph35768_and103_assignment.model.User;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FrameLayout frameLayout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        frameLayout = findViewById(R.id.fragment_container);

        setUpAvatarUserLogin();
        setUpToolbar();
        setUpBottomNavigationView();

        loadFragment(new HomeFragment(), false);
        binding.toolbarTitle.setText("");
    }

    private void setUpAvatarUserLogin() {
        sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);
        String jsonUserData = sharedPreferences.getString("userData", "");

        Gson gson = new Gson();
        User userData = gson.fromJson(jsonUserData, User.class);
        String url = userData.getImage();
        Log.d("TAG", "setUpAvatarUserLogin: " + url);
        String newUrl = url.replace("localhost", "10.0.2.2");
        Glide.with(this)
                .load(newUrl)
                .thumbnail(Glide.with(this).load(R.drawable.loading))
                .centerCrop()
                .circleCrop()
                .skipMemoryCache(true)
                .into(binding.avatarIcon);
    }

    private void setUpToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.avatarIcon.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AccountAndSettingActivity.class));
        });
    }

    private void setUpBottomNavigationView() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                binding.toolbarTitle.setText("");
                loadFragment(new HomeFragment(),false);
            } else if (id == R.id.heart) {
                binding.toolbarTitle.setText("Favorite");
                loadFragment(new FavoriteFragment(),false);
            } else if (id == R.id.cart) {
                binding.toolbarTitle.setText("Cart");
                loadFragment(new CartFragment(),false);
            } else if (id == R.id.noti) {
                binding.toolbarTitle.setText("Notification");
                loadFragment(new NotificationFragment(),false);
            }
            return true;
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
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpAvatarUserLogin();
    }
}