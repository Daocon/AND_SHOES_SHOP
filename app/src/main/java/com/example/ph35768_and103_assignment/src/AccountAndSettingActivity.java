package com.example.ph35768_and103_assignment.src;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.databinding.ActivityAccountAndSettingBinding;
import com.example.ph35768_and103_assignment.model.User;
import com.example.ph35768_and103_assignment.src.setting.EditProfileActivity;
import com.google.gson.Gson;

public class AccountAndSettingActivity extends AppCompatActivity {
    private ActivityAccountAndSettingBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountAndSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpToolbar();
        setUpAvatarUserLogin();
        setUpButtonInAccount();

        binding.btnLogout.setOnClickListener(v -> {
            sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(AccountAndSettingActivity.this, MainActivity.class));
            finish();
        });

    }

    private void setUpButtonInAccount() {
        binding.llChangeProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Change profile", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AccountAndSettingActivity.this, EditProfileActivity.class));
        });

        binding.llNotifySetting.setOnClickListener(v -> {
            Toast.makeText(this, "Notify setting", Toast.LENGTH_SHORT).show();
        });

        binding.llShippingAddress.setOnClickListener(v -> {
            Toast.makeText(this, "Shipping address", Toast.LENGTH_SHORT).show();
        });

        binding.llPaymentInfo.setOnClickListener(v -> {
            Toast.makeText(this, "Payment info", Toast.LENGTH_SHORT).show();
        });
        binding.llDeleteAccount.setOnClickListener(v -> {
            Toast.makeText(this, "Delete account", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(AccountAndSettingActivity.this, DeleteAccountActivity.class));
        });
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

        Glide.with(this)
                .load(newUrl)
                .thumbnail(Glide.with(this).load(R.drawable.loading))
                .centerCrop()
                .circleCrop()
                .skipMemoryCache(true)
                .into(binding.ivProfile);

    }

    private void setUpToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.icBack.setOnClickListener(v -> finish());
    }
}