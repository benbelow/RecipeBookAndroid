package com.example.ben.recipebook.android.recipe;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.ben.recipebook.models.Ingredient;
import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeIngredientViewHolder extends ViewHolder{

    @Bind(R.id.recipe_ingredient_name)
    TextView recipe_ingredient_name;

    public RecipeIngredientViewHolder(Ingredient ingredient, LayoutInflater inflater){
        super(inflater.inflate(R.layout.template_recipe_ingredient_name, null));
        ButterKnife.bind(this, getView());
        updateContent(ingredient);
    }

    @Override
    public void updateContent(Object item) {
        Ingredient ingredient = (Ingredient) item;

        recipe_ingredient_name.setText(ingredient.Name);
    }
}
