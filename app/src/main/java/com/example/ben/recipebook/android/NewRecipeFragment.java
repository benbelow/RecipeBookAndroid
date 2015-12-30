package com.example.ben.recipebook.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.fetching.DataFetchingService;
import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NewRecipeFragment extends Fragment {

    private LayoutInflater inflater;

    private List<String> ingredientNames = new ArrayList<>();
    private List<String> equipmentNames = new ArrayList<>();

    private ArrayAdapter equipmentNamesAdapter;
    private ArrayAdapter ingredientNamesAdapter;

    @Inject
    DataFetchingService fetchingService;

    @Bind(R.id.recipe_new_name)
    EditText nameView;

    @Bind(R.id.create_recipe_button)
    ImageButton createRecipeButton;

    @Bind(R.id.recipe_search_ingredient_list)
    LinearLayout ingredientList;

    @Bind(R.id.recipe_search_equipment_list)
    LinearLayout equipmentList;

    @Bind(R.id.add_ingredient)
    ImageButton addIngredientButton;

    @Bind(R.id.add_equipment)
    ImageButton addEquipmentButton;

    public static NewRecipeFragment newInstance() {
        NewRecipeFragment fragment = new NewRecipeFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);
        ButterKnife.bind(this, view);
        this.inflater = inflater;

        fetchIngredientList();
        fetchEquipmentList();
        setUpAddIngredientButton();

        setUpCreateRecipeButton();

        return view;
    }

    private void setUpCreateRecipeButton() {
        createRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameView.getText().toString();
                List<Ingredient> ingredients = new ArrayList<>();

                for (int i = 0; i < ingredientList.getChildCount(); i++) {
                    View view = ingredientList.getChildAt(i);
                    if (view instanceof LinearLayout) {
                        AutoCompleteTextView nameView = (AutoCompleteTextView) view.findViewById(R.id.ingredient_name);
                        String ingredientName = nameView.getText().toString();

                        EditText amountView = (EditText) view.findViewById(R.id.ingredient_amount);
                        int ingredientAmount = Integer.parseInt(amountView.getText().toString());

                        EditText unitsView = (EditText) view.findViewById(R.id.ingredient_units);
                        String ingredientUnits = unitsView.getText().toString();

                        EditText descriptionView = (EditText) view.findViewById(R.id.ingredient_description);
                        String ingredientDescription = descriptionView.getText().toString();

                        Ingredient ingredient = new Ingredient(ingredientName, ingredientAmount, ingredientUnits, ingredientDescription);
                        ingredients.add(ingredient);
                    }
                }

                int x = 0;
            }
        });
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

    private void setUpAddIngredientButton() {

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout newSearchTermLayout = (LinearLayout) inflater.inflate(R.layout.template_new_ingredient, null);
                final AutoCompleteTextView view = (AutoCompleteTextView) newSearchTermLayout.findViewById(R.id.ingredient_name);

                view.setAdapter(ingredientNamesAdapter);

                ingredientList.addView(newSearchTermLayout, 0);
                newSearchTermLayout.requestFocus();

                ImageButton removeButton = (ImageButton) newSearchTermLayout.findViewById(R.id.remove_item);
                removeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ingredientList.removeView(newSearchTermLayout);
                    }
                });
            }
        });
    }

}
