package com.example.ben.recipebook.android.recipe.viewholders;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ben.recipebook.models.Ingredient;
import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeIngredientViewHolder extends ViewHolder{

    @Bind(R.id.recipe_ingredient_amount)
    TextView amountView;

    @Bind(R.id.recipe_ingredient_units)
    TextView unitsView;

    @Bind(R.id.recipe_ingredient_name)
    TextView nameView;

    public RecipeIngredientViewHolder(Ingredient ingredient, View view){
        super(view);
        ButterKnife.bind(this, getView());
    }

    @Override
    public void updateContent(Object item) {
        Ingredient ingredient = (Ingredient) item;

        amountView.setText(Integer.toString(ingredient.Amount));
        unitsView.setText(ingredient.Units);
        nameView.setText(ingredient.Name);

        if(ingredient.Units == ""){
            unitsView.setVisibility(View.GONE);
        }
    }
}
