package com.example.ph35768_and103_assignment.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.databinding.ItemShoesBinding;
import com.example.ph35768_and103_assignment.init.IdCategoryChoosed;
import com.example.ph35768_and103_assignment.model.Shoe;
import com.example.ph35768_and103_assignment.src.DetailShoesActivity;

import java.util.ArrayList;

public class ShoeAdapter extends RecyclerView.Adapter<ShoeAdapter.ShoeViewHolder> {

    private ArrayList<Shoe> listShoe;
    private Context context;
    private final IdCategoryChoosed idCategoryChoosed;

    public ShoeAdapter(ArrayList<Shoe> listShoe, Context context, IdCategoryChoosed idCategoryChoosed) {
        this.listShoe = listShoe;
        this.context = context;
        this.idCategoryChoosed = idCategoryChoosed;
    }

    @NonNull
    @Override
    public ShoeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShoesBinding binding = ItemShoesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ShoeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeViewHolder holder, int position) {
        Shoe shoe = listShoe.get(position);
        if (shoe == null) {
            return;
        }
        holder.binding.tvName.setText(shoe.getName());
        holder.binding.tvPrice.setText(shoe.getPrice());
        if (!shoe.getAvatar().isEmpty()){
            String url = shoe.getAvatar();
            String newUrl = url.replace("localhost", "10.0.2.2");
            Glide.with(context)
                    .load(newUrl)
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .skipMemoryCache(true)
                    .into(holder.binding.ivShoes);
        }
        holder.binding.btnSubmitBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (listShoe == null) {
            return 0;
        }
        return listShoe.size();
    }

    public class ShoeViewHolder extends RecyclerView.ViewHolder {
        private ItemShoesBinding binding;

        public ShoeViewHolder(ItemShoesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set an OnClickListener for the item view
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the clicked item
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Get the Shoe object at the position
                        Shoe clickedShoe = listShoe.get(position);

                        // Create an Intent to start the detail activity
                        Intent intent = new Intent(context, DetailShoesActivity.class);

                        // Create a Bundle and put the Shoe object into it
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("clickedShoe", clickedShoe);

                        // Put the Bundle into the Intent
                        intent.putExtras(bundle);

                        // Start the detail activity
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}

