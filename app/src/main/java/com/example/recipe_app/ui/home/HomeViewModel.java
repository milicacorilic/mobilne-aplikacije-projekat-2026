package com.example.recipe_app.ui.home;

import androidx.lifecycle.ViewModel;

import com.example.recipe_app.data.HomeRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final HomeRepository repo = new HomeRepository();

    public List<RecipeCard> getPopular()        { return repo.getPopular(); }
    public List<RecipeCard> getNewest()         { return repo.getNewest(); }
    public List<RecipeCard> getRecentlyViewed() { return repo.getRecentlyViewed(); }
    public List<RecipeCard> getRecentlyCooked() { return repo.getRecentlyCooked(); }
    public List<RecipeCard> getCanCook()        { return repo.getCanCook(); }
}