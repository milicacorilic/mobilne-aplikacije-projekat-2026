package com.example.recipe_app.ui.search;

import com.example.recipe_app.model.enums.Allergen;
import com.example.recipe_app.model.enums.DietType;
import com.example.recipe_app.model.enums.DishType;
import com.example.recipe_app.model.enums.MealType;
import com.example.recipe_app.model.enums.PrepMethod;

import java.util.EnumSet;
import java.util.Set;

public class RecipeFilter {
    public static final int DEFAULT_MIN_TIME = 10;
    public static final int DEFAULT_MAX_TIME = 120;

    // EnumSet guarantees stable iteration order (useful for chips in Step 9)
    public Set<DishType> dishTypes = EnumSet.noneOf(DishType.class);
    public Set<DietType> dietTypes = EnumSet.noneOf(DietType.class);
    public Set<MealType> mealTypes = EnumSet.noneOf(MealType.class);
    public Set<PrepMethod> prepMethods = EnumSet.noneOf(PrepMethod.class);
    public Set<Allergen> excludedAllergens = EnumSet.noneOf(Allergen.class);

    public int minMinutes = DEFAULT_MIN_TIME;
    public int maxMinutes = DEFAULT_MAX_TIME;

    public boolean isTimeActive() {
        return minMinutes > DEFAULT_MIN_TIME || maxMinutes < DEFAULT_MAX_TIME;
    }

    public boolean matchesTime(int minutes) {
        if (minMinutes > DEFAULT_MIN_TIME && minutes < minMinutes) return false;
        if (maxMinutes < DEFAULT_MAX_TIME && minutes > maxMinutes) return false;
        return true;
    }

    public int activeGroupCount() {
        int count = 0;
        if (!dishTypes.isEmpty()) count++;
        if (!dietTypes.isEmpty()) count++;
        if (!mealTypes.isEmpty()) count++;
        if (!prepMethods.isEmpty()) count++;
        if (!excludedAllergens.isEmpty()) count++;
        if (isTimeActive()) count++;
        return count;
    }

    public RecipeFilter copy() {
        RecipeFilter newFilter = new RecipeFilter();
        newFilter.dishTypes.addAll(this.dishTypes);
        newFilter.dietTypes.addAll(this.dietTypes);
        newFilter.mealTypes.addAll(this.mealTypes);
        newFilter.prepMethods.addAll(this.prepMethods);
        newFilter.excludedAllergens.addAll(this.excludedAllergens);
        newFilter.minMinutes = this.minMinutes;
        newFilter.maxMinutes = this.maxMinutes;
        return newFilter;
    }

    public void clear() {
        dishTypes.clear();
        dietTypes.clear();
        mealTypes.clear();
        prepMethods.clear();
        excludedAllergens.clear();
        minMinutes = DEFAULT_MIN_TIME;
        maxMinutes = DEFAULT_MAX_TIME;
    }
}