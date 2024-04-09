package com.example.ph35768_and103_assignment.src;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.auth.LoginActivity;
import com.example.ph35768_and103_assignment.databinding.ActivityAccountAndSettingBinding;
import com.example.ph35768_and103_assignment.model.Response;
import com.example.ph35768_and103_assignment.model.User;
import com.example.ph35768_and103_assignment.services.HttpRequest;
import com.example.ph35768_and103_assignment.src.setting.DeleteAccountActivity;
import com.example.ph35768_and103_assignment.src.setting.EditProfileActivity;
import com.google.gson.Gson;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;
import com.saadahmedev.popupdialog.listener.StatusDialogActionListener;

import retrofit2.Call;
import retrofit2.Callback;

public class AccountAndSettingActivity extends AppCompatActivity {
    private ActivityAccountAndSettingBinding binding;
    private SharedPreferences sharedPreferences;

    private User userDataUpdate;
    private HttpRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountAndSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        httpRequest = new HttpRequest();

        setUpToolbar();
        setUpButtonInAccount();
        loadUserDataAndUpdateUI();

        binding.btnLogout.setOnClickListener(v -> {
            PopupDialog.getInstance(this)
                    .standardDialogBuilder()
                    .createIOSDialog()
                    .setHeading("Logout")
                    .setDescription("Are you sure you want to logout?" +
                            " This action cannot be undone")
                    .build(new StandardDialogActionListener() {
                        @Override
                        public void onPositiveButtonClicked(Dialog dialog) {
                            sharedPreferences.edit().clear().apply();
                            startActivity(new Intent(AccountAndSettingActivity.this, LoginActivity.class));
                            finish();
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegativeButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
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
            PopupDialog.getInstance(this)
                    .standardDialogBuilder()
                    .createIOSDialog()
                    .setHeading("Delete")
                    .setDescription("Are you sure you want to logout?" +
                            " This action cannot be undone")
                    .build(new StandardDialogActionListener() {
                        @Override
                        public void onPositiveButtonClicked(Dialog dialog) {
                            httpRequest.callApi().deleteUser(userDataUpdate.getId()).enqueue(responseDelete);
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegativeButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        });
    }

    Callback<Response<User>> responseDelete = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    sharedPreferences.edit().clear().apply();
                    aniOpen();
                } else {
                    Toast.makeText(AccountAndSettingActivity.this, "Delete fail", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            Log.d(">>>> Distributor", "onFailure: " + t.getMessage());
        }
    };

    private void aniOpen() {
        PopupDialog.getInstance(this)
                .statusDialogBuilder()
                .createSuccessDialog()
                .setHeading("Well Done")
                .setDescription("You have successfully completed the task")
                .build(new StatusDialogActionListener() {
                    @Override
                    public void onStatusActionClicked(Dialog dialog) {
                        startActivity(new Intent(AccountAndSettingActivity.this, LoginActivity.class));
                        finish();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void setUpToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.icBack.setOnClickListener(v -> finish());
    }

    private void loadUserDataAndUpdateUI() {
        sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);
        String jsonUserData = sharedPreferences.getString("userData", "");

        if (!jsonUserData.isEmpty()) {
            Gson gson = new Gson();
            userDataUpdate = gson.fromJson(jsonUserData, User.class);
            updateAvatar(userDataUpdate.getImage());
        }
    }

    private void updateAvatar(String imageUrl) {
        String newUrl = imageUrl.replace("localhost", "10.0.2.2");
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


    @Override
    protected void onResume() {
        super.onResume();
        loadUserDataAndUpdateUI();
    }
}