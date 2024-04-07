package com.example.ph35768_and103_assignment.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(v -> finish());

        binding.btContinue.setOnClickListener(v -> sendEmail());
    }

    private void sendEmail() {
        String email = binding.etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            binding.etEmail.setError("Email is required");
            return;
        }
        // send email
    }
    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}