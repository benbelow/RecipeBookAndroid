package com.example.ben.recipebook.models;

import java.io.Serializable;

public class Ingredient implements Serializable{


    public Ingredient(String name, int amount, String units, String description){
        this.Name = name;
        Amount = amount;
        Units = units;
        Description = description;
    }

    public final String Name;
    public final int Amount;
    public final String Units;
    public final String Description;

}
