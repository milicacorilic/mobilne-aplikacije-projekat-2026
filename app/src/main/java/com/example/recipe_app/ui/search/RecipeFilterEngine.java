package com.example.recipe_app.ui.search;

import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.model.enums.DietType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RecipeFilterEngine {

    public static List<Recipe> apply(List<Recipe> allRecipes, String query, RecipeFilter filter) {
        List<Recipe> result = new ArrayList<>();

        if (allRecipes == null) return result;
        if (filter == null) filter = new RecipeFilter();

        String normalizedQuery = query != null ? query.trim().toLowerCase(Locale.ROOT) : "";

        for (Recipe recipe : allRecipes) {
            if (!normalizedQuery.isEmpty()) {
                if (recipe.Name == null || !recipe.Name.toLowerCase(Locale.ROOT).contains(normalizedQuery)) {
                    continue;
                }
            }

            if (!filter.dishTypes.isEmpty() && !filter.dishTypes.contains(recipe.DishType)) {
                continue;
            }

            if (!filter.mealTypes.isEmpty() && !filter.mealTypes.contains(recipe.MealType)) {
                continue;
            }

            if (!filter.prepMethods.isEmpty() && !filter.prepMethods.contains(recipe.PrepMethod)) {
                continue;
            }

            if (!filter.dietTypes.isEmpty() && !matchesDiet(recipe, filter)) {
                continue;
            }

            if (!filter.excludedAllergens.isEmpty() && recipe.Allergens != null) {
                if (!Collections.disjoint(recipe.Allergens, filter.excludedAllergens)) {
                    continue; // Recipe contains at least one excluded allergen
                }
            }

            if (!filter.matchesTime(recipe.PreparationTime)) {
                continue;
            }

            result.add(recipe);
        }
        return result;
    }

    private static boolean matchesDiet(Recipe recipe, RecipeFilter filter) {
        if (recipe.DietType == null) return false;

        for (DietType selected : filter.dietTypes) {
            if (selected == DietType.VEGETARIAN && (recipe.DietType == DietType.VEGETARIAN || recipe.DietType == DietType.VEGAN)) return true;
            if (selected == DietType.PESCATARIAN && (recipe.DietType == DietType.PESCATARIAN || recipe.DietType == DietType.VEGETARIAN || recipe.DietType == DietType.VEGAN)) return true;
            if (selected == DietType.LENT && (recipe.DietType == DietType.LENT || recipe.DietType == DietType.VEGAN)) return true;

            if (selected == recipe.DietType) return true;
        }
        return false;
    }
}