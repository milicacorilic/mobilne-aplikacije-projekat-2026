package com.example.recipe_app.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.R;
import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.util.DisplayNames;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchRecipeAdapter extends RecyclerView.Adapter<SearchRecipeAdapter.ViewHolder> {

    private List<Recipe> recipes = new ArrayList<>();

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_recipe_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.textName.setText(recipe.Name);
        holder.textTime.setText(holder.itemView.getContext().getString(R.string.prep_time_minutes, recipe.PreparationTime));

        // Uppercase only in the adapter for the eyebrow design
        String dishLabel = DisplayNames.dishType(recipe.DishType);
        holder.textDishType.setText(dishLabel.toUpperCase(Locale.getDefault()));

        holder.imageRecipe.setContentDescription(holder.itemView.getContext().getString(R.string.recipe_image_desc, recipe.Name));

        int resId = 0;
        if (recipe.ImagePath != null) {
            resId = holder.itemView.getContext().getResources()
                    .getIdentifier(recipe.ImagePath, "drawable", holder.itemView.getContext().getPackageName());
        }

        if (resId != 0) {
            holder.imageRecipe.setImageResource(resId);
        } else {
            holder.imageRecipe.setImageResource(R.drawable.ic_placeholder_image);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageRecipe;
        ImageView imageHeart;
        TextView textDishType, textName, textTime, textViewRecipe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRecipe = itemView.findViewById(R.id.imageRecipe);
            imageHeart = itemView.findViewById(R.id.imageHeart);
            textDishType = itemView.findViewById(R.id.textDishType);
            textName = itemView.findViewById(R.id.textName);
            textTime = itemView.findViewById(R.id.textTime);
            textViewRecipe = itemView.findViewById(R.id.textViewRecipe);
        }
    }
}