package com.example.ben.recipebook.android.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.ben.recipebook.android.recipe.viewholders.RecipeIngredientViewHolder;
import com.example.ben.recipebook.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeIngredientAdapter extends BaseAdapter {

    List<RecipeIngredientViewHolder> viewHolders = new ArrayList<>();
    private LayoutInflater inflater;

    @Inject
    public RecipeIngredientAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        for (Ingredient i : ingredients) {
            viewHolders.add(new RecipeIngredientViewHolder(inflater, i));
        }
    }

    @Override
    public int getCount() {
        return viewHolders.size();
    }

    @Override
    public Object getItem(int position) {
        return viewHolders.get(position).ingredient;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return viewHolders.get(position).getView();
    }

    public void scaleIngredientAmounts(float scaleFactor) {
        for (RecipeIngredientViewHolder ingredientViewHolder : viewHolders) {
            float newAmount = ingredientViewHolder.ingredient.Amount * scaleFactor;
            ingredientViewHolder.updateAmount(newAmount);
        }
    }
}
