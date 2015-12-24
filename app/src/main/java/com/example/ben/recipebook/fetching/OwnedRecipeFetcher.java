package com.example.ben.recipebook.fetching;

import com.example.ben.recipebook.models.recipe.Recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;

public class OwnedRecipeFetcher implements IListFetcher<Recipe> {

    private RecipeService service;
    private OwnedRecipeSearchTerms searchTerms;

    public OwnedRecipeFetcher(DataFetchingService service, OwnedRecipeSearchTerms searchTerms) {
        this.searchTerms = searchTerms;
        this.service = service.getService();
    }

    @Override
    public Call<List<Recipe>> fetchListCall() {
        return service.listOwnedRecipes(searchTerms.ingredientsOwned, searchTerms.ingredientsRequired, searchTerms.equipments);
    }
}
