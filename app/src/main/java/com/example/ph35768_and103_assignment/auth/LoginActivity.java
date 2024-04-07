package com.example.ph35768_and103_assignment.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.ph35768_and103_assignment.databinding.ActivityLoginBinding;
import com.example.ph35768_and103_assignment.init.InputValidator;
import com.example.ph35768_and103_assignment.model.Response;
import com.example.ph35768_and103_assignment.model.User;
import com.example.ph35768_and103_assignment.services.HttpRequest;
import com.example.ph35768_and103_assignment.src.MainActivity;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private HttpRequest httpRequest;
    String email , password;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addTextWatcher();
        checkRememberMe();

        binding.ivBack.setOnClickListener(v -> finish());
        binding.tvForgotPassword.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        binding.tvSignUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        binding.btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }

    private void checkRememberMe() {
        sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");

        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            binding.etEmail.setText(savedEmail);
            binding.etPassword.setText(savedPassword);
            //set checkbox remember me checked
            binding.checkboxRememberMe.setChecked(true);
        }
    }

    private void signIn() {
        email = binding.etEmail.getText().toString().trim();
        password = binding.etPassword.getText().toString().trim();
        //Toast.makeText(this, "email:"+email+password, Toast.LENGTH_SHORT).show();

        if (!areAllFieldsValid()) {
            Toast.makeText(this, "Please check all error!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            User userLogin = new User();
            userLogin.setEmail(email);
            userLogin.setPassword(password);
            httpRequest = new HttpRequest();
            httpRequest.callApi().login(userLogin).enqueue(responseUser);
        }
    }

    Callback<Response<User>> responseUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200) {

                    Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                    User userData = response.body().getData();
                    // Convert User object to JSON String
                    Gson gson = new Gson();
                    String jsonUserData = gson.toJson(userData);

                    sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.putString("refreshToken", response.body().getRefreshToken());
                    editor.putString("userData", jsonUserData);
                    if (binding.checkboxRememberMe.isChecked()) {
                        editor.putString("email", userData.getEmail());
                        editor.putString("password", userData.getPassword());
                    }
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Login fail", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    };

    private void addTextWatcher() {
        //use inputvalidation class to validate email and password
        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!InputValidator.isValidPassword(editable.toString())) {
                    binding.etPassword.setError("Password must be at least 6 characters");
                } else {
                    binding.etPassword.setError(null); // Clear the error when the input is valid
                }
            }
        });
        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password = editable.toString();
                if (!InputValidator.isValidPassword(password)) {
                    binding.etPassword.setError("Password must be at least 6 characters");
                } else {
                    binding.etPassword.setError(null); // Clear the error when the input is valid
                }
            }
        });
    }

    private boolean areAllFieldsValid() {
        return binding.etEmail.getError() == null &&
                binding.etPassword.getError() == null;
    }
}