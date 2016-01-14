package com.example.ben.recipebook.fetching;

import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;
import com.example.ben.recipebook.models.StoreCupboard;
import com.example.ben.recipebook.models.recipe.Recipe;

import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface RecipeService {

    // Authentication

    @POST("api/auth/googlelogin")
    Call<TokenDetails> googleLogin(
            @Query("googleIdToken") String googleIdToken);

    @POST("api/auth/refresh")
    Call<TokenDetails> refreshToken(
            @Body RefreshToken refreshTokenDetails);

    // Recipes

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

    @POST("/api/recipes")
    Call<Recipe> postRecipe(
            @Body Recipe body);

    @PATCH("/api/recipes/{id}")
    Call<Recipe> patchRecipe(
            @Path("id") int recipeId,
            @Body JsonPatchDocument patchData);

    @DELETE("api/recipes/{id}")
    Call<Void> deleteRecipe(@Path("id") int recipeId);

    // Ingredient Components

    @GET("/api/ingredients")
    Call<List<Ingredient>> listIngredients();

    @GET("/api/equipment")
    Call<List<Equipment>> listEquipment();

    @GET("/api/storeCupboard")
    Call<StoreCupboard> storeCupboard();

    @POST("api/storeCupboard/ingredients")
    Call<StoreCupboard> postStoreCupboardIngredients(
            @Body List<String> ingredientNames);

    @POST("api/storeCupboard/equipments")
    Call<StoreCupboard> postStoreCupboardEquipments(
            @Body List<String> equipmentNames);

}
