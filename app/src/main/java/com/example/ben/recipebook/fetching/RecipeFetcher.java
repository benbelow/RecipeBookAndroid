package com.example.ben.recipebook.fetching;

import com.example.ben.recipebook.models.recipe.Recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;

public class RecipeFetcher implements IListFetcher<Recipe> {

    private RecipeService service;
    private RecipeSearchTerms searchTerms;

    public RecipeFetcher(DataFetchingService service, RecipeSearchTerms searchTerms) {
        this.searchTerms = searchTerms;
        this.service = service.getService();
    }

    @Override
    public Call<List<Recipe>> fetchListCall() {
        Map<String, String> searchOptions = new HashMap<>();

        if (!searchTerms.name.isEmpty()) {
            searchOptions.put("name", searchTerms.name);
        }

        if (searchTerms.maxTime > 0) {
            searchOptions.put("maxTotalTime", searchTerms.maxTime.toString());
        }

        if (searchTerms.minServings > 1) {
            searchOptions.put("minNumberOfServings", searchTerms.minServings.toString());
        }

        if (searchTerms.restrictIngredients) {
            searchOptions.put("restrictIngredients", "true");
        }

        if (searchTerms.restrictEquipment) {
            searchOptions.put("restrictEquipment", "true");
        }

        searchOptions.put("limit", searchTerms.limit.toString());

        return service.listRecipes(searchOptions, searchTerms.ingredients, searchTerms.equipment);
    }
}
