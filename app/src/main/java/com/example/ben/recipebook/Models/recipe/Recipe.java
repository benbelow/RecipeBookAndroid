package com.example.ben.recipebook.models.recipe;

import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable{

    public final int Id;
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

    public Recipe(int id, String name, String description, String mealType, String author, String imageSource, int numberOfServings, int cookTime, int preparationTime, List<Ingredient> ingredients, List<Equipment> equipment, List<Instruction> instructions) {
        Id = id;
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
