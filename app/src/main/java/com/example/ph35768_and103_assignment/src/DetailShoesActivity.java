package com.example.ph35768_and103_assignment.src;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.databinding.ActivityDetailShoesBinding;
import com.example.ph35768_and103_assignment.fragment.CartFragment;
import com.example.ph35768_and103_assignment.model.Cart;
import com.example.ph35768_and103_assignment.model.Response;
import com.example.ph35768_and103_assignment.model.Shoe;
import com.example.ph35768_and103_assignment.model.User;
import com.example.ph35768_and103_assignment.services.HttpRequest;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DetailShoesActivity extends AppCompatActivity {

    private ActivityDetailShoesBinding binding;
    private HttpRequest httpRequest;
    private SharedPreferences sharedPreferences;
    private Shoe currentShoe;
    private String size, color;
    private String quantity = "1";
    private Shoe clickedShoe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailShoesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        httpRequest = new HttpRequest();
        currentShoe = new Shoe();
        setUpToolbar();
        setUpAvatarUserLogin();
        getDataFromBundle();
        setOnclick();

        binding.btAddToCart.setOnClickListener(v -> {
            if (size == null || color == null) {
                Toast.makeText(this, "Please choose size and color", Toast.LENGTH_SHORT).show();
                return;
            }
            // Add the shoe to the cart
            Cart cart = new Cart(size, color, quantity, clickedShoe.getId());
            httpRequest.callApi().addToCart(cart).enqueue(responseAddToCart);
//            Toast.makeText(this, "Added to cart"+size+"-"+color, Toast.LENGTH_SHORT).show();
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbarTitle.setText("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.avatarIcon.setOnClickListener(v -> {
            startActivity(new Intent(DetailShoesActivity.this, AccountAndSettingActivity.class));
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

    Callback<Response<Cart>> responseAddToCart = new Callback<Response<Cart>>() {
        @Override
        public void onResponse(Call<Response<Cart>> call, retrofit2.Response<Response<Cart>> response) {
            if (response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    Toast.makeText(DetailShoesActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
//                    CartFragment cartFragment = new CartFragment();
//
//                    // Get the FragmentManager and start a transaction
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                    // Replace the container with the new fragment
//                    fragmentTransaction.replace(R.id.fragment_container, cartFragment);
//
//                    // Commit the transaction
//                    fragmentTransaction.commit();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Cart>> call, Throwable t) {

        }
    };

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Get the Shoe object from the Bundle
            clickedShoe = (Shoe) bundle.getSerializable("clickedShoe");

            if (!clickedShoe.getAvatar().isEmpty()){
                String url = clickedShoe.getAvatar();
                String newUrl = url.replace("localhost", "10.0.2.2");
                Glide.with(this)
                        .load(newUrl)
                        .thumbnail(Glide.with(this).load(R.drawable.loading))
                        .skipMemoryCache(true)
                        .into(binding.ivShoes);
            }
            binding.tvShoesName.setText(clickedShoe.getName());
            binding.tvShoesDescription.setText(clickedShoe.getDescription());
        }
    }

    private void setOnclick() {
        binding.btnShoesColorRed.setOnClickListener(v -> {
            resetColorButtons();
            color = "Red";
            binding.btnShoesColorRed.setBackground(getResources().getDrawable(R.drawable.bg_btn_color_rian_red));
        });
        binding.btnShoesColorBlue.setOnClickListener(v -> {
            resetColorButtons();
            color = "Blue";
            binding.btnShoesColorBlue.setBackground(getResources().getDrawable(R.drawable.bg_btn_color_rian_blue));
        });

        binding.btnShoesColorBlack.setOnClickListener(v -> {
            resetColorButtons();
            color = "Black";
            binding.btnShoesColorBlack.setBackground(getResources().getDrawable(R.drawable.bg_btn_color_rian_black));
        });

        binding.btnShoesColorWhite.setOnClickListener(v -> {
            resetColorButtons();
            color = "White";
            binding.btnShoesColorWhite.setBackground(getResources().getDrawable(R.drawable.bg_btn_color_rian_white));
        });

        // Set onClickListeners for size buttons
        binding.btnShoesSize32.setOnClickListener(v -> {
            resetSizeButtons();
            size = "32"; // Update the size variable
            binding.btnShoesSize32.setBackgroundResource(R.drawable.bg_btn_color_rian_white);
        });

        binding.btnShoesSize34.setOnClickListener(v -> {
            resetSizeButtons();
            size = "33"; // Update the size variable
            binding.btnShoesSize34.setBackgroundResource(R.drawable.bg_btn_color_rian_white);
        });

        binding.btnShoesSize35.setOnClickListener(v -> {
            resetSizeButtons();
            size = "35"; // Update the size variable
            binding.btnShoesSize35.setBackgroundResource(R.drawable.bg_btn_color_rian_white);
        });

        binding.btnShoesSize36.setOnClickListener(v -> {
            resetSizeButtons();
            size = "36"; // Update the size variable
            binding.btnShoesSize36.setBackgroundResource(R.drawable.bg_btn_color_rian_white);
        });

        binding.btnShoesSize37.setOnClickListener(v -> {
            resetSizeButtons();
            size = "37"; // Update the size variable
            binding.btnShoesSize37.setBackgroundResource(R.drawable.bg_btn_color_rian_white);
        });

        binding.btnShoesSize38.setOnClickListener(v -> {
            resetSizeButtons();
            size = "38"; // Update the size variable
            binding.btnShoesSize38.setBackgroundResource(R.drawable.bg_btn_color_rian_white);
        });
    }

    private void resetColorButtons() {
        binding.btnShoesColorRed.setBackground(getResources().getDrawable(R.drawable.bg_btn_color_red));
        binding.btnShoesColorBlue.setBackground(getResources().getDrawable(R.drawable.bg_btn_color_blue));
        binding.btnShoesColorBlack.setBackground(getResources().getDrawable(R.drawable.bg_btn_color_black));
        binding.btnShoesColorWhite.setBackground(getResources().getDrawable(R.drawable.bg_btn_color_white));
    }

    private void resetSizeButtons() {
        binding.btnShoesSize32.setBackgroundResource(R.drawable.bg_btn_color_white);
        binding.btnShoesSize34.setBackgroundResource(R.drawable.bg_btn_color_white);
        binding.btnShoesSize35.setBackgroundResource(R.drawable.bg_btn_color_white);
        binding.btnShoesSize36.setBackgroundResource(R.drawable.bg_btn_color_white);
        binding.btnShoesSize37.setBackgroundResource(R.drawable.bg_btn_color_white);
        binding.btnShoesSize38.setBackgroundResource(R.drawable.bg_btn_color_white);
    }
}