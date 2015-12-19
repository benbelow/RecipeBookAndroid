package com.example.ben.recipebook.android.recipe.viewholders;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;
import com.example.ben.recipebook.models.recipe.RecipeTimings;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RecipeTimingsViewHolder extends ViewHolder {

    @Bind(R.id.recipe_total_time)
    TextView totalTimeView;

    public RecipeTimingsViewHolder(RecipeTimings recipeTimings, LayoutInflater inflater) {
        super(inflater.inflate(R.layout.template_recipe_timings, null));
        ButterKnife.bind(this, getView());
        updateContent(recipeTimings);
    }

    @Override
    public void updateContent(Object item) {
        RecipeTimings recipeTimings = (RecipeTimings) item;

        //ToDo extract timeformatter class
        int totalMinutes = recipeTimings.prepTime + recipeTimings.cookTime;
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        String displayedHours = hours > 0 ? hours + "hr " : "";
        String displayedMinutes = minutes + "min";

        totalTimeView.setText(displayedHours + displayedMinutes);
    }
}
