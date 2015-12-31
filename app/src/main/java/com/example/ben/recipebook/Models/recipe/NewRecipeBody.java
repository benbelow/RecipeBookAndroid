package com.example.ben.recipebook.models.recipe;

import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;

import java.util.List;

public class NewRecipeBody {

    public List<Ingredient> Ingredients;
    public List<Equipment> Equipment;
    public List<Instruction> Instructions;

    public NewRecipeBody(List<Ingredient> ingredients, List<Equipment> equipments, List<Instruction> instructions){
        Ingredients = ingredients;
        Equipment = equipments;
        Instructions = instructions;
    }

}
