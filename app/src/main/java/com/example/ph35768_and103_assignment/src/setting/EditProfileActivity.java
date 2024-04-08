package com.example.ph35768_and103_assignment.src.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.auth.SignUpActivity;
import com.example.ph35768_and103_assignment.databinding.ActivityEditProfileBinding;
import com.example.ph35768_and103_assignment.init.InputValidator;
import com.example.ph35768_and103_assignment.model.Response;
import com.example.ph35768_and103_assignment.model.User;
import com.example.ph35768_and103_assignment.services.HttpRequest;
import com.google.gson.Gson;

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

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    String email, password, name, phone, address;
    File file;
    private HttpRequest httpRequest;

    private SharedPreferences sharedPreferences;
    User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        httpRequest = new HttpRequest();

        addTextWatchers();
        setDataUserToView();

        binding.ivBack.setOnClickListener(view -> {
            finish();
        });

        binding.btUpdate.setOnClickListener(view -> {
            updateInfor();
        });

        binding.ivProfilePicture.setOnClickListener(v -> chooseImage());
    }

    private void setDataUserToView() {
        sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);
        String jsonUserData = sharedPreferences.getString("userData", "");

        if (!jsonUserData.isEmpty()) {
            Gson gson = new Gson();
            userData = gson.fromJson(jsonUserData, User.class);

            binding.etEmail.setText(userData.getEmail());
            binding.etPassword.setText(userData.getPassword());
            binding.etFullName.setText(userData.getName());
            binding.etPhone.setText(userData.getPhone());
            binding.etAddress.setText(userData.getAddress());

            String url = userData.getImage();
            Log.d("TAG", "setUpAvatarUserLogin: " + url);
            String newUrl = url.replace("localhost", "10.0.2.2");
            Glide.with(this)
                    .load(newUrl)
                    .thumbnail(Glide.with(this).load(R.drawable.loading))
                    .centerCrop()
                    .circleCrop()
                    .skipMemoryCache(true)
                    .into(binding.ivProfilePicture);
            binding.tvChooseProfilePicture.setVisibility(View.GONE);
        }
    }

    private void updateInfor() {
        if (!areAllFieldsValid()) {
            Toast.makeText(this, "Please check again!", Toast.LENGTH_SHORT).show();
        } else {
            email = binding.etEmail.getText().toString();
            password = binding.etPassword.getText().toString();
            name = binding.etFullName.getText().toString();
            phone = binding.etPhone.getText().toString();
            address = binding.etAddress.getText().toString();

            // Create RequestBody instances for the user information
            RequestBody emailBody = RequestBody.create(MediaType.parse("multipart/form-data"), email);
            RequestBody passwordBody = RequestBody.create(MediaType.parse("multipart/form-data"), password);
            RequestBody nameBody = RequestBody.create(MediaType.parse("multipart/form-data"), name);
            RequestBody phoneBody = RequestBody.create(MediaType.parse("multipart/form-data"), phone);
            RequestBody addressBody = RequestBody.create(MediaType.parse("multipart/form-data"), address);

            // Create a MultipartBody.Part for the image file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

            // Call API to update user information and profile picture
            httpRequest.callApi().updateUserWithImage(addressBody,emailBody,body, nameBody,passwordBody, phoneBody ,userData.getId()).enqueue(responseUser);
        }
    }

    Callback<Response<User>> responseUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(EditProfileActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                    // Convert User object to JSON String
                    Gson _gson = new Gson();
                    String jsonUserData = _gson.toJson(response.body().getData());

                    sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userData", jsonUserData);
                    editor.apply();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Update fail", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            Log.d(">>>> Distributor", "onFailure: " + t.getMessage());
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
                Glide.with(EditProfileActivity.this)
                        .load(file)
                        .thumbnail(Glide.with(EditProfileActivity.this).load(R.drawable.loading))
                        .centerCrop()
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.ivProfilePicture);
                binding.tvChooseProfilePicture.setVisibility(View.GONE);
            }
        }
    });

    private File createFileFromURI(Uri uri, String name) {
        File file = new File(EditProfileActivity.this.getCacheDir(), name+".png");
        try {
            InputStream inputStream = EditProfileActivity.this.getContentResolver().openInputStream(uri);
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
                binding.etFullName.getError() == null &&
                binding.etPhone.getError() == null &&
                binding.etAddress.getError() == null;
    }
}