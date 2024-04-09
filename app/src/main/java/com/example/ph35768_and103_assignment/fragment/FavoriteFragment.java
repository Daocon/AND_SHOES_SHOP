package com.example.ph35768_and103_assignment.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.adapter.CategoryAdapter;
import com.example.ph35768_and103_assignment.adapter.ShoeAdapter;
import com.example.ph35768_and103_assignment.databinding.FragmentFavoriteBinding;
import com.example.ph35768_and103_assignment.init.IdCategoryChoosed;
import com.example.ph35768_and103_assignment.model.Response;
import com.example.ph35768_and103_assignment.model.Shoe;
import com.example.ph35768_and103_assignment.services.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class FavoriteFragment extends Fragment implements IdCategoryChoosed {
    private FragmentFavoriteBinding binding;
    private ArrayList<Shoe> dsShoe;
    private ShoeAdapter shoeAdapter;
    private HttpRequest httpRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpRequest = new HttpRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataShoeFavorite();

    }

    private void getDataShoeFavorite() {
        httpRequest.callApi().getFavoriteShoe().enqueue(responseFavShoe);
    }

    Callback<Response<ArrayList<Shoe>>> responseFavShoe = new Callback<Response<ArrayList<Shoe>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Shoe>>> call, retrofit2.Response<Response<ArrayList<Shoe>>> response) {
            if (response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    dsShoe = response.body().getData();
                    getdata(dsShoe);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Shoe>>> call, Throwable t) {

        }
    };

    private void getdata(ArrayList<Shoe> dsShoe) {
        shoeAdapter = new ShoeAdapter(dsShoe, getActivity(), this);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.rcvShoesFavorite.setLayoutManager(linearLayoutManager);
        binding.rcvShoesFavorite.setAdapter(shoeAdapter);
    }

    @Override
    public void getIdCategory(String idCategory) {

    }
}