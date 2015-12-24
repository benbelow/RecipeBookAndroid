package com.example.ben.recipebook.fetching;

import java.io.Serializable;
import java.util.List;

public class RecipeSearchTerms implements Serializable {

    public String name = "";
    public List<String> ingredients;
    public List<String> equipments;
    public Integer maxTime = 0;
    public Integer minServings = 0;
    public Integer limit = 100;

}
