package com.example.ph35768_and103_assignment.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.adapter.CategoryAdapter;
import com.example.ph35768_and103_assignment.adapter.ShoeAdapter;
import com.example.ph35768_and103_assignment.databinding.FragmentHomeBinding;
import com.example.ph35768_and103_assignment.init.IdCategoryChoosed;
import com.example.ph35768_and103_assignment.model.Category;
import com.example.ph35768_and103_assignment.model.Response;
import com.example.ph35768_and103_assignment.model.Shoe;
import com.example.ph35768_and103_assignment.model.User;
import com.example.ph35768_and103_assignment.services.HttpRequest;
import com.google.gson.Gson;
import com.saadahmedev.popupdialog.PopupDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment implements IdCategoryChoosed {
    private FragmentHomeBinding binding;
    private SharedPreferences sharedPreferences;
    private User user;
    private HttpRequest httpRequest;
    private CategoryAdapter categoryAdapter;
    private ShoeAdapter shoeAdapter;
    private ArrayList<Category> dsCategory;
    private ArrayList<Shoe> dsShoe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new User();
        httpRequest = new HttpRequest();
        dsCategory = new ArrayList<>();
        dsShoe = new ArrayList<>();
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

        PopupDialog.getInstance(getActivity())
                .progressDialogBuilder()
                .createProgressDialog()
                .setTint(R.color.colorPrimary)
                .build()
                .show();
        callSharePre();
        getDataCategory();
    }

    private void getDataCategory() {
        httpRequest.callApi().getListCategory().enqueue(getListCategoryResponse);
    }

    private void callSharePre() {
        sharedPreferences = getActivity().getSharedPreferences("Account", MODE_PRIVATE);
        String jsonUserData = sharedPreferences.getString("userData", "");
        Gson gson = new Gson();
        User userData = gson.fromJson(jsonUserData, User.class);
    }

    Callback<Response<ArrayList<Category>>> getListCategoryResponse = new Callback<Response<ArrayList<Category>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Category>>> call, retrofit2.Response<Response<ArrayList<Category>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    dsCategory = response.body().getData();
                    getdata(dsCategory);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Category>>> call, Throwable t) {

        }
    };

    private void getdata(ArrayList<Category> dsCategory) {
        categoryAdapter = new CategoryAdapter(dsCategory, getActivity(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcvCategory.setLayoutManager(linearLayoutManager);
        binding.rcvCategory.setAdapter(categoryAdapter);

        if (!dsCategory.isEmpty()) {
            getIdCategory(dsCategory.get(0).getId());
        }
    }

    @Override
    public void getIdCategory(String idCategory) {
//        Toast.makeText(getActivity(), "idddddddddd"+ idCategory, Toast.LENGTH_SHORT).show();
        if (idCategory != null) {
            httpRequest.callApi().getShoeByCategory(idCategory).enqueue(getListProductResponse);
        }
    }

    Callback<Response<ArrayList<Shoe>>> getListProductResponse = new Callback<Response<ArrayList<Shoe>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Shoe>>> call, retrofit2.Response<Response<ArrayList<Shoe>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    ArrayList<Shoe> ds = response.body().getData();
                    getdataShoe(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Shoe>>> call, Throwable t) {

        }
    };

    private void getdataShoe(ArrayList<Shoe> ds) {
        shoeAdapter = new ShoeAdapter(ds, getActivity(), this);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.rcvShoes.setLayoutManager(linearLayoutManager);
        binding.rcvShoes.setAdapter(shoeAdapter);
    }
}