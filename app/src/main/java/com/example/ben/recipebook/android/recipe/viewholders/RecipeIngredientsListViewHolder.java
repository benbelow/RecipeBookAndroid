package com.example.ben.recipebook.android.recipe.viewholders;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;
import com.example.ben.recipebook.models.Ingredient;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeIngredientsListViewHolder extends ViewHolder {

    private final LayoutInflater inflater;
    @Bind(R.id.recipe_ingredients_list)
    LinearLayout ingredientList;

    public RecipeIngredientsListViewHolder(List<Ingredient> ingredients, LayoutInflater inflater) {
        super(inflater.inflate(R.layout.template_recipe_ingredients_list, null));
        this.inflater = inflater;
        ButterKnife.bind(this, getView());
        updateContent(ingredients);
    }

    @Override
    public void updateContent(Object item) {
        List<Ingredient> ingredients = (List<Ingredient>)item;

        for(Ingredient ingredient : ingredients){
            addIngredientView(ingredient);
        }
    }

    private void addIngredientView(Ingredient ingredient) {
        View ingredientView = inflater.inflate(R.layout.template_recipe_ingredient, ingredientList, false);
        RecipeIngredientViewHolder viewHolder = new RecipeIngredientViewHolder(ingredient, ingredientView);
        viewHolder.updateContent(ingredient);
        ingredientList.addView(ingredientView);
    }
}
