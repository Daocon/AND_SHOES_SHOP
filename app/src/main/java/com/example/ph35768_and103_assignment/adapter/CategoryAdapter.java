package com.example.ph35768_and103_assignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.databinding.ItemRcvCategoryBinding;
import com.example.ph35768_and103_assignment.init.IdCategoryChoosed;
import com.example.ph35768_and103_assignment.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final ArrayList<Category> dsCategory;
    private Context context;

    private final IdCategoryChoosed idCategoryChoosed;


    public CategoryAdapter(ArrayList<Category> dsCategory, Context context, IdCategoryChoosed idCategoryChoosed) {
        this.dsCategory = dsCategory;
        this.context = context;
        this.idCategoryChoosed = idCategoryChoosed;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRcvCategoryBinding binding = ItemRcvCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding, dsCategory, idCategoryChoosed);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = dsCategory.get(position);
        if (category == null) {
            return;
        }
        holder.binding.shoeCategoryName.setText(category.getName());
        if (!category.getAvatar().isEmpty()) { // Kiểm tra danh sách hình ảnh không rỗng
            String url = category.getAvatar();
            String newUrl = url.replace("localhost", "10.0.2.2");
            Glide.with(context)
                    .load(newUrl)
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .centerCrop()
                    .circleCrop()
                    .skipMemoryCache(true)
                    .into(holder.binding.shoeCategoryImage);
        }
    }

    @Override
    public int getItemCount() {
        if (dsCategory != null)
            return dsCategory.size();
        else
            return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemRcvCategoryBinding binding;
        private final ArrayList<Category> dsCategory;

        public CategoryViewHolder(ItemRcvCategoryBinding binding, ArrayList<Category> dsCategory, IdCategoryChoosed idCategoryChoosed) {
            super(binding.getRoot());
            this.binding = binding;
            this.dsCategory = dsCategory;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String categoryId = dsCategory.get(getLayoutPosition()).getId();
                    idCategoryChoosed.getIdCategory(categoryId);
                }
            });
        }
    }
}
