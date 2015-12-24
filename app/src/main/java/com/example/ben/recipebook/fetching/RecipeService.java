package com.example.ben.recipebook.fetching;

import com.example.ben.recipebook.models.Equipment;
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


    //ToDo: See if there's a nicer way to avoid passing in nulls, c.f. default values or a better Map
    @GET("/api/recipes")
    Call<List<Recipe>> listRecipes(
            @QueryMap Map<String, String> options,
            @Query("ingredientsAny") List<String> ingredientsAny,
            @Query("ingredientsAll") List<String> ingredientsAll,
            @Query("equipment") List<String> equipment);

    @GET("/api/equipment")
    Call<List<Equipment>> listEquipment();
}
