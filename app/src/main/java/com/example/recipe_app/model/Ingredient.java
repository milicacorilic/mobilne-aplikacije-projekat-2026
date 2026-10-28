package com.example.recipe_app.model;

import com.example.recipe_app.model.enums.IngredientBrand;
import com.example.recipe_app.model.enums.IngredientType;
import com.example.recipe_app.model.enums.UnitOfMeasure;

public class Ingredient {
    public String Name;
    public IngredientType IngredientType;
    public UnitOfMeasure UnitOfMeasure;
    public float Ammount;   // optional
    public IngredientBrand Brand;
}
