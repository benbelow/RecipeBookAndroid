package com.example.ben.recipebook.android;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.recipe.RecipeActivity;
import com.example.ben.recipebook.models.Ingredient;
import com.example.ben.recipebook.models.recipe.Recipe;
import com.example.ben.recipebook.services.DataFetchingService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RecipeSearchFragment extends Fragment{

    @Bind(R.id.recipe_search_name)
    EditText nameSearch;

    @Bind(R.id.recipe_search_ingredient)
    AutoCompleteTextView ingredientSearch;

    @Bind(R.id.recipe_search_button)
    ImageButton searchButton;

    private ArrayList<String> ingredientNames = new ArrayList<>();

    private ArrayAdapter<String> mAdapter;

    public static RecipeSearchFragment newInstance() {
        RecipeSearchFragment fragment = new RecipeSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_search, container, false);
        ButterKnife.bind(this, view);


        setUpIngredientDropDown();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataFetchingService service = new DataFetchingService();
                Map<String, String> searchParams = new HashMap<String, String>();
                if(!nameSearch.getText().toString().isEmpty()) {
                    searchParams.put("name", nameSearch.getText().toString());
                }

                List<String> ingredientsAny = new ArrayList<String>();
                if(!ingredientSearch.getText().toString().isEmpty()) {
                    ingredientsAny.add(ingredientSearch.getText().toString());
                }
                for(String ingredientName : ingredientsAny){
                    searchParams.put("ingredientsAny", ingredientName);
                }

                searchParams.put("limit", "1");
                Call<List<Recipe>> call = service.service.listRecipes(searchParams);

                call.enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Response<List<Recipe>> response, Retrofit retrofit) {
                        List<Recipe> recipes = response.body();

                        if (recipes.size() > 0) {
                            Intent recipeIntent = new Intent(getActivity(), RecipeActivity.class);
                            recipeIntent.putExtra("Recipe", recipes.get(0));
                            startActivity(recipeIntent);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

            }
        });
        return view;
    }

    private void setUpIngredientDropDown(){
        DataFetchingService service = new DataFetchingService();
        Call<List<Ingredient>> call = service.service.listIngredients();


        mAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, ingredientNames);

        call.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Response<List<Ingredient>> response, Retrofit retrofit) {
                List<Ingredient> ingredients = response.body();
                for(Ingredient i : ingredients){
                    ingredientNames.add(i.Name);
                }
                ((BaseAdapter)mAdapter).notifyDataSetChanged();

                ingredientSearch.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
