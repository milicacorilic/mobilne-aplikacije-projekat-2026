package com.example.recipe_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.util.UiUtils;

import java.util.List;

public class ProfileRecipeAdapter
        extends RecyclerView.Adapter<ProfileRecipeAdapter.ViewHolder> {

    // this interface is used for fragment to find what user has clicked on
    public interface OnRecipeActionListener {
        void onEditClick(Recipe recipe);
        void onRecipeClick(Recipe recipe);
    }

    private final List<Recipe> recipes;
    private final OnRecipeActionListener listener;

    public ProfileRecipeAdapter(List<Recipe> recipes, OnRecipeActionListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    // 1. creates one card from item_profile_recipe.xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_recipe, parent, false);
        return new ViewHolder(view);
    }

    // 2. writes recipe data into a card
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        UiUtils.loadImage(holder.ivRecipe, recipe.ImagePath);
        holder.tvTitle.setText(recipe.Name);
        holder.tvTime.setText(recipe.PreparationTime + " min");
        holder.tvStatus.setText("Published");

        holder.tvEdit.setOnClickListener(v -> listener.onEditClick(recipe));
        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivRecipe;
        final TextView tvTitle, tvTime, tvStatus, tvEdit;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRecipe = itemView.findViewById(R.id.ivRecipe);
            tvTitle = itemView.findViewById(R.id.tvRecipeTitle);
            tvTime = itemView.findViewById(R.id.tvRecipeTime);
            tvStatus = itemView.findViewById(R.id.tvRecipeStatus);
            tvEdit = itemView.findViewById(R.id.tvRecipeEdit);
        }
    }
}