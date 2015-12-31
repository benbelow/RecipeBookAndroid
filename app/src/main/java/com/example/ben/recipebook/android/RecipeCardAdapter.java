package com.example.ben.recipebook.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeViewHolderMapping;
import com.example.ben.recipebook.fetching.ImageService;
import com.example.ben.recipebook.models.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardViewHolder> {

    private ImageService imageService;

    private List<Recipe> recipes = new ArrayList<>();

    @Inject
    public RecipeCardAdapter(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public RecipeCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.template_recipe_card, parent, false);

        return new RecipeCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeCardViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.title.setText(recipe.Name);

        holder.description.setText(recipe.Description);

        if (recipe.ImageSource != null && !recipe.ImageSource.isEmpty()) {
            imageService.loadImageIntoView(holder.image, recipe.ImageSource);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

}
