package com.example.ben.recipebook.models.recipe;

import java.io.Serializable;

public class RecipeTimings implements Serializable {

    public final int prepTime;
    public final int cookTime;

    public RecipeTimings(int prepTime, int cookTime) {
        this.prepTime = prepTime;
        this.cookTime = cookTime;
    }
}
