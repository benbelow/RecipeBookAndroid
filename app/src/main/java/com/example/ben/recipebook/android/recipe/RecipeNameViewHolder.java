package com.example.ben.recipebook.android.recipe;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RecipeNameViewHolder extends ViewHolder {

    @Bind(R.id.recipe_name_text)
    TextView nameView;

    public RecipeNameViewHolder(String name, LayoutInflater inflater) {
        super(inflater.inflate(R.layout.template_recipe_name, null));
        ButterKnife.bind(this, getView());
        updateContent(name);
    }

    @Override
    public void updateContent(Object nameObject) {
        String name = (String) nameObject;

        nameView.setText(name);
    }
}
