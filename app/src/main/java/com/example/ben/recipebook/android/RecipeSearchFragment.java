package com.example.ben.recipebook.android;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;
import com.example.ben.recipebook.models.recipe.Recipe;
import com.example.ben.recipebook.services.DataFetchingService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RecipeSearchFragment extends Fragment {

    private LayoutInflater inflater;

    private List<String> ingredientNames = new ArrayList<>();
    private List<String> equipmentNames = new ArrayList<>();
    private ArrayAdapter equipmentNamesAdapter;
    private ArrayAdapter ingredientNamesAdapter;

    @Inject
    DataFetchingService fetchingService;

    @Bind(R.id.recipe_search_name)
    EditText nameSearch;

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

    @Bind(R.id.search_max_time_hours)
    NumberPicker maxTimeHours;

    @Bind(R.id.search_max_time_minutes)
    NumberPicker maxTimeMinutes;

    @Bind(R.id.time_header)
    LinearLayout timeHeader;

    @Bind(R.id.time_search_image)
    ImageView timeSearchImage;

    @Bind(R.id.time_container)
    LinearLayout timeContainer;

    @Bind(R.id.servings_header)
    LinearLayout servingsHeader;

    @Bind(R.id.servings_search_image)
    ImageView servingsSearchImage;

    @Bind(R.id.recipe_search_min_servings)
    NumberPicker minServingsSearch;

    public static RecipeSearchFragment newInstance() {
        RecipeSearchFragment fragment = new RecipeSearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_recipe_search, container, false);
        ButterKnife.bind(this, view);
        this.inflater = inflater;

        fetchIngredientList();
        fetchEquipmentList();
        setUpAddSearchTermButton(addSearchIngredientButton, ingredientList, ingredientNamesAdapter);
        setUpAddSearchTermButton(addSearchEquipmentButton, equipmentList, equipmentNamesAdapter);
        setUpSearchButton();

        //ToDo: make these numbers less magic, at least constants
        maxTimeHours.setMaxValue(23);
        String[] minuteDisplayedValues = new String[]{"0", "15", "30", "45"};
        maxTimeMinutes.setMaxValue(minuteDisplayedValues.length - 1);
        maxTimeMinutes.setWrapSelectorWheel(true);
        maxTimeMinutes.setDisplayedValues(minuteDisplayedValues);
        minServingsSearch.setMaxValue(15);

        timeHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeContainer.getVisibility() == View.VISIBLE) {
                    timeSearchImage.setImageResource(R.drawable.expand_arrow);
                    timeContainer.setVisibility(View.GONE);
                } else {
                    timeSearchImage.setImageResource(R.drawable.collapse_arrow);
                    timeContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        servingsHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minServingsSearch.getVisibility() == View.VISIBLE) {
                    servingsSearchImage.setImageResource(R.drawable.expand_arrow);
                    minServingsSearch.setVisibility(View.GONE);
                } else {
                    servingsSearchImage.setImageResource(R.drawable.collapse_arrow);
                    minServingsSearch.setVisibility(View.VISIBLE);
                }
            }
        });


        return view;
    }

    private void fetchIngredientList() {
        Call<List<Ingredient>> ingredientsCall = fetchingService.service.listIngredients();
        ingredientsCall.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Response<List<Ingredient>> response, Retrofit retrofit) {
                List<Ingredient> ingredients = response.body();
                if (response.isSuccess()) {
                    for (Ingredient ingredient : ingredients) {
                        ingredientNames.add(ingredient.Name);
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
        Call<List<Equipment>> equipmentCall = fetchingService.service.listEquipment();
        equipmentCall.enqueue(new Callback<List<Equipment>>() {
            @Override
            public void onResponse(Response<List<Equipment>> response, Retrofit retrofit) {
                List<Equipment> equipments = response.body();
                if (response.isSuccess()) {
                    for (Equipment equipment : equipments) {
                        equipmentNames.add(equipment.Name);
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

                Map<String, String> searchParams = new HashMap<String, String>();

                String name = nameSearch.getText().toString();
                int minutes = Integer.parseInt(maxTimeMinutes.getDisplayedValues()[maxTimeMinutes.getValue()]);
                Integer maxTotalTime = (maxTimeHours.getValue() * 60) + minutes;
                Integer minServings = minServingsSearch.getValue();

                if (!name.isEmpty()) {
                    searchParams.put("name", name);
                }
                if (maxTotalTime != 0) {
                    searchParams.put("maxTotalTime", maxTotalTime.toString());
                }
                if (minServings != 0) {
                    searchParams.put("minNumberOfServings", minServings.toString());
                }

                List<String> ingredientsAll = new ArrayList<>();
                List<String> equipment = new ArrayList<>();

                for (int i = 0; i < ingredientList.getChildCount(); i++) {
                    View view = ingredientList.getChildAt(i);
                    if (view instanceof LinearLayout) {
                        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.search_item);
                        ingredientsAll.add(textView.getText().toString());
                    }
                }

                for (int i = 0; i < equipmentList.getChildCount(); i++) {
                    View view = equipmentList.getChildAt(i);
                    if (view instanceof LinearLayout) {
                        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.search_item);
                        equipment.add(textView.getText().toString());
                    }
                }

                searchParams.put("limit", "100");
                Call<List<Recipe>> call = fetchingService.service.listRecipes(searchParams, null, ingredientsAll, equipment);

                call.enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Response<List<Recipe>> response, Retrofit retrofit) {
                        List<Recipe> recipes = response.body();

                        if (response.isSuccess()) {
                            if (recipes.size() > 0) {
                                Intent recipeSearchResultsIntent = new Intent(getActivity(), RecipeSearchResultsActivity.class);
                                recipeSearchResultsIntent.putExtra("recipes", (Serializable) recipes);
                                startActivity(recipeSearchResultsIntent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

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
