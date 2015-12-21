package com.example.ben.recipebook.android;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ToggleButton;

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

    private LayoutInflater inflater;

    @Bind(R.id.recipe_search_name)
    EditText nameSearch;

    @Bind(R.id.recipe_search_button)
    ImageButton searchButton;

    @Bind(R.id.add_search_ingredient)
    ImageButton addSearchIngredientButton;

    @Bind(R.id.recipe_search_ingredient_list)
    LinearLayout ingredientList;


    public static RecipeSearchFragment newInstance() {
        RecipeSearchFragment fragment = new RecipeSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_search, container, false);
        ButterKnife.bind(this, view);
        this.inflater = inflater;

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataFetchingService service = new DataFetchingService();
                Map<String, String> searchParams = new HashMap<String, String>();
                if (!nameSearch.getText().toString().isEmpty()) {
                    searchParams.put("name", nameSearch.getText().toString());
                }

                List<String> ingredientsAny = new ArrayList();
                List<String> ingredientsAll = new ArrayList();

                for(int i=0; i < ingredientList.getChildCount(); i++){
                    View view = ingredientList.getChildAt(i);
                    if(view instanceof LinearLayout){
                        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.recipe_search_ingredient);
                        ToggleButton requiredButton = (ToggleButton) view.findViewById(R.id.recipe_ingredient_required);

                        if(requiredButton.isChecked()){
                            ingredientsAll.add((textView).getText().toString());
                        } else{
                            ingredientsAny.add((textView).getText().toString());
                        }
                    }
                }

                searchParams.put("limit", "1");
                Call<List<Recipe>> call = service.service.listRecipes(searchParams, ingredientsAny, ingredientsAll, null);

                call.enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Response<List<Recipe>> response, Retrofit retrofit) {
                        List<Recipe> recipes = response.body();

                        if (recipes.size() > 0) {

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            fragmentTransaction.replace(R.id.container, RecipeSearchResultsFragment.newInstance(recipes)).commit();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

            }
        });

        addSearchIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout newIngredient = (LinearLayout)inflater.inflate(R.layout.template_ingredient_search, null);

                AutoCompleteTextView view = (AutoCompleteTextView)newIngredient.findViewById(R.id.recipe_search_ingredient);

                setUpIngredientDropDown(view);

                ingredientList.addView(newIngredient, 1);

                newIngredient.requestFocus();
            }
        });

        return view;
    }

    private void setUpIngredientDropDown(final AutoCompleteTextView ingredientSearchView){
        final DataFetchingService service = new DataFetchingService();
        Call<List<Ingredient>> call = service.service.listIngredients();

        final ArrayList<String> ingredientNames = new ArrayList<>();

        final ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, ingredientNames);

        call.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Response<List<Ingredient>> response, Retrofit retrofit) {
                List<Ingredient> ingredients = response.body();
                for(Ingredient i : ingredients){
                    ingredientNames.add(i.Name);
                }
                mAdapter.notifyDataSetChanged();

                ingredientSearchView.setAdapter(mAdapter);
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
