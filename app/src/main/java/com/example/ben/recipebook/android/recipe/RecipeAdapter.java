package com.example.ben.recipebook.android.recipe;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.ben.recipebook.android.ViewHolder;
import com.example.ben.recipebook.android.ViewHolderFactory;
import com.example.ben.recipebook.models.recipe.Recipe;

import javax.inject.Inject;

public class RecipeAdapter extends BaseAdapter {

    private RecipeViewHolderMapping recipeViewHolderMapping;

    private ViewHolderFactory viewHolderFactory;

    @Inject
    public RecipeAdapter(ViewHolderFactory viewHolderFactory){
        this.viewHolderFactory = viewHolderFactory;
    }

    public void setRecipe(Recipe recipe){
        recipeViewHolderMapping = new RecipeViewHolderMapping(recipe, viewHolderFactory);
    }

    @Override
    public int getCount() {
        if(recipeViewHolderMapping == null) return 0;
        return recipeViewHolderMapping.getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return recipeViewHolderMapping.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        viewHolder = recipeViewHolderMapping.getViewHolder(position);

        return viewHolder.getView();
    }
}
