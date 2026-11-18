package com.example.recipe_app.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.R;

import java.util.List;
import java.util.Locale;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.VH> {

    public interface OnRecipeClick {
        void onClick(RecipeCard recipe);
    }

    private final List<RecipeCard> items;
    private final OnRecipeClick listener;

    public RecipeCardAdapter(List<RecipeCard> items, OnRecipeClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        RecipeCard r = items.get(position);
        h.title.setText(r.name);
        h.meta.setText(String.format(Locale.getDefault(), "%d min", r.minutes));
        h.itemView.setOnClickListener(v -> listener.onClick(r));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView title, meta;

        VH(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.tvRecipeTitle);
            meta = v.findViewById(R.id.tvMeta);
        }
    }
}