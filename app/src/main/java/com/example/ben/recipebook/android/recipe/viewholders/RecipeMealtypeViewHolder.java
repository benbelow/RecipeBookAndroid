package com.example.ben.recipebook.android.recipe.viewholders;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RecipeMealtypeViewHolder extends ViewHolder {

    @Bind(R.id.recipe_mealtype)
    TextView mealtypeView;

    public RecipeMealtypeViewHolder(String mealType, LayoutInflater inflater) {
        super(inflater.inflate(R.layout.template_recipe_mealtype, null));
        ButterKnife.bind(this, getView());
        updateContent(mealType);
    }

    @Override
    public void updateContent(Object item) {
        String mealType = (String) item;

        mealtypeView.setText(mealType);
    }
}
