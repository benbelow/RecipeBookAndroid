package com.example.ben.recipebook.android.recipe.viewholders;

import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;
import com.example.ben.recipebook.android.ViewHolder;
import com.example.ben.recipebook.android.ViewHolderFactory;
import com.example.ben.recipebook.models.recipe.Instruction;
import com.example.ben.recipebook.models.recipe.Recipe;
import com.example.ben.recipebook.models.recipe.RecipeTimings;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RecipeViewHolderMapping {

    private ViewHolderFactory viewHolderFactory;

    private static final ImmutableList<Class<? extends ViewHolder>> VIEW_HOLDER_CLASSES = ImmutableList.of(
    );

    public Object getItem(int position) {
        return viewHolderDetails.get(position).content;
    }

    public ViewHolder getViewHolder(int position) {
        MappingPair pair = viewHolderDetails.get(position);

        if(pair == null){
            throw new IllegalArgumentException("Asked for view holder for invalid position in mapping.");
        }
        return viewHolderFactory.buildViewHolder(pair.type, pair.content);
    }

    public static class MappingPair{
        protected final Class<? extends ViewHolder> type;
        protected final Object content;

        public MappingPair(Class<? extends ViewHolder> viewHolderType, Object content){
            this.type = viewHolderType;
            this.content = content;
        }
    }

    private final List<MappingPair> viewHolderDetails;

    public RecipeViewHolderMapping(Recipe recipe, ViewHolderFactory viewHolderFactory){
        this.viewHolderFactory = viewHolderFactory;
        viewHolderDetails = Collections.unmodifiableList(mapRecipe(recipe));
    }

    private List<MappingPair> mapRecipe(Recipe recipe) {
        List<MappingPair> recipeViewHolderPairs = new ArrayList<>();

        recipeViewHolderPairs.add(new MappingPair(RecipeNameViewHolder.class, recipe.Name));
        recipeViewHolderPairs.add(new MappingPair(RecipeAuthorViewHolder.class, recipe.Author));
        recipeViewHolderPairs.add(new MappingPair(RecipeServingsViewHolder.class, recipe.NumberOfServings));

        RecipeTimings timings = new RecipeTimings(recipe.PreparationTime, recipe.CookTime);
        recipeViewHolderPairs.add(new MappingPair(RecipeTimingsViewHolder.class, timings));

        recipeViewHolderPairs.add(new MappingPair(RecipeIngredientsListViewHolder.class, recipe.Ingredients));
        recipeViewHolderPairs.add(new MappingPair(RecipeEquipmentListViewHolder.class, recipe.Equipment));

//        for(int i = 0; i < 20; i++){
//            recipeViewHolderPairs.add(new MappingPair(RecipeNameViewHolder.class, "Filler!"));
//        }

        for(Instruction instruction : recipe.Instructions){
            recipeViewHolderPairs.add(new MappingPair(RecipeInstructionViewHolder.class, instruction));
        }

        return recipeViewHolderPairs;
    }

    public int getItemCount() {
        return viewHolderDetails.size();
    }

}
