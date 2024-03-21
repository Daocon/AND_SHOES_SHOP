package com.example.ph35768_and103_assignment.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.databinding.ActivitySignUpBinding;
import com.example.ph35768_and103_assignment.src.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(v -> finish());
        binding.tvLogin.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));

        auth = FirebaseAuth.getInstance();
        binding.btSignUp.setOnClickListener(v -> signUp());


    }

    private void signUp() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            binding.etEmail.setError("Email is required");
            binding.etPassword.setError("Password is required");
            binding.etConfirmPassword.setError("Confirm password is required");
            return;
        }
        if (password.length() < 6) {
            binding.etPassword.setError("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(confirmPassword)) {
            binding.etConfirmPassword.setError("Passwords do not match");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            } else {
                binding.etEmail.setError("Invalid email address");
                binding.etPassword.setError("Password must be at least 6 characters");
            }
        });
    }
}