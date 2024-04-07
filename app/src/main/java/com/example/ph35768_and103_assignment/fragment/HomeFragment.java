package com.example.ph35768_and103_assignment.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.databinding.FragmentHomeBinding;
import com.example.ph35768_and103_assignment.model.User;
import com.google.gson.Gson;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private SharedPreferences sharedPreferences;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new User();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("Account", MODE_PRIVATE);
        String jsonUserData = sharedPreferences.getString("userData", "");
        Gson gson = new Gson();
        User userData = gson.fromJson(jsonUserData, User.class);

        binding.textViewHome.setText("Welcome " + userData.getImage());
    }
}