package com.example.ben.recipebook.android;

import android.content.Intent;
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
import android.widget.Switch;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.fetching.DataFetchingService;
import com.example.ben.recipebook.fetching.RecipeSearchTerms;
import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @Bind(R.id.search_restrict_container)
    LinearLayout restrictContainer;

    @Bind(R.id.restrict_image)
    ImageView restrictImage;

    @Bind(R.id.restrict_ingredient_toggle)
    Switch restrictIngredients;

    @Bind(R.id.restrict_equipment_toggle)
    Switch restrictEquipment;

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
        String[] minuteDisplayedValues = new String[]{"0", "15", "30", "45"};
        String[] hourDisplayedValues = new String[]{"0", "1", "2", "3", "4", "5", "10", "15"};
        String[] servingsDisplayedValues = new String[]{"1", "2", "3", "4", "5", "6", "8", "12"};
        setUpNumberPicker(maxTimeMinutes, minuteDisplayedValues);
        setUpNumberPicker(maxTimeHours, hourDisplayedValues);
        setUpNumberPicker(minServingsSearch, servingsDisplayedValues);

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

    @OnClick(R.id.restrict_header)
    public void setUpCollapsingRestrictHeader(){
        if (restrictContainer.getVisibility() == View.VISIBLE) {
            restrictImage.setImageResource(R.drawable.expand_arrow);
            restrictContainer.setVisibility(View.GONE);
        } else {
            restrictImage.setImageResource(R.drawable.collapse_arrow);
            restrictContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setUpNumberPicker(NumberPicker picker, String[] displayedValues) {
        picker.setMaxValue(displayedValues.length - 1);
        picker.setWrapSelectorWheel(true);
        picker.setDisplayedValues(displayedValues);
    }

    private String getNumberPickerDisplayedValue(NumberPicker picker) {
        return picker.getDisplayedValues()[picker.getValue()];
    }

    private void fetchIngredientList() {
        Call<List<Ingredient>> ingredientsCall = fetchingService.getService().listIngredients();
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
        Call<List<Equipment>> equipmentCall = fetchingService.getService().listEquipment();
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
                int minutes = Integer.parseInt(getNumberPickerDisplayedValue(maxTimeMinutes));
                int hours = Integer.parseInt(getNumberPickerDisplayedValue(maxTimeHours));
                Integer maxTotalTime = (hours * 60) + minutes;
                String minServings = getNumberPickerDisplayedValue(minServingsSearch);
                List<String> ingredientsAll = new ArrayList<>();
                List<String> equipment = new ArrayList<>();

                for (int i = 0; i < ingredientList.getChildCount(); i++) {
                    View view = ingredientList.getChildAt(i);
                    if (view instanceof LinearLayout) {
                        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.search_item);
                        String ingredientName = textView.getText().toString();
                        if (!ingredientName.isEmpty()) {
                            ingredientsAll.add(ingredientName);
                        }
                    }
                }

                for (int i = 0; i < equipmentList.getChildCount(); i++) {
                    View view = equipmentList.getChildAt(i);
                    if (view instanceof LinearLayout) {
                        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.search_item);
                        String equipmentName = textView.getText().toString();
                        if(!equipmentName.isEmpty()){
                            equipment.add(equipmentName);
                        }
                    }
                }

                RecipeSearchTerms searchTerms = new RecipeSearchTerms();

                searchTerms.name = (nameSearch.getText().toString());
                searchTerms.maxTime = (maxTotalTime);
                searchTerms.minServings = (Integer.parseInt(minServings));
                searchTerms.ingredients = (ingredientsAll);
                searchTerms.equipments = (equipment);

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
