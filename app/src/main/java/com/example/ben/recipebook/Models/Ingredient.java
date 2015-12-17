package com.example.ben.recipebook.Models;

import java.io.Serializable;

public class Ingredient implements Serializable{

    public Ingredient(String name){
        this.Name = name;
    }

    public final String Name;

}
