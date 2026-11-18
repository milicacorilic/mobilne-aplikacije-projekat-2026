package com.example.recipe_app.ui.home;

public class RecipeCard {
    public final long id;
    public final String name;
    public final String author;
    public final String dishType;
    public final int minutes;
    public final float rating; // 0 = nema ocena

    public RecipeCard(long id, String name, String author,
                      String dishType, int minutes, float rating) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.dishType = dishType;
        this.minutes = minutes;
        this.rating = rating;
    }
}