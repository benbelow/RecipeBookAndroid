package com.example.ben.recipebook.models.recipe;

import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable{

    public final String Name;
    public final String Description;
    public final String MealType;
    public final int CookTime;
    public final int PreparationTime;
    public final int NumberOfServings;
    public final String Author;
    public final String ImageSource;
    public final List<Ingredient> Ingredients;
    public final List<Equipment> Equipment;
    public final List<Instruction> Instructions;

    public Recipe(String description, String mealType, int preparationTime, int cookTime, int numberOfServings, String author, List<Ingredient> ingredients, String name, String imageSource, List<Equipment> equipment, List<Instruction> instructions) {
        Description = description;
        MealType = mealType;
        CookTime = cookTime;
        PreparationTime = preparationTime;
        NumberOfServings = numberOfServings;
        Author = author;
        Ingredients = ingredients;
        Name = name;
        ImageSource = imageSource;
        Equipment = equipment;
        Instructions = instructions;
    }
}
