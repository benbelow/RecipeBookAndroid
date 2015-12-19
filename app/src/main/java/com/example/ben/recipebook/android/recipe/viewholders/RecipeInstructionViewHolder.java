package com.example.ben.recipebook.android.recipe.viewholders;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;
import com.example.ben.recipebook.models.recipe.Instruction;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RecipeInstructionViewHolder extends ViewHolder {

    @Bind(R.id.recipe_instruction)
    TextView instructionView;

    public RecipeInstructionViewHolder(Instruction instruction, LayoutInflater inflater) {
        super(inflater.inflate(R.layout.template_recipe_instruction, null));
        ButterKnife.bind(this, getView());
        updateContent(instruction);
    }

    @Override
    public void updateContent(Object item) {
        Instruction instruction = (Instruction) item;

        instructionView.setText(instruction.stepDescription);
    }
}
