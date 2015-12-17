package com.example.ben.recipebook.Services;

import com.example.ben.recipebook.Models.Ingredient;

import java.util.List;

import javax.security.auth.callback.Callback;

import retrofit.Call;
import retrofit.http.GET;

public interface RecipeService {

    @GET("/api/ingredients")
    Call<List<Ingredient>> listIngredients();

}
