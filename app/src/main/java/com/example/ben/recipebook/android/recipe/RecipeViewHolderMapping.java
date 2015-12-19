package com.example.ben.recipebook.android.recipe;

import com.example.ben.recipebook.models.Ingredient;
import com.example.ben.recipebook.android.ViewHolder;
import com.example.ben.recipebook.android.ViewHolderFactory;
import com.example.ben.recipebook.models.recipe.Recipe;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        for(Ingredient ingredient : recipe.Ingredients){
            recipeViewHolderPairs.add(new MappingPair(RecipeIngredientViewHolder.class, ingredient));
        }

        return recipeViewHolderPairs;
    }

    public int getItemCount() {
        return viewHolderDetails.size();
    }

}
