package com.example.ben.recipebook.models;

import java.io.Serializable;

public class Ingredient implements Serializable{

    public Ingredient(String name, int amount, String units){
        this.Name = name;
        Amount = amount;
        Units = units;
    }

    public final String Name;
    public final int Amount;
    public final String Units;

}
