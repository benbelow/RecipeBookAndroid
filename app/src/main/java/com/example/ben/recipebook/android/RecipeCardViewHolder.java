package com.example.ben.recipebook.android;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.fetching.ImageService;
import com.example.ben.recipebook.models.recipe.Recipe;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeCardViewHolder extends ViewHolder {

    @Bind(R.id.recipe_card_title)
    TextView title;

    @Bind(R.id.recipe_card_image)
    ImageView image;
    private ImageService imageService;

    public RecipeCardViewHolder(Recipe recipe, LayoutInflater inflater, ImageService imageService) {
        super(inflater.inflate(R.layout.template_recipe_card, null));
        this.imageService = imageService;
        ButterKnife.bind(this, getView());
        updateContent(recipe);
    }

    @Override
    public void updateContent(Object item) {
        Recipe recipe = (Recipe) item;
        title.setText(recipe.Name);

        if(recipe.ImageSource != null && !recipe.ImageSource.isEmpty()){
            imageService.loadImageIntoView(image, recipe.ImageSource);
        }
    }
}
