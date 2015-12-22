package com.example.ben.recipebook.models;

import java.io.Serializable;

public class Equipment implements Serializable{

    public Equipment(String name){
        this.Name = name;
    }

    public final String Name;

    @Override
    public String toString(){
        return Name;
    }

}
