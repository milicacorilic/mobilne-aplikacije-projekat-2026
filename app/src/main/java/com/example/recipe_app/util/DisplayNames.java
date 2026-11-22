package com.example.recipe_app.util;

import com.example.recipe_app.model.enums.DishType;
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
            default:
                // Fallback: "PANCAKES" -> "Pancakes"
                String name = type.name().replace("_", " ").toLowerCase(Locale.getDefault());
                return name.substring(0, 1).toUpperCase(Locale.getDefault()) + name.substring(1);
        }
    }
}