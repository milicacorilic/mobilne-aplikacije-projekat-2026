package com.example.recipe_app.data;

import com.example.recipe_app.model.FakeData;
import com.example.recipe_app.model.Recipe;

import java.util.List;

public class RecipeRepository {
    private static RecipeRepository instance;
    private final List<Recipe> recipes;

    private RecipeRepository() {
        this.recipes = FakeData.getRecipes();
    }

    public static RecipeRepository getInstance() {
        if (instance == null) instance = new RecipeRepository();
        return instance;
    }

    public List<Recipe> getAllRecipes() { return recipes; }

    public Recipe getRecipeById(int id) {
        for (Recipe r : recipes) {
            if (r.Id == id) return r;
        }
        return null;
    }
}