package com.example.ben.recipebook.android;

import android.view.LayoutInflater;

import com.example.ben.recipebook.android.recipe.RecipeActivity;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeAuthorViewHolder;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeEquipmentListViewHolder;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeEquipmentViewHolder;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeImageViewHolder;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeIngredientsListViewHolder;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeInstructionViewHolder;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeMealtypeViewHolder;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeServingsViewHolder;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeTimingsViewHolder;
import com.example.ben.recipebook.fetching.ImageService;
import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeIngredientViewHolder;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeNameViewHolder;
import com.example.ben.recipebook.models.recipe.Instruction;
import com.example.ben.recipebook.models.recipe.Recipe;
import com.example.ben.recipebook.models.recipe.RecipeTimings;

import java.util.List;

import javax.inject.Inject;

public class ViewHolderFactory {

    private LayoutInflater inflater;
    private ImageService imageService;

    @Inject
    public ViewHolderFactory(LayoutInflater inflater, ImageService imageService){
        this.inflater = inflater;
        this.imageService = imageService;
    }

    public <T> T buildViewHolder(Class<T> klass, Object content){
        if(RecipeNameViewHolder.class.isAssignableFrom(klass)){
            return (T) buildTitleViewHolder((String) content); 
        }
        else if(RecipeIngredientsListViewHolder.class.isAssignableFrom(klass)){
            return (T) buildRecipeIngredientsListViewHolder((List<Ingredient>) content);
        }
        else if(RecipeAuthorViewHolder.class.isAssignableFrom(klass)){
            return (T) buildRecipeAuthorViewHolder((String) content);
        }
        else if(RecipeEquipmentListViewHolder.class.isAssignableFrom(klass)){
            return (T) buildRecipeEquipmentListViewHolder((List<Equipment>) content);
        }
        else if(RecipeInstructionViewHolder.class.isAssignableFrom(klass)){
            return (T) buildRecipeInstructionViewHolder((Instruction) content);
        }
        else if(RecipeMealtypeViewHolder.class.isAssignableFrom(klass)){
            return (T) buildRecipeMealtypeViewHolder((String) content);
        }
        else if(RecipeServingsViewHolder.class.isAssignableFrom(klass)){
            return (T) buildRecipeServingsViewHolder((int) content);
        }
        else if(RecipeTimingsViewHolder.class.isAssignableFrom(klass)){
            return (T) buildRecipeTimingsViewHolder((RecipeTimings) content);
        }
        else if(RecipeImageViewHolder.class.isAssignableFrom(klass)){
            return (T) buildRecipeImageViewHolder((String) content);
        }
        else {
            throw new IllegalStateException("Unrecognised view holder type requested.");
        }
    }

    private RecipeImageViewHolder buildRecipeImageViewHolder(String imageSource) {
        return new RecipeImageViewHolder(imageSource, inflater, imageService);
    }

    private RecipeIngredientsListViewHolder buildRecipeIngredientsListViewHolder(List<Ingredient> ingredients) {
        return new RecipeIngredientsListViewHolder(ingredients, inflater);
    }

    private RecipeTimingsViewHolder buildRecipeTimingsViewHolder(RecipeTimings timings) {
        return new RecipeTimingsViewHolder(timings, inflater);
    }

    private RecipeServingsViewHolder buildRecipeServingsViewHolder(int numberOfServings) {
        return new RecipeServingsViewHolder(numberOfServings, inflater);
    }

    private RecipeMealtypeViewHolder buildRecipeMealtypeViewHolder(String mealtype) {
        return new RecipeMealtypeViewHolder(mealtype, inflater);
    }

    private RecipeInstructionViewHolder buildRecipeInstructionViewHolder(Instruction instruction) {
        return new RecipeInstructionViewHolder(instruction, inflater);
    }

    private RecipeEquipmentListViewHolder buildRecipeEquipmentListViewHolder(List<Equipment> equipment) {
        return new RecipeEquipmentListViewHolder(equipment, inflater);
    }

    private RecipeAuthorViewHolder buildRecipeAuthorViewHolder(String author) {
        return new RecipeAuthorViewHolder(author, inflater);
    }

    private RecipeNameViewHolder buildTitleViewHolder(String title) {
        return new RecipeNameViewHolder(title, inflater);
    }

}
