package com.example.ben.recipebook.android.recipe.viewholders;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;
import com.example.ben.recipebook.models.recipe.RecipeTimings;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RecipeTimingsViewHolder extends ViewHolder {

    @Bind(R.id.recipe_prep_time)
    TextView prepTimeView;

    @Bind(R.id.recipe_cook_time)
    TextView cookTimeView;

    public RecipeTimingsViewHolder(RecipeTimings recipeTimings, LayoutInflater inflater) {
        super(inflater.inflate(R.layout.template_recipe_timings, null));
        ButterKnife.bind(this, getView());
        updateContent(recipeTimings);
    }

    @Override
    public void updateContent(Object item) {
        RecipeTimings recipeTimings = (RecipeTimings) item;

        prepTimeView.setText(Integer.toString(recipeTimings.prepTime));
        cookTimeView.setText(Integer.toString(recipeTimings.cookTime));
    }
}
