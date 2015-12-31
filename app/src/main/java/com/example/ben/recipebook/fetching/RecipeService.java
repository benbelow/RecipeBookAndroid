package com.example.ben.recipebook.fetching;

import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;

import java.util.List;
import java.util.Map;

import com.example.ben.recipebook.models.recipe.NewRecipeBody;
import com.example.ben.recipebook.models.recipe.Recipe;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
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

    @GET("/api/recipes/with")
    Call<List<Recipe>> listOwnedRecipes(
            @Query("ownedIngredients") List<String> ownedIngredients,
            @Query("requiredIngredients") List<String> requiredIngredients,
            @Query("equipment") List<String> equipment);

    @GET("/api/equipment")
    Call<List<Equipment>> listEquipment();

    @POST("/api/recipes/postRecipe")
    Call<Recipe> postRecipe(
            @Query("name") String name,
            @Query("description") String description,
            @Query("mealType") String mealType,
            @Query("prepTime") int prepTime,
            @Query("cookTime") int cookTime,
            @Query("numberOfServings") int numberOfServings,
            @Query("author") String author,
            @Body NewRecipeBody body);
}
