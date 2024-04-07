package com.example.ph35768_and103_assignment.auth;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.databinding.ActivitySignUpBinding;
import com.example.ph35768_and103_assignment.init.InputValidator;
import com.example.ph35768_and103_assignment.model.Response;
import com.example.ph35768_and103_assignment.model.User;
import com.example.ph35768_and103_assignment.services.HttpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private HttpRequest httpRequest;
    File file;
    String email, password, confirmPassword, name, phone, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        httpRequest = new HttpRequest();

        binding.ivBack.setOnClickListener(v -> finish());
        binding.tvLogin.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
        binding.btSignUp.setOnClickListener(v -> signUp());
        binding.ivProfilePicture.setOnClickListener(v -> chooseImage());
        addTextWatchers();
    }

    private void signUp() {
        email = binding.etEmail.getText().toString().trim();
        password = binding.etPassword.getText().toString().trim();
        confirmPassword = binding.etConfirmPassword.getText().toString().trim();
        name = binding.etFullName.getText().toString().trim();
        phone = binding.etPhone.getText().toString().trim();
        address = binding.etAddress.getText().toString().trim();

        if (file == null) {
            Toast.makeText(this, "Please choose a profile picture", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody _email = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        RequestBody _password = RequestBody.create(MediaType.parse("multipart/form-data"), password);
        RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody _phone = RequestBody.create(MediaType.parse("multipart/form-data"), phone);
        RequestBody _address = RequestBody.create(MediaType.parse("multipart/form-data"), address);

        MultipartBody.Part multipartBody;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            multipartBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
        } else {
            multipartBody = null;
        }

        if (!areAllFieldsValid() || multipartBody == null){
            Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // log data
            Log.d("Dataaaaaaaaaaaaaaa", "Email: " + email+ " Password: " + password + " Name: " + name + " Phone: " + phone + " Address: " + address + " File: " + file);
            Log.d("123123", "onClick: " + multipartBody);
            httpRequest.callApi().register(_address, _email, multipartBody, _name, _password, _phone).enqueue(responseUser);
        }
    }

    Callback<Response<User>> responseUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200) {
                    Toast.makeText(SignUpActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Register fail", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            Log.e("Errorrrrrrrrrrrrrr", t.getMessage());
        }
    };

    private void chooseImage() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getImage.launch(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == Activity.RESULT_OK) {
                Intent data = o.getData();
                Uri uri = data.getData();
                file = createFileFromURI(uri, "avatar");
                Glide.with(SignUpActivity.this)
                        .load(file)
                        .thumbnail(Glide.with(SignUpActivity.this).load(R.drawable.loading))
                        .centerCrop()
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.ivProfilePicture);
            }
        }
    });

    private File createFileFromURI(Uri uri, String name) {
        File file = new File(SignUpActivity.this.getCacheDir(), name+".png");
        try {
            InputStream inputStream = SignUpActivity.this.getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addTextWatchers() {
        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Code to execute before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Code to execute when text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Code to execute after text has changed
                if (!InputValidator.isValidEmail(s.toString())) {
                    binding.etEmail.setError("Invalid email address");
                } else {
                    binding.etEmail.setError(null); // Clear the error when the input is valid
                }
            }
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Code to execute before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Code to execute when text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Code to execute after text has changed
                password = s.toString();
                if (!InputValidator.isValidPassword(password)) {
                    binding.etPassword.setError("Password must be at least 6 characters");
                } else {
                    binding.etPassword.setError(null); // Clear the error when the input is valid
                }
            }
        });

        binding.etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Code to execute before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Code to execute when text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Code to execute after text has changed
                if (!InputValidator.isPasswordMatch(password, s.toString())) {
                    binding.etConfirmPassword.setError("Passwords do not match");
                } else {
                    binding.etConfirmPassword.setError(null); // Clear the error when the input is valid
                }
            }
        });

        binding.etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Code to execute before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Code to execute when text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Code to execute after text has changed
                if (!InputValidator.isValidName(s.toString())) {
                    binding.etFullName.setError("Invalid name");
                } else {
                    binding.etFullName.setError(null); // Clear the error when the input is valid
                }
            }
        });

        binding.etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Code to execute before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Code to execute when text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Code to execute after text has changed
                if (!InputValidator.isValidPhone(s.toString())) {
                    binding.etPhone.setError("Invalid phone number");
                } else {
                    binding.etPhone.setError(null); // Clear the error when the input is valid
                }
            }
        });

        binding.etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Code to execute before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Code to execute when text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Code to execute after text has changed
                if (!InputValidator.isValidAddress(s.toString())) {
                    binding.etAddress.setError("Invalid address");
                } else {
                    binding.etAddress.setError(null); // Clear the error when the input is valid
                }
            }
        });
    }

    private boolean areAllFieldsValid() {
        return binding.etEmail.getError() == null &&
                binding.etPassword.getError() == null &&
                binding.etConfirmPassword.getError() == null &&
                binding.etFullName.getError() == null &&
                binding.etPhone.getError() == null &&
                binding.etAddress.getError() == null;
    }
}