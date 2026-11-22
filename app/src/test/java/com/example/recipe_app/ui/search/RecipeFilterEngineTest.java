package com.example.recipe_app.ui.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import com.example.recipe_app.model.Recipe;
import com.example.recipe_app.model.enums.Allergen;
import com.example.recipe_app.model.enums.DietType;
import com.example.recipe_app.model.enums.DishType;
import com.example.recipe_app.model.enums.MealType;
import com.example.recipe_app.model.enums.PrepMethod;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeFilterEngineTest {

    private List<Recipe> recipes;

    @Before
    public void setUp() {
        recipes = new ArrayList<>();

        // 1. Chicken: Meat, Main, Regular, Baking, 45min, Gluten
        recipes.add(createRecipe("Chicken", DishType.MEAT_DISH, MealType.MAIN, DietType.REGULAR, PrepMethod.BAKING, 45, Allergen.GLUTEN));

        // 2. Salad: Veg Dish, Salad, Vegan, No Cook, 10min, none
        recipes.add(createRecipe("Salad", DishType.VEGETABLE_DISH, MealType.SALAD, DietType.VEGAN, PrepMethod.NO_COOK, 10));

        // 3. Salmon: Fish, Main, Pescatarian, Grilling, 25min, none
        recipes.add(createRecipe("Salmon", DishType.FISH_DISH, MealType.MAIN, DietType.PESCATARIAN, PrepMethod.GRILLING, 25));

        // 4. Cake: Cake, Dessert, Vegetarian, Baking, 60min, Gluten, Lactose
        recipes.add(createRecipe("Cake", DishType.CAKE, MealType.DESSERT, DietType.VEGETARIAN, PrepMethod.BAKING, 60, Allergen.GLUTEN, Allergen.LACTOSE));

        // 5. Quick Bite: 5min (for boundary testing)
        recipes.add(createRecipe("Quick Bite", DishType.SANDWICH, MealType.APPETIZER, DietType.VEGETARIAN, PrepMethod.NO_COOK, 5));

        // 6. Slow Roast: 150min (for boundary testing)
        recipes.add(createRecipe("Slow Roast", DishType.MEAT_DISH, MealType.DINNER, DietType.REGULAR, PrepMethod.BAKING, 150));
    }

    private Recipe createRecipe(String name, DishType dish, MealType meal, DietType diet, PrepMethod method, int time, Allergen... allergens) {
        Recipe r = new Recipe();
        r.Name = name;
        r.DishType = dish;
        r.MealType = meal;
        r.DietType = diet;
        r.PrepMethod = method;
        r.PreparationTime = time;
        r.Allergens = new ArrayList<>(Arrays.asList(allergens));
        return r;
    }

    @Test
    public void testNameSearch() {
        List<Recipe> result = RecipeFilterEngine.apply(recipes, "chi", new RecipeFilter());
        assertEquals(1, result.size());
        assertEquals("Chicken", result.get(0).Name);
    }

    @Test
    public void testSingleGroupDishType() {
        RecipeFilter filter = new RecipeFilter();
        filter.dishTypes.add(DishType.MEAT_DISH);
        filter.dishTypes.add(DishType.CAKE);

        List<Recipe> result = RecipeFilterEngine.apply(recipes, "", filter);
        assertEquals(3, result.size()); // Chicken, Cake, Slow Roast
    }

    @Test
    public void testDietTypeMappings() {
        RecipeFilter filter = new RecipeFilter();

        // Vegetarian should include Vegan (Salad), Vegetarian (Cake, Quick Bite)
        filter.dietTypes.add(DietType.VEGETARIAN);
        List<Recipe> result = RecipeFilterEngine.apply(recipes, "", filter);
        assertEquals(3, result.size());

        // Pescatarian should include Pescatarian (Salmon), Vegetarian (Cake, Quick Bite), Vegan (Salad)
        filter.clear();
        filter.dietTypes.add(DietType.PESCATARIAN);
        result = RecipeFilterEngine.apply(recipes, "", filter);
        assertEquals(4, result.size());
    }

    @Test
    public void testAllergenExclusion() {
        RecipeFilter filter = new RecipeFilter();
        filter.excludedAllergens.add(Allergen.GLUTEN);

        List<Recipe> result = RecipeFilterEngine.apply(recipes, "", filter);
        // Salad, Salmon, Quick Bite, Slow Roast (Chicken and Cake have Gluten)
        assertEquals(4, result.size());
    }

    @Test
    public void testTimeRangeBoundaries() {
        RecipeFilter filter = new RecipeFilter();
        filter.minMinutes = 10; // default (no lower limit)
        filter.maxMinutes = 60;

        List<Recipe> result = RecipeFilterEngine.apply(recipes, "", filter);
        // Quick Bite(5), Salad(10), Salmon(25), Chicken(45), Cake(60).
        // (Slow Roast 150 is excluded)
        assertEquals(5, result.size());

        // Let's test a strict range
        filter.minMinutes = 15;
        filter.maxMinutes = 45;
        result = RecipeFilterEngine.apply(recipes, "", filter);
        // Only Salmon(25) and Chicken(45) fit
        assertEquals(2, result.size());
    }

    @Test
    public void testTimeSliderUpperLimitOnly() {
        RecipeFilter filter = new RecipeFilter();
        filter.maxMinutes = 30; // Lower thumb stays default
        // Recipes: 5, 10, 25, 45, 60, 150.
        // Expected: Quick Bite(5), Salad(10), Salmon(25). (Everything below 30 is kept, even < 10).

        List<Recipe> result = RecipeFilterEngine.apply(recipes, "", filter);
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(r -> r.Name.equals("Quick Bite")));
    }

    @Test
    public void testTimeSliderLowerLimitOnly() {
        RecipeFilter filter = new RecipeFilter();
        filter.minMinutes = 40; // Upper thumb stays default
        // Recipes: 5, 10, 25, 45, 60, 150.
        // Expected: Chicken(45), Cake(60), Slow Roast(150). (Everything above 40 is kept, even > 120).

        List<Recipe> result = RecipeFilterEngine.apply(recipes, "", filter);
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(r -> r.Name.equals("Slow Roast")));
    }

    @Test
    public void testNullAndWhitespaceQuery() {
        assertEquals(6, RecipeFilterEngine.apply(recipes, null, new RecipeFilter()).size());
        assertEquals(6, RecipeFilterEngine.apply(recipes, "   ", new RecipeFilter()).size());
    }

    @Test
    public void testCopyIndependence() {
        RecipeFilter original = new RecipeFilter();
        original.dishTypes.add(DishType.SOUP);

        RecipeFilter copy = original.copy();
        copy.dishTypes.add(DishType.CAKE);

        assertEquals(1, original.dishTypes.size()); // Original unchanged
        assertEquals(2, copy.dishTypes.size());
        assertNotSame(original.dishTypes, copy.dishTypes);
    }

    @Test
    public void testEverythingCombined() {
        RecipeFilter filter = new RecipeFilter();
        filter.dishTypes.add(DishType.MEAT_DISH);
        filter.dietTypes.add(DietType.REGULAR);
        filter.excludedAllergens.add(Allergen.GLUTEN);
        filter.minMinutes = 30;
        filter.maxMinutes = 90;

        // Query "ro"
        List<Recipe> result = RecipeFilterEngine.apply(recipes, "ro", filter);

        // Should filter out Slow Roast because it has 150 mins (too long).
        // Should filter out Chicken because it has Gluten.
        assertEquals(0, result.size());
    }
}