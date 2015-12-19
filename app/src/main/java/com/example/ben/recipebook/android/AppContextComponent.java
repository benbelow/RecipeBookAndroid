package com.example.ben.recipebook.android;

import android.content.Context;

import com.example.ben.recipebook.android.recipe.RecipeActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = AppContextModule.class)
@Singleton
public interface AppContextComponent{

    Context context();

    void inject(MainActivity mainActivity);
    void inject(RecipeActivity recipeActivity);
}
