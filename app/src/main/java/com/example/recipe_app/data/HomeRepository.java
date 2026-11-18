package com.example.recipe_app.data;

import com.example.recipe_app.ui.home.RecipeCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeRepository {

    private static final List<RecipeCard> ALL = Arrays.asList(
            new RecipeCard(1, "Palačinke", "marko", "Palačinke", 30, 4.5f),
            new RecipeCard(2, "Pileća čorba", "ana", "Čorba", 60, 5.0f),
            new RecipeCard(3, "Pica margarita", "jovan", "Pica", 90, 3.8f),
            new RecipeCard(4, "Tiramisu", "ana", "Torta", 45, 4.9f),
            new RecipeCard(5, "Rižoto sa pečurkama", "petar", "Rižoto", 40, 0f),
            new RecipeCard(6, "Pasta karbonara", "marko", "Testenina", 25, 4.2f)
    );
    public List<RecipeCard> getPopular() {
        List<RecipeCard> list = new ArrayList<>(ALL);
        Collections.sort(list, (a, b) -> Float.compare(b.rating, a.rating));
        return list;
    }

    public List<RecipeCard> getNewest() {
        List<RecipeCard> list = new ArrayList<>(ALL);
        Collections.reverse(list);
        return list;
    }

    public List<RecipeCard> getRecentlyViewed() {
        return Arrays.asList(ALL.get(1), ALL.get(2));
    }

    public List<RecipeCard> getRecentlyCooked() {
        return Collections.singletonList(ALL.get(0));
    }
    public List<RecipeCard> getCanCook() {
        return Arrays.asList(ALL.get(0), ALL.get(1), ALL.get(5));
    }
}