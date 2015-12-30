package com.example.ben.recipebook.android.recipe.viewholders;

import android.view.LayoutInflater;
import android.widget.ImageView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;
import com.example.ben.recipebook.fetching.ImageService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeImageViewHolder extends ViewHolder {

    @Bind(R.id.recipe_image)
    ImageView image;

    private ImageService imageService;

    public RecipeImageViewHolder(String imageSource, LayoutInflater inflater, ImageService imageService) {
        super(inflater.inflate(R.layout.template_recipe_image, null));
        ButterKnife.bind(this, getView());
        this.imageService = imageService;
        updateContent(imageSource);
    }

    @Override
    public void updateContent(Object item) {
        String imageSource = (String) item;

        imageService.loadImageIntoView(image, imageSource);
    }
}
