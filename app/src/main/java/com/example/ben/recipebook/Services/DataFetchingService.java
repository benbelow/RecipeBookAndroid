package com.example.ben.recipebook.services;

import retrofit.Retrofit;
import retrofit.GsonConverterFactory;


public class DataFetchingService {

    //ToDo: Make a config file
    private String baseUrl = "http://52.35.36.23/";

    public DataFetchingService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RecipeService.class);
    }

    public RecipeService service;
}


