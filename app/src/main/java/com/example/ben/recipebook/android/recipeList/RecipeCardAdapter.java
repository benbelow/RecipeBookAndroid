package com.example.ben.recipebook.android.recipeList;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.recipe.RecipeActivity;
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
        final Recipe recipe = recipes.get(position);
        holder.title.setText(recipe.Name);

        if (recipe.ImageSource != null && !recipe.ImageSource.isEmpty()) {
            imageService.loadImageIntoView(holder.image, recipe.ImageSource);
            holder.image.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecipeActivity.class);
                intent.putExtra("Recipe", recipe);
                v.getContext().startActivity(intent);
            }
        });
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
