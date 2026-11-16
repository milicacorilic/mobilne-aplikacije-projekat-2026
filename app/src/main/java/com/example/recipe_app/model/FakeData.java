package com.example.recipe_app.model;

import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.model.RecipeStep;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeData {

    public static List<Recipe> getRecipes() {
        List<Recipe> list = new ArrayList<>();

        list.add(makeRecipe("1", "Chicken strips", "Pera Peric", 2, 45,
                "chicken_strips", 4.5f,
                "Golden, crunchy chicken with a tender middle. An easy homemade favorite for any day."));
        list.add(makeRecipe("2", "French fries", "Pera Peric", 2, 20,
                "french_fries", 4.0f,
                "Crispy on the outside, fluffy inside."));
        list.add(makeRecipe("3", "Ramen", "Pera Peric", 2, 15,
                "ramen", 4.8f,
                "A warm bowl of comfort in fifteen minutes."));

        // same steps for all recipes
        for (Recipe r : list) {
            r.Steps.addAll(getSteps());
        }
        return list;
    }

    // for later
    public static Recipe getRecipeById(String id) {
        for (Recipe r : getRecipes()) {
            if (r.Id.equals(id)) return r;
        }
        return null;
    }

    private static Recipe makeRecipe(String id, String name, String author, int portion, int time, String image, float rating, String story) {
        Recipe r = new Recipe();
        r.Id = id;
        r.Name = name;
        r.Author = author;
        r.Portion = portion;
        r.PreparationTime = time;
        r.ImagePath = image;
        r.Rating = rating;
        r.Story = story;

        r.Allergens = new ArrayList<>();
        r.AdditionalImagePaths = new ArrayList<>();
        r.Ingredients = new ArrayList<>();        // enumi: popuni kad znaš konstante
        r.Steps = new ArrayList<>();
        return r;
    }

    // ---------- KORACI ----------

    public static List<RecipeStep> getSteps() {
        List<RecipeStep> steps = new ArrayList<>();
        steps.add(new RecipeStep("Prep the chicken",
                "Heat the oven to 200°C (180°C fan). Line a tray. Pat the chicken dry and cut into even strips."));
        steps.add(new RecipeStep("Coat for crunch",
                "Put flour, beaten eggs, and panko in separate bowls. Mix paprika, salt, and pepper into the panko. Dip each strip in flour, egg, then panko."));
        steps.add(new RecipeStep("Bake until golden",
                "Place on the tray and drizzle with oil. Bake for 25–30 min, turning halfway. The thickest part must reach 74°C."));
        steps.add(new RecipeStep("Serve and enjoy",
                "Serve hot with your favorite dip and a fresh salad. Add fries for a comforting homemade meal."));
        return steps;
    }

    // temporary
    public static List<String> getIngredients() {
        return Arrays.asList(
                "400 g chicken breast, cut into strips",
                "60 g plain flour",
                "2 eggs, beaten",
                "100 g panko breadcrumbs",
                "1 tsp smoked paprika",
                "½ tsp salt + a pinch of black pepper",
                "2 tbsp olive oil");
    }
}