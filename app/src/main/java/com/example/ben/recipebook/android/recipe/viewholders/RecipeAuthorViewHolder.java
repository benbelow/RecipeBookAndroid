package com.example.ben.recipebook.android.recipe.viewholders;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeAuthorViewHolder extends ViewHolder {

    @Bind(R.id.recipe_author)
    TextView authorView;

    public RecipeAuthorViewHolder(String author, LayoutInflater inflater){
        super(inflater.inflate(R.layout.template_recipe_author, null));
        ButterKnife.bind(this, getView());
        updateContent(author);
    }

    @Override
    public void updateContent(Object item) {
        String author = (String) item;
        authorView.setText(author);
    }
}
