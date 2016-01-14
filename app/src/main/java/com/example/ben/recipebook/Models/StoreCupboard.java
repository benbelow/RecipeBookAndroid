package com.example.ben.recipebook.models;

import java.util.List;

public class StoreCupboard {

    public final List<Ingredient> ingredients;
    public final List<Equipment> equipments;

    public StoreCupboard(List<Ingredient> ingredients, List<Equipment> equipments) {
        this.ingredients = ingredients;
        this.equipments = equipments;
    }
}
