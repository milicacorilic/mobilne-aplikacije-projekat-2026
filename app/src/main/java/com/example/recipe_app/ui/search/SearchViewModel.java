package com.example.recipe_app.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipe_app.data.RecipeRepository;
import com.example.recipe_app.model.Recipe;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private final RecipeRepository repository;

    private final MutableLiveData<String> query = new MutableLiveData<>("");
    private final MutableLiveData<RecipeFilter> filter = new MutableLiveData<>(new RecipeFilter());

    private final MediatorLiveData<List<Recipe>> filteredRecipes = new MediatorLiveData<>();

    public SearchViewModel() {
        repository = RecipeRepository.getInstance();

        filteredRecipes.addSource(query, q -> updateResults());
        filteredRecipes.addSource(filter, f -> updateResults());
    }

    private void updateResults() {
        String currentQuery = query.getValue();
        RecipeFilter currentFilter = filter.getValue();
        List<Recipe> allRecipes = repository.getAllRecipes();

        filteredRecipes.setValue(RecipeFilterEngine.apply(allRecipes, currentQuery, currentFilter));
    }

    public LiveData<List<Recipe>> getFilteredRecipes() {
        return filteredRecipes;
    }

    public LiveData<String> getQuery() {
        return query;
    }

    public void setQuery(String newQuery) {
        if (newQuery == null) newQuery = "";
        if (!newQuery.equals(query.getValue())) {
            query.setValue(newQuery);
        }
    }

    public LiveData<RecipeFilter> getFilter() {
        return filter;
    }

    public void setFilter(RecipeFilter newFilter) {
        filter.setValue(newFilter.copy());
    }

    public void clearAll() {
        query.setValue("");
        filter.setValue(new RecipeFilter());
    }
}