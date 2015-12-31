package com.example.ben.recipebook.android;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.ben.recipebook.android.recipe.viewholders.RecipeViewHolderMapping;
import com.example.ben.recipebook.models.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeCardAdapter extends BaseAdapter {

    private ViewHolderFactory viewHolderFactory;

    private List<RecipeCardViewHolder> viewHolders = new ArrayList<>();

    private List<Recipe> recipes;

    @Inject
    public RecipeCardAdapter(ViewHolderFactory viewHolderFactory){
        this.viewHolderFactory = viewHolderFactory;
    }

    public void setRecipes(List<Recipe> recipes){
        this.recipes = recipes;
        for(Recipe r : recipes){
            viewHolders.add(viewHolderFactory.buildViewHolder(RecipeCardViewHolder.class, r));
        }
    }

    @Override
    public int getCount() {
        return viewHolders.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        viewHolder = viewHolders.get(position);

        return viewHolder.getView();
    }
}
