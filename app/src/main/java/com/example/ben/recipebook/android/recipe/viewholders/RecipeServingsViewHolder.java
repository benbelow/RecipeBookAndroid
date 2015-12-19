package com.example.ben.recipebook.android.recipe.viewholders;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RecipeServingsViewHolder extends ViewHolder {

    @Bind(R.id.recipe_serving_number)
    TextView servingsView;

    public RecipeServingsViewHolder(int numberOfServings, LayoutInflater inflater) {
        super(inflater.inflate(R.layout.template_recipe_servings, null));
        ButterKnife.bind(this, getView());
        updateContent(numberOfServings);
    }

    @Override
    public void updateContent(Object item) {
        int servings = (int) item;

        servingsView.setText(Integer.toString(servings));
    }
}
