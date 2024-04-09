package com.example.ph35768_and103_assignment.src;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.auth.LoginActivity;
import com.example.ph35768_and103_assignment.databinding.ActivityCheckOutBinding;
import com.example.ph35768_and103_assignment.model.Bill;
import com.example.ph35768_and103_assignment.model.Cart;
import com.example.ph35768_and103_assignment.model.Response;
import com.example.ph35768_and103_assignment.model.User;
import com.example.ph35768_and103_assignment.services.HttpRequest;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StatusDialogActionListener;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class CheckOutActivity extends AppCompatActivity {

    private ActivityCheckOutBinding binding;
    private SharedPreferences sharedPreferences;

    private User userData;
    private String email, phone;
    private String paymentMethod = "Cash";
    private HttpRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        httpRequest = new HttpRequest();

        getInfo();
        setUpToolbar();
        setUpAvatarUserLogin();

        binding.btnBuy.setOnClickListener(v -> {
            goToBuy();
        });

    }

    private void goToBuy() {
        // Get data from SharedPreferences
        SharedPreferences sharedPreferences1 = getSharedPreferences("Account", MODE_PRIVATE);
        String jsonUserData = sharedPreferences1.getString("userData", "");

        SharedPreferences sharedPreferences2 = getSharedPreferences("InfoBill", Context.MODE_PRIVATE);
        String totalCostJson = sharedPreferences2.getString("totalCost", "");
        String dsToJson = sharedPreferences2.getString("dsTo", "");

// Convert dsToJson and totalCostJson back to ArrayList<Cart> and double
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Cart>>() {}.getType();
        ArrayList<Cart> cart = gson.fromJson(dsToJson, type);
        double totalCost = gson.fromJson(totalCostJson, double.class);
        User userData = gson.fromJson(jsonUserData, User.class);

// Create a new Bill
        Bill bill = new Bill(paymentMethod, cart, userData.getId(), totalCost);

// Convert the Bill to JSON
        String billJson = gson.toJson(bill);

        if (billJson != null) {
            httpRequest.callApi().createBill(bill).enqueue(responseBuy);
        }
    }

    Callback<Response<Bill>> responseBuy = new Callback<Response<Bill>>() {
        @Override
        public void onResponse(Call<Response<Bill>> call, retrofit2.Response<Response<Bill>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Log.d("TAG", "onResponse: " + response.body().getData().getId());
                    aniDia();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Bill>> call, Throwable t) {

        }
    };

    private void aniDia() {
        PopupDialog.getInstance(this)
                .statusDialogBuilder()
                .createSuccessDialog()
                .setHeading("Well Done")
                .setDescription("You have successfully completed the task")
                .build(new StatusDialogActionListener() {
                    @Override
                    public void onStatusActionClicked(Dialog dialog) {
                        deleteAllCart();
                        Intent intent = new Intent(CheckOutActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteAllCart() {
        httpRequest.callApi().deleteAllCart().enqueue(new Callback<Response<Cart>>() {
            @Override
            public void onResponse(Call<Response<Cart>> call, retrofit2.Response<Response<Cart>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        Toast.makeText(CheckOutActivity.this, "Delete All Shoes In Cart Success", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<Cart>> call, Throwable t) {

            }
        });
    }

    private void getInfo() {
        sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);
        String jsonUserData = sharedPreferences.getString("userData", "");
        Gson gson = new Gson();
        userData = gson.fromJson(jsonUserData, User.class);
        binding.etEmail.setText(userData.getEmail());
        binding.etPhone.setText(userData.getPhone());


        sharedPreferences = getSharedPreferences("InfoBill", Context.MODE_PRIVATE);
        String totalCostJson = sharedPreferences.getString("totalCost", "");
        Gson gson1 = new Gson();
        double totalCost = gson1.fromJson(totalCostJson, double.class);
        binding.tvTotalCost.setText(totalCost + "");
    }

    private void setUpToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbarTitle.setText("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.avatarIcon.setOnClickListener(v -> {
            startActivity(new Intent(CheckOutActivity.this, AccountAndSettingActivity.class));
        });
        binding.icBack.setOnClickListener(v -> finish());

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

//    // Get data from SharedPreferences
//    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
//    String dsToJson = sharedPreferences.getString("dsTo", "");
//    String totalCostJson = sharedPreferences.getString("totalCost", "");
//
//    // Convert dsToJson and totalCostJson back to ArrayList<Cart> and double
//    Gson gson = new Gson();
//    Type type = new TypeToken<ArrayList<Cart>>() {}.getType();
//    ArrayList<Cart> dsTo = gson.fromJson(dsToJson, type);
//    double totalCost = gson.fromJson(totalCostJson, double.class);

}