package com.example.ben.recipebook.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.recipeList.RecipeCardViewHolder;
import com.example.ben.recipebook.fetching.DataFetchingService;
import com.example.ben.recipebook.models.Ingredient;
import com.example.ben.recipebook.models.StoreCupboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class StoreCupboardIngredientFragment extends Fragment implements Saveable {

    private LayoutInflater inflater;

    private List<String> ingredientNames = new ArrayList<>();
    private ArrayAdapter ingredientNamesAdapter;

    private List<String> ingredients = new ArrayList<>();

    @Inject
    DataFetchingService fetchingService;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    StoreCupboardItemAdapter ingredientAdapter;

    @Bind(R.id.add_search_ingredient)
    FloatingActionButton addSearchIngredientButton;

    @Bind(R.id.ingredients)
    RecyclerView ingredientList;

    public static StoreCupboardIngredientFragment newInstance() {
        StoreCupboardIngredientFragment fragment = new StoreCupboardIngredientFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RecipeApplication) getActivity().getApplication()).getApplicationComponent().inject(this);

        ingredientNamesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, ingredientNames);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_cupboard_ingredients, container, false);
        ButterKnife.bind(this, view);
        this.inflater = inflater;

        ingredientList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        ingredientList.setAdapter(ingredientAdapter);

        fetchIngredientList();
        setUpAddSearchTermButton(addSearchIngredientButton, ingredientNamesAdapter);

        final List<String> savedIngredients = new ArrayList<>();

        fetchingService.getService().storeCupboard().enqueue(new Callback<StoreCupboard>() {
            @Override
            public void onResponse(Response<StoreCupboard> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    StoreCupboard storeCupboard = response.body();
                    for (Ingredient i : storeCupboard.ingredients) {
                        savedIngredients.add(i.Name);
                    }

                    Collections.sort(savedIngredients, String.CASE_INSENSITIVE_ORDER);
                    Collections.reverse(savedIngredients);

                    if (!savedIngredients.isEmpty()) {
                        addViewsForSavedSearchTerms(savedIngredients, ingredientNamesAdapter);
                    }

                    ingredientAdapter.setItems(savedIngredients);
                    ingredientAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        return view;
    }

    private void addViewsForSavedSearchTerms(List<String> savedStrings, ArrayAdapter adapter) {
        for (String s : savedStrings) {
            final LinearLayout newSearchTermLayout = (LinearLayout) this.inflater.inflate(R.layout.template_search_item, null);
            final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) newSearchTermLayout.findViewById(R.id.search_item);

            autoCompleteTextView.setAdapter(adapter);

            ingredients.add("saved");

            autoCompleteTextView.setText(s);
        }
    }

    private void fetchIngredientList() {
        Call<List<Ingredient>> ingredientsCall = fetchingService.getService().listIngredients();
        ingredientsCall.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Response<List<Ingredient>> response, Retrofit retrofit) {
                List<Ingredient> ingredients = response.body();
                if (response.isSuccess()) {
                    for (Ingredient ingredient : ingredients) {
                        ingredientNamesAdapter.add(ingredient.Name);
                    }
                    ingredientNamesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setUpAddSearchTermButton(FloatingActionButton button, final ArrayAdapter adapter) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LinearLayout newSearchTermLayout = (LinearLayout) inflater.inflate(R.layout.template_search_item, null);
                final AutoCompleteTextView view = (AutoCompleteTextView) newSearchTermLayout.findViewById(R.id.search_item);

                view.setAdapter(adapter);

                ingredients.add("string");
                newSearchTermLayout.requestFocus();
            }
        });
    }

    @Override
    public void save() {

        fetchingService.getService().postStoreCupboardIngredients(ingredientAdapter.getItems()).enqueue(new Callback<StoreCupboard>() {
            @Override
            public void onResponse(Response<StoreCupboard> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
