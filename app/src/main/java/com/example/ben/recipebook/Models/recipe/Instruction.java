package com.example.ben.recipebook.models.recipe;

import java.io.Serializable;

public class Instruction implements Serializable {

    public final int StepNumber;
    public final String StepDescription;

    public Instruction(int stepNumber, String stepDescription) {
        this.StepNumber = stepNumber;
        this.StepDescription = stepDescription;
    }
}
