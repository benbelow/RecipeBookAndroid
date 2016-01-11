package com.example.ben.recipebook.android.dagger;

import android.content.Context;

import com.example.ben.recipebook.android.IngredientFragment;
import com.example.ben.recipebook.android.MainActivity;
import com.example.ben.recipebook.android.NewRecipeFragment;
import com.example.ben.recipebook.android.OwnedRecipeSearchFragment;
import com.example.ben.recipebook.android.RecipeApplication;
import com.example.ben.recipebook.android.SignInActivity;
import com.example.ben.recipebook.android.recipeList.RecipeListFragment;
import com.example.ben.recipebook.android.RecipeSearchFragment;
import com.example.ben.recipebook.android.RecipeSearchResultsActivity;
import com.example.ben.recipebook.android.recipe.RecipeActivity;
import com.example.ben.recipebook.models.Ingredient;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ApplicationModule.class)
@Singleton
public interface ApplicationComponent {

    Context context();

    void inject(RecipeApplication application);
    void inject(MainActivity mainActivity);
    void inject(RecipeActivity recipeActivity);
    void inject(RecipeSearchResultsActivity recipeSearchResultsActivity);
    void inject(RecipeSearchFragment recipeSearchFragment);
    void inject(OwnedRecipeSearchFragment ownedRecipeSearchFragment);
    void inject(RecipeListFragment recipeListFragment);
    void inject(NewRecipeFragment recipeListFragment);
    void inject(IngredientFragment ingredientFragment);
    void inject(SignInActivity signInActivity);
}
