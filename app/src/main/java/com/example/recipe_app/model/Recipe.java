package com.example.recipe_app.model;

import com.example.recipe_app.model.enums.Allergen;
import com.example.recipe_app.model.enums.DietType;
import com.example.recipe_app.model.enums.DishType;
import com.example.recipe_app.model.enums.IngredientType;
import com.example.recipe_app.model.enums.MealType;

import java.sql.Time;
import java.util.ArrayList;

public class Recipe {
    public String Name, Author;
    public MealType MealType;
    public DishType DishType;
    public DietType DietType;
    public ArrayList<Allergen> Allergens;
    public int Portion;
    public Time PreparationTime;
    public String ImagePath;
    public float Rating;
    public ArrayList<String> AdditionalImagePaths;
    public String VideoPath;
    public ArrayList<IngredientType> Ingredients;
    public String Origin;
    public String Story;

}
