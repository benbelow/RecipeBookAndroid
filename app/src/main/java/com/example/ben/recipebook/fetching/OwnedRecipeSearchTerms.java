package com.example.ben.recipebook.fetching;

import java.io.Serializable;
import java.util.List;

public class OwnedRecipeSearchTerms implements Serializable {

    public List<String> ingredientsOwned;
    public List<String> ingredientsRequired;
    public List<String> equipments;

}
