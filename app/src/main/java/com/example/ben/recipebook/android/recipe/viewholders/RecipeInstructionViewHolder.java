package com.example.ben.recipebook.android.recipe.viewholders;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;
import com.example.ben.recipebook.models.recipe.Instruction;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RecipeInstructionViewHolder extends ViewHolder {

    @Bind(R.id.recipe_instruction)
    TextView instructionView;

    @Bind(R.id.recipe_instruction_number)
    TextView instructionNumberView;

    public RecipeInstructionViewHolder(Instruction instruction, View view) {
        super(view);
        ButterKnife.bind(this, getView());
        updateContent(instruction);
    }

    @Override
    public void updateContent(Object item) {
        Instruction instruction = (Instruction) item;

        instructionView.setText(instruction.StepDescription);
        instructionNumberView.setText(instruction.StepNumber + ".");
    }
}
