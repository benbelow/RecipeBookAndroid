package com.example.ben.recipebook.models.recipe;

import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;

import java.util.List;

public class NewRecipeBody {

    public List<Instruction> instructionList;
    public List<Ingredient> ingredientList;
    public List<Equipment> equipmentList;

}
