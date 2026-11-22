package com.example.recipe_app.ui.search;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.R;
import com.example.recipe_app.data.RecipeRepository;
import com.example.recipe_app.model.Recipe;

import java.util.List;

public class SearchFragment extends Fragment {

    private SearchRecipeAdapter adapter;

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        TextView textRecipeCount = view.findViewById(R.id.textRecipeCount);

        adapter = new SearchRecipeAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Load sample data
        List<Recipe> recipes = RecipeRepository.getInstance().getAllRecipes();
        adapter.setRecipes(recipes);

        // Use plurals for correct grammar (1 recipe vs 2 recipes)
        textRecipeCount.setText(getResources().getQuantityString(R.plurals.recipe_count, recipes.size(), recipes.size()));
    }
}