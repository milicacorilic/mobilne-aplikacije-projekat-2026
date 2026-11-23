package com.example.recipe_app.model;

import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.model.RecipeStep;
import com.example.recipe_app.model.enums.Allergen;
import com.example.recipe_app.model.enums.DietType;
import com.example.recipe_app.model.enums.DishType;
import com.example.recipe_app.model.enums.MealType;
import com.example.recipe_app.model.enums.PrepMethod;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeData {

    public static List<Recipe> getRecipes() {
        List<Recipe> list = new ArrayList<>();

        list.add(makeRecipe(1, "Chicken strips", "Pera Peric", 2, 45, "chicken_strips", 4.5f,
                "Golden, crunchy chicken with a tender middle.",
                DishType.MEAT_DISH, MealType.MAIN, DietType.REGULAR, PrepMethod.BAKING,
                new Allergen[]{Allergen.GLUTEN}));
        list.add(makeRecipe(2, "French fries", "Pera Peric", 2, 20, "french_fries", 4.0f,
                "Crispy on the outside, fluffy inside.",
                DishType.VEGETABLE_DISH, MealType.APPETIZER, DietType.VEGAN, PrepMethod.FRYING,
                new Allergen[]{}));
        list.add(makeRecipe(3, "Ramen", "Pera Peric", 2, 15, "ramen", 4.8f,
                "A warm bowl of comfort in fifteen minutes.",
                DishType.SOUP, MealType.MAIN, DietType.REGULAR, PrepMethod.BOILING,
                new Allergen[]{Allergen.GLUTEN}));
        list.add(makeRecipe(4, "Greek salad", "Pera Peric", 2, 10, "greek_salad", 4.3f,
                "Fresh and simple.",
                DishType.VEGETABLE_DISH, MealType.SALAD, DietType.VEGETARIAN, PrepMethod.NO_COOK,
                new Allergen[]{Allergen.LACTOSE}));
        list.add(makeRecipe(5, "Tomato soup", "Pera Peric", 4, 30, "tomato_soup", 4.2f,
                "Slow-simmered comfort.",
                DishType.SOUP, MealType.APPETIZER, DietType.VEGETARIAN, PrepMethod.BOILING,
                new Allergen[]{Allergen.LACTOSE}));
        list.add(makeRecipe(6, "Grilled salmon", "Pera Peric", 2, 25, "grilled_salmon", 4.7f,
                "Light and smoky.",
                DishType.FISH_DISH, MealType.MAIN, DietType.PESCATARIAN, PrepMethod.GRILLING,
                new Allergen[]{}));
        list.add(makeRecipe(7, "Pancakes", "Pera Peric", 4, 30, "pancakes", 4.1f,
                "Weekend breakfast staple.",
                DishType.PANCAKES, MealType.BREAKFAST, DietType.VEGETARIAN, PrepMethod.FRYING,
                new Allergen[]{Allergen.GLUTEN, Allergen.LACTOSE}));
        list.add(makeRecipe(8, "Pasta carbonara", "Pera Peric", 2, 30, "pasta_carbonara", 4.6f,
                "Roman classic.",
                DishType.PASTA, MealType.MAIN, DietType.REGULAR, PrepMethod.BOILING,
                new Allergen[]{Allergen.GLUTEN, Allergen.LACTOSE}));
        list.add(makeRecipe(9, "Vegan buddha bowl", "Pera Peric", 2, 15, "buddha_bowl", 4.4f,
                "Colourful and filling.",
                DishType.VEGETABLE_DISH, MealType.LUNCH, DietType.VEGAN, PrepMethod.NO_COOK,
                new Allergen[]{Allergen.NUTS}));
        list.add(makeRecipe(10, "Chocolate cake", "Pera Peric", 8, 60, "chocolate_cake", 4.9f,
                "Rich and moist.",
                DishType.CAKE, MealType.DESSERT, DietType.VEGETARIAN, PrepMethod.BAKING,
                new Allergen[]{Allergen.GLUTEN, Allergen.LACTOSE}));

        for (Recipe r : list) r.Steps.addAll(getSteps());
        return list;
    }

    public static Recipe getRecipeById(int id) {
        for (Recipe r : getRecipes()) {
            if (r.Id == id) return r;
        }
        return null;
    }

    private static Recipe makeRecipe(int id, String name, String author, int portion, int time,
                                     String image, float rating, String story,
                                     DishType dish, MealType meal, DietType diet,
                                     PrepMethod method, Allergen[] allergens) {
        Recipe r = new Recipe();
        r.Id = id;
        r.Name = name;
        r.Author = author;
        r.Portion = portion;
        r.PreparationTime = time;
        r.ImagePath = image;
        r.Rating = rating;
        r.Story = story;
        r.DishType = dish;
        r.MealType = meal;
        r.DietType = diet;
        r.PrepMethod = method;
        r.Allergens = new ArrayList<>(Arrays.asList(allergens));
        r.AdditionalImagePaths = new ArrayList<>();
        r.Ingredients = new ArrayList<>();
        r.Steps = new ArrayList<>();
        return r;
    }

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