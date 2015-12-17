package com.example.ben.recipebook.Services;

import com.example.ben.recipebook.Models.Ingredient;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.GsonConverterFactory;


public class DataFetchingService {

    //ToDo: Make a config file
    private String baseUrl = "http://f4a8fbf8.ngrok.io/";

    public DataFetchingService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RecipeService.class);
    }

    public RecipeService service;
}


