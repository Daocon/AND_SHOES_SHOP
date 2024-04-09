package com.example.ph35768_and103_assignment.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.adapter.CartAdapter;
import com.example.ph35768_and103_assignment.adapter.ShoeAdapter;
import com.example.ph35768_and103_assignment.databinding.FragmentCartBinding;
import com.example.ph35768_and103_assignment.init.Cart_Handle;
import com.example.ph35768_and103_assignment.model.Cart;
import com.example.ph35768_and103_assignment.model.Response;
import com.example.ph35768_and103_assignment.model.Shoe;
import com.example.ph35768_and103_assignment.services.HttpRequest;
import com.example.ph35768_and103_assignment.src.CheckOutActivity;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class CartFragment extends Fragment implements Cart_Handle {

    private FragmentCartBinding binding;
    private HttpRequest httpRequest;
    private CartAdapter cartAdapter;
    private Shoe shoe;
    double totalCost = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpRequest = new HttpRequest();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDataCart();
        checkout();
    }

    private void checkout() {
        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Checkout", Toast.LENGTH_SHORT).show();
                httpRequest.callApi().getListCart().enqueue(new Callback<Response<ArrayList<Cart>>>() {
                    @Override
                    public void onResponse(Call<Response<ArrayList<Cart>>> call, retrofit2.Response<Response<ArrayList<Cart>>> response) {
                        if(response.isSuccessful()){
                            if (response.body().getStatus() == 200) {
                                ArrayList<Cart> dsTo = response.body().getData();

                                // Convert dsTo and totalCost to JSON
                                Gson gson = new Gson();
                                String dsToJson = gson.toJson(dsTo);
                                String totalCostJson = gson.toJson(totalCost);

                                // Save dsToJson and totalCostJson to SharedPreferences
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("InfoBill", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("dsTo", dsToJson);
                                editor.putString("totalCost", totalCostJson);
                                editor.apply();

                                // Start CheckOutActivity
                                Intent intent = new Intent(getActivity(), CheckOutActivity.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<ArrayList<Cart>>> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void setDataCart() {
        httpRequest.callApi().getListCart().enqueue(responseCart);
    }

    Callback<Response<ArrayList<Cart>>> responseCart = new Callback<Response<ArrayList<Cart>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Cart>>> call, retrofit2.Response<Response<ArrayList<Cart>>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200) {
                    ArrayList<Cart> ds = response.body().getData();
                    getData(ds);
//                    Toast.makeText(HomeActivity.this, "hihi"+dsFruits, Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Cart>>> call, Throwable t) {

        }
    };

    private void getData(ArrayList<Cart> ds) {
        cartAdapter = new CartAdapter(ds, getActivity(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvCart.setLayoutManager(linearLayoutManager);
        binding.rvCart.setAdapter(cartAdapter);

        for (Cart cart : ds) {
            httpRequest.callApi().getShoeById(cart.getId_shoes()).enqueue(new Callback<Response<Shoe>>() {
                @Override
                public void onResponse(Call<Response<Shoe>> call, retrofit2.Response<Response<Shoe>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            shoe = new Shoe();
                            shoe = response.body().getData();
                            double price = Double.parseDouble(shoe.getPrice()); // Convert to double

                            int quantity = Integer.parseInt(cart.getQuantity()); // Get quantity

                            totalCost += price * quantity; // Calculate total
//                            Toast.makeText(getActivity(), "k:"+ price+quantity, Toast.LENGTH_SHORT).show();
                            binding.tvTotalCost.setText(String.format("$%.2f", totalCost));
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response<Shoe>> call, Throwable t) {

                }
            });
        }
//        Toast.makeText(getActivity(), "total2"+ totalCost, Toast.LENGTH_SHORT).show();
        // Display total cost

    }

    @Override
    public void Delete(String id) {
        httpRequest.callApi().deleteCart(id).enqueue(responseCartDelete);
    }

    Callback<Response<Cart>> responseCartDelete = new Callback<Response<Cart>>() {
        @Override
        public void onResponse(Call<Response<Cart>> call, retrofit2.Response<Response<Cart>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200) {
                    setDataCart();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Cart>> call, Throwable t) {

        }
    };

    @Override
    public void Update(String id, int quantity) {

    }
}