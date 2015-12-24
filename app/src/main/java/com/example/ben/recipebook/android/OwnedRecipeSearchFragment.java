package com.example.ben.recipebook.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.fetching.DataFetchingService;
import com.example.ben.recipebook.fetching.OwnedRecipeSearchTerms;
import com.example.ben.recipebook.fetching.RecipeSearchTerms;
import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OwnedRecipeSearchFragment extends Fragment {

    private LayoutInflater inflater;

    private List<String> ingredientNames = new ArrayList<>();
    private List<String> equipmentNames = new ArrayList<>();
    private ArrayAdapter equipmentNamesAdapter;
    private ArrayAdapter ingredientNamesAdapter;

    @Inject
    DataFetchingService fetchingService;

    @Inject
    SharedPreferences sharedPreferences;

    @Bind(R.id.recipe_search_button)
    ImageButton searchButton;

    @Bind(R.id.add_search_ingredient)
    ImageButton addSearchIngredientButton;

    @Bind(R.id.add_search_equipment)
    ImageButton addSearchEquipmentButton;

    @Bind(R.id.recipe_search_ingredient_list)
    LinearLayout ingredientList;

    @Bind(R.id.recipe_search_equipment_list)
    LinearLayout equipmentList;

    private String savedIngredientsKey;

    public static OwnedRecipeSearchFragment newInstance() {
        OwnedRecipeSearchFragment fragment = new OwnedRecipeSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RecipeApplication) getActivity().getApplication()).getApplicationComponent().inject(this);

        equipmentNamesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, equipmentNames);
        ingredientNamesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, ingredientNames);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owned_recipe_search, container, false);
        ButterKnife.bind(this, view);
        this.inflater = inflater;

        fetchIngredientList();
        fetchEquipmentList();
        setUpAddSearchTermButton(addSearchEquipmentButton, equipmentList, equipmentNamesAdapter);
        setUpSearchButton();

        savedIngredientsKey = "com.example.app.savedIngredients";
        Set<String> savedIngredients = sharedPreferences.getStringSet(savedIngredientsKey, new HashSet<String>());

        for (String ingredient : savedIngredients) {
            final LinearLayout newSearchTermLayout = (LinearLayout) inflater.inflate(R.layout.template_search_item, null);
            final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) newSearchTermLayout.findViewById(R.id.search_item);

            autoCompleteTextView.setAdapter(ingredientNamesAdapter);

            ingredientList.addView(newSearchTermLayout, 0);
            newSearchTermLayout.requestFocus();

            autoCompleteTextView.setText(ingredient);

            ImageButton removeButton = (ImageButton) newSearchTermLayout.findViewById(R.id.remove_search_item);
            removeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ingredientList.removeView(newSearchTermLayout);
                }
            });
        }

        setUpAddSearchTermButton(addSearchIngredientButton, ingredientList, ingredientNamesAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Set<String> ingredientNames = new HashSet<>();
        for (int i = 0; i < ingredientList.getChildCount(); i++) {
            View view = ingredientList.getChildAt(i);
            if (view instanceof LinearLayout) {
                AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.search_item);
                String ingredientName = textView.getText().toString();
                ingredientNames.add(ingredientName);
            }
        }
        sharedPreferences.edit().putStringSet(savedIngredientsKey, ingredientNames).apply();
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

    private void fetchEquipmentList() {
        Call<List<Equipment>> equipmentCall = fetchingService.getService().listEquipment();
        equipmentCall.enqueue(new Callback<List<Equipment>>() {
            @Override
            public void onResponse(Response<List<Equipment>> response, Retrofit retrofit) {
                List<Equipment> equipments = response.body();
                if (response.isSuccess()) {
                    for (Equipment equipment : equipments) {
                        equipmentNamesAdapter.add(equipment.Name);
                    }
                    equipmentNamesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setUpSearchButton() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> ingredientsAll = new ArrayList<>();
                List<String> equipment = new ArrayList<>();

                for (int i = 0; i < ingredientList.getChildCount(); i++) {
                    View view = ingredientList.getChildAt(i);
                    if (view instanceof LinearLayout) {
                        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.search_item);
                        String ingredientName = textView.getText().toString();
                        ingredientsAll.add(ingredientName);
                    }
                }


                for (int i = 0; i < equipmentList.getChildCount(); i++) {
                    View view = equipmentList.getChildAt(i);
                    if (view instanceof LinearLayout) {
                        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.search_item);
                        String equipmentName = textView.getText().toString();
                        if (!equipmentName.isEmpty()) {
                            equipment.add(equipmentName);
                        }
                    }
                }

                OwnedRecipeSearchTerms searchTerms = new OwnedRecipeSearchTerms();

                searchTerms.ingredientsOwned = ingredientsAll;
                searchTerms.equipments = equipment;

                Intent recipeSearchResultsIntent = new Intent(getActivity(), RecipeSearchResultsActivity.class);
                recipeSearchResultsIntent.putExtra("searchTerms", searchTerms);
                startActivity(recipeSearchResultsIntent);
            }
        });
    }

    private void setUpAddSearchTermButton(ImageButton button, final LinearLayout layout, final ArrayAdapter adapter) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LinearLayout newSearchTermLayout = (LinearLayout) inflater.inflate(R.layout.template_search_item, null);
                final AutoCompleteTextView view = (AutoCompleteTextView) newSearchTermLayout.findViewById(R.id.search_item);

                view.setAdapter(adapter);

                layout.addView(newSearchTermLayout, 0);
                newSearchTermLayout.requestFocus();

                ImageButton removeButton = (ImageButton) newSearchTermLayout.findViewById(R.id.remove_search_item);
                removeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        layout.removeView(newSearchTermLayout);
                    }
                });
            }
        });
    }
}
