package com.example.ben.recipebook.android;

import android.content.Context;
import android.view.LayoutInflater;

import com.example.ben.recipebook.models.Ingredient;
import com.example.ben.recipebook.android.recipe.RecipeIngredientViewHolder;
import com.example.ben.recipebook.android.recipe.RecipeNameViewHolder;

import javax.inject.Inject;

public class ViewHolderFactory {

    @Inject
    public ViewHolderFactory(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    private Context context;

    private LayoutInflater inflater;

    public <T> T buildViewHolder(Class<T> klass, Object content){
        if(RecipeNameViewHolder.class.isAssignableFrom(klass)){
            return (T) buildTitleViewHolder((String) content); 
        }
        else if(RecipeIngredientViewHolder.class.isAssignableFrom(klass)){
            return (T) buildRecipeIngredientViewHolder((Ingredient) content);
        }
        else {
            throw new IllegalStateException("Unrecognised view holder type requested.");
        }
    }

    private RecipeNameViewHolder buildTitleViewHolder(String title) {
        return new RecipeNameViewHolder(title, inflater);
    }

    private RecipeIngredientViewHolder buildRecipeIngredientViewHolder(Ingredient ingredient) {
        return new RecipeIngredientViewHolder(ingredient, inflater);
    }

}
