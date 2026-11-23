package com.example.recipe_app.util;

import com.example.recipe_app.model.enums.Allergen;
import com.example.recipe_app.model.enums.DietType;
import com.example.recipe_app.model.enums.DishType;
import com.example.recipe_app.model.enums.MealType;
import com.example.recipe_app.model.enums.PrepMethod;

import java.util.Locale;

public class DisplayNames {

    public static String dishType(DishType type) {
        if (type == null) return "";
        switch (type) {
            case MEAT_DISH: return "Meat dishes";
            case SOUP: return "Soups";
            case FISH_DISH: return "Fish dishes";
            case VEGETABLE_DISH: return "Vegetable dishes";
            case PASTA: return "Pasta";
            case BAKERY_PRODUCT: return "Bakery products";
            case CAKE: return "Cakes";
            default: return formatEnum(type.name());
        }
    }

    public static String dietType(DietType type) {
        if (type == null) return "";
        switch (type) {
            case LENT: return "Lent / Fasting";
            case CHRONO: return "Chrono";
            default: return formatEnum(type.name());
        }
    }

    public static String mealType(MealType type) {
        if (type == null) return "";
        return formatEnum(type.name());
    }

    public static String prepMethod(PrepMethod method) {
        if (method == null) return "";
        return formatEnum(method.name());
    }

    public static String allergen(Allergen allergen) {
        if (allergen == null) return "";
        return formatEnum(allergen.name());
    }

    public static String time(int minutes) {
        if (minutes < 60) return minutes + " min";
        int hours = minutes / 60;
        int mins = minutes % 60;
        if (mins == 0) return hours + " hr";
        return hours + " hr " + mins + " min";
    }

    private static String formatEnum(String name) {
        String formatted = name.replace("_", " ").toLowerCase(Locale.ROOT);
        return formatted.substring(0, 1).toUpperCase(Locale.ROOT) + formatted.substring(1);
    }
}