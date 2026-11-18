package com.example.recipe_app.model;

import com.example.recipe_app.model.enums.Allergen;
import com.example.recipe_app.model.enums.DietType;
import com.example.recipe_app.model.enums.DishType;
import com.example.recipe_app.model.enums.IngredientType;
import com.example.recipe_app.model.enums.MealType;

import java.sql.Time;
import java.util.ArrayList;

public class Recipe {
    public int Id;
    public String Name, Author;
    public ArrayList<RecipeStep> Steps;
    public MealType MealType;
    public DishType DishType;
    public DietType DietType;
    public ArrayList<Allergen> Allergens;
    public int Portion;
    public int PreparationTime; // minutes of preparation
    public String ImagePath;
    public float Rating;
    public ArrayList<String> AdditionalImagePaths;
    public String VideoPath;
    public ArrayList<Ingredient> Ingredients;
    public String Origin;
    public String Story;

}
