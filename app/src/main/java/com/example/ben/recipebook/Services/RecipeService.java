package com.example.ben.recipebook.services;

import com.example.ben.recipebook.models.Ingredient;

import java.util.List;
import java.util.Map;

import com.example.ben.recipebook.models.recipe.Recipe;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface RecipeService {

    @GET("/api/ingredients")
    Call<List<Ingredient>> listIngredients();

    @GET("/api/recipes")
    Call<List<Recipe>> listRecipes(
            @QueryMap Map<String, String> options);

}
