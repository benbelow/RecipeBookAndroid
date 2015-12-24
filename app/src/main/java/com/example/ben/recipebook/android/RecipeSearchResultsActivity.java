package com.example.ben.recipebook.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.fetching.DataFetchingService;
import com.example.ben.recipebook.fetching.RecipeSearchTerms;
import com.example.ben.recipebook.models.recipe.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeSearchResultsActivity extends FragmentActivity implements RecipeListFragment.OnFragmentInteractionListener {

    private List<Recipe> recipes = new ArrayList<>();

    @Inject
    DataFetchingService fetchingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search_results);
        ((RecipeApplication) getApplication()).getApplicationComponent().inject(this);

        Serializable searchTerms = getIntent().getExtras().getSerializable("searchTerms");
        RecipeListFragment fragment = RecipeListFragment.newInstance(searchTerms);

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.search_results_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_search_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
