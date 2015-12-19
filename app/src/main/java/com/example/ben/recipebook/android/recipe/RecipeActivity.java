package com.example.ben.recipebook.android.recipe;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.AppContextComponent;
import com.example.ben.recipebook.android.RecipeApplication;
import com.example.ben.recipebook.models.recipe.Recipe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RecipeActivity extends ActionBarActivity {

    Recipe recipe;

    @Inject
    RecipeAdapter recipeAdapter;

    @Bind(R.id.recipe_items)
    ListView recipeItems;

    private AppContextComponent appContextComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        ((RecipeApplication) getApplication()).getAppContextComponent().inject(this);

        recipe = (Recipe) getIntent().getExtras().getSerializable("Recipe");

        recipeAdapter.setRecipe(recipe);

        recipeItems.setAdapter(recipeAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
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
}
