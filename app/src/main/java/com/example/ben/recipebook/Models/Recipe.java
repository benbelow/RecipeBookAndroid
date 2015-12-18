package com.example.ben.recipebook.models;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable{

    public final String Name;
    public final String Description;
    public final String MealType;
    public final int PreparationTime;
    public final int CookTime;
    public final int NumberOfServings;
    public final String Author;
    public final List<Ingredient> Ingredients;

    public Recipe(String description, String mealType, int preparationTime, int cookTime, int numberOfServings, String author, List<Ingredient> ingredients, String name) {
        Description = description;
        MealType = mealType;
        PreparationTime = preparationTime;
        CookTime = cookTime;
        NumberOfServings = numberOfServings;
        Author = author;
        Ingredients = ingredients;
        Name = name;
    }
}
