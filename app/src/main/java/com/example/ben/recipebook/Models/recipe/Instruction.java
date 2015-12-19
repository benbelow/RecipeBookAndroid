package com.example.ben.recipebook.models.recipe;

import java.io.Serializable;

public class Instruction implements Serializable {

    public final int stepNumber;
    public final String stepDescription;

    public Instruction(int stepNumber, String stepDescription) {
        this.stepNumber = stepNumber;
        this.stepDescription = stepDescription;
    }
}
