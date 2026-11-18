package com.example.recipe_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipe_app.model.FakeData;
import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.model.RecipeStep;
import com.example.recipe_app.util.UiUtils;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;
import java.util.Locale;

public class RecipeDetailsFragment extends Fragment {

    private Recipe recipe;

    public RecipeDetailsFragment() {
        super(R.layout.fragment_recipe_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int recipeId = -1;
        if (getArguments() != null) {
            recipeId = getArguments().getInt("recipeId", -1);
        }
        recipe = FakeData.getRecipeById(recipeId);

        if (recipe == null) {
            toast("Recipe not found");
            Navigation.findNavController(view).navigateUp();
            return;
        }


        bindRecipe(view);
        fillIngredients(view);
        fillSteps(view);

        view.findViewById(R.id.ivFavorite).setOnClickListener(v -> toast("TODO: favorites"));
    }



    private void bindRecipe(View v) {
        UiUtils.loadImage((ImageView) v.findViewById(R.id.ivRecipeImage), recipe.ImagePath);

        setText(v, R.id.tvRecipeName, recipe.Name);
        setText(v, R.id.tvAuthor, "@" + recipe.Author.toLowerCase(Locale.ROOT).replace(" ", ""));
        setText(v, R.id.tvStory, recipe.Story);

        setText(v, R.id.tvStatTotal, String.valueOf(recipe.PreparationTime));
        setText(v, R.id.tvStatRating, String.format(Locale.US, "%.1f", recipe.Rating));

        setText(v, R.id.tvServes, "Serves " + recipe.Portion);
    }

    private void fillIngredients(View v) {
        LinearLayout container = v.findViewById(R.id.llIngredients);
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (String ingredient : FakeData.getIngredients()) {
            View row = inflater.inflate(R.layout.item_ingredient, container, false);
            ((TextView) row.findViewById(R.id.tvIngredient)).setText(ingredient);
            container.addView(row);
        }
    }

    private void fillSteps(View v) {
        LinearLayout container = v.findViewById(R.id.llSteps);
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        List<RecipeStep> steps = recipe.Steps;
        for (int i = 0; i < steps.size(); i++) {
            RecipeStep step = steps.get(i);
            View row = inflater.inflate(R.layout.item_recipe_step, container, false);
            ((TextView) row.findViewById(R.id.tvStepNumber)).setText(String.valueOf(i + 1));
            ((TextView) row.findViewById(R.id.tvStepTitle)).setText(step.Name);
            ((TextView) row.findViewById(R.id.tvStepDescription)).setText(step.Content);
            container.addView(row);
        }
    }

    private void setText(View root, int id, String text) {
        ((TextView) root.findViewById(id)).setText(text);
    }

    private void toast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}