package com.example.ph35768_and103_assignment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.init.Cart_Handle;
import com.example.ph35768_and103_assignment.model.Cart;
import com.example.ph35768_and103_assignment.model.Response;
import com.example.ph35768_and103_assignment.model.Shoe;
import com.example.ph35768_and103_assignment.services.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    private ArrayList<Cart> dsCart;
    private Context context;
    private Cart cart;

    private HttpRequest httpRequest;
    private Cart_Handle handle;


    public CartAdapter(ArrayList<Cart> dsCart, Context context, Cart_Handle handle) {
        this.dsCart = dsCart;
        this.context = context;
        this.handle = handle;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_cart, null);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        cart = dsCart.get(position);
        if (cart == null) {
            return;
        }
        String id_shoes = cart.getId_shoes();
        httpRequest = new HttpRequest();
        httpRequest.callApi().getShoeById(id_shoes).enqueue(new Callback<Response<Shoe>>() {
            @Override
            public void onResponse(Call<Response<Shoe>> call, retrofit2.Response<Response<Shoe>> response) {
                if (response.isSuccessful()) {
                    Shoe shoe = response.body().getData();
                    // Update your UI with the shoe data here
                    holder.tvName.setText(shoe.getName());
                    holder.tvPrice.setText(shoe.getPrice());
                    if (!shoe.getAvatar().isEmpty()){
                        String url = shoe.getAvatar();
                        String newUrl = url.replace("localhost", "10.0.2.2");
                        Glide.with(context)
                                .load(newUrl)
                                .thumbnail(Glide.with(context).load(R.drawable.loading))
                                .centerCrop()
                                .circleCrop()
                                .skipMemoryCache(true)
                                .into(holder.ivCart);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<Shoe>> call, Throwable t) {
                // Handle failure here
            }
        });

        holder.tvQuantity.setText(cart.getQuantity());
        holder.color.setText(cart.getColor());
        holder.size.setText(cart.getSize());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handle.Delete(cart.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsCart.size();
    }

    public static class CartViewHolder  extends RecyclerView.ViewHolder{

        ImageView ivCart;
        TextView tvName, tvPrice, tvQuantity, color, size;
        Button btnDelete, btnPlus, btnMinus;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCart = itemView.findViewById(R.id.iv_cart);
            tvName = itemView.findViewById(R.id.tv_cart_name);
            tvPrice = itemView.findViewById(R.id.tv_cart_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            color = itemView.findViewById(R.id.tv_cart_color);
            size = itemView.findViewById(R.id.tv_cart_size);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnMinus = itemView.findViewById(R.id.btn_minus);
        }
    }
}
