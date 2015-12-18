package com.example.ben.recipebook.services;

import com.example.ben.recipebook.models.Ingredient;

import java.util.List;

import com.example.ben.recipebook.models.Recipe;
import retrofit.Call;
import retrofit.http.GET;

public interface RecipeService {

    @GET("/api/ingredients")
    Call<List<Ingredient>> listIngredients();

    @GET("/api/recipes")
    Call<List<Recipe>> listRecipes();

}
