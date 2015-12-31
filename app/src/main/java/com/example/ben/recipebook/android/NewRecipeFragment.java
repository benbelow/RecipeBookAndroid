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
import android.widget.LinearLayout;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.recipe.RecipeActivity;
import com.example.ben.recipebook.fetching.DataFetchingService;
import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;
import com.example.ben.recipebook.models.recipe.Instruction;
import com.example.ben.recipebook.models.recipe.NewRecipeBody;
import com.example.ben.recipebook.models.recipe.Recipe;

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

    @Bind(R.id.recipe_new_ingredient_list)
    LinearLayout ingredientList;

    @Bind(R.id.recipe_new_equipment_list)
    LinearLayout equipmentList;

    @Bind(R.id.recipe_new_instruction_list)
    LinearLayout instructionList;

    @Bind(R.id.add_ingredient)
    ImageButton addIngredientButton;

    @Bind(R.id.add_equipment)
    ImageButton addEquipmentButton;

    @Bind(R.id.add_instruction)
    ImageButton addInstructionButton;

    @Bind(R.id.recipe_new_description)
    EditText descriptionView;

    @Bind(R.id.recipe_new_meal_type)
    EditText mealTypeView;

    @Bind(R.id.recipe_new_prep_time)
    EditText prepTimeView;

    @Bind(R.id.recipe_new_cook_time)
    EditText cookTimeView;

    @Bind(R.id.recipe_new_number_of_servings)
    EditText servingNumberView;

    @Bind(R.id.recipe_new_author)
    EditText authorView;


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
        setUpAddEquipmentButton();
        setUpAddInstructionButton();

        setUpCreateRecipeButton();

        return view;
    }

    private void setUpCreateRecipeButton() {
        createRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameView.getText().toString();
                String description = descriptionView.getText().toString();
                String mealType = mealTypeView.getText().toString();
                int prepTime = Integer.parseInt(prepTimeView.getText().toString());
                int cookTime = Integer.parseInt(cookTimeView.getText().toString());
                int numberOfServings = Integer.parseInt(servingNumberView.getText().toString());
                String author = authorView.getText().toString();
                List<Ingredient> ingredients = new ArrayList<>();
                List<Equipment> equipments = new ArrayList<>();
                List<Instruction> instructions = new ArrayList<>();

                for (int i = 0; i < ingredientList.getChildCount(); i++) {
                    View view = ingredientList.getChildAt(i);
                    if (view instanceof LinearLayout) {
                        AutoCompleteTextView nameView = (AutoCompleteTextView) view.findViewById(R.id.ingredient_name);
                        String ingredientName = nameView.getText().toString();

                        EditText amountView = (EditText) view.findViewById(R.id.ingredient_amount);
                        String amountString = amountView.getText().toString();
                        int ingredientAmount = amountString.isEmpty() ? 0 : Integer.parseInt(amountString);

                        EditText unitsView = (EditText) view.findViewById(R.id.ingredient_units);
                        String ingredientUnits = unitsView.getText().toString();

                        EditText descriptionView = (EditText) view.findViewById(R.id.ingredient_description);
                        String ingredientDescription = descriptionView.getText().toString();

                        Ingredient ingredient = new Ingredient(ingredientName, ingredientAmount, ingredientUnits, ingredientDescription);
                        ingredients.add(ingredient);
                    }
                }

                for (int e = 0; e < equipmentList.getChildCount(); e++) {
                    View view = equipmentList.getChildAt(e);
                    if (view instanceof LinearLayout) {
                        AutoCompleteTextView nameView = (AutoCompleteTextView) view.findViewById(R.id.search_item);
                        String equipmentName = nameView.getText().toString();

                        Equipment equipment = new Equipment(equipmentName);
                        equipments.add(equipment);
                    }
                }

                for (int i = 0; i < instructionList.getChildCount(); i++) {
                    View view = instructionList.getChildAt(i);
                    if (view instanceof LinearLayout) {
                        EditText nameView = (EditText) view.findViewById(R.id.new_instruction);
                        String instructionText = nameView.getText().toString();

                        Instruction instruction = new Instruction(i + 1, instructionText);
                        instructions.add(instruction);
                    }
                }


                NewRecipeBody body = new NewRecipeBody(ingredients, equipments, instructions);

                Call<Recipe> postCall = fetchingService.getService().postRecipe(name, description, mealType, prepTime, cookTime, numberOfServings, author, body);
                postCall.enqueue(new Callback<Recipe>() {
                    @Override
                    public void onResponse(Response response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            Intent recipeIntent = new Intent(getActivity(), RecipeActivity.class);
                            recipeIntent.putExtra("Recipe", (Recipe) response.body());
                            startActivity(recipeIntent);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
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

    private void setUpAddEquipmentButton() {
        addEquipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout newSearchTermLayout = (LinearLayout) inflater.inflate(R.layout.template_search_item, null);
                final AutoCompleteTextView view = (AutoCompleteTextView) newSearchTermLayout.findViewById(R.id.search_item);

                view.setAdapter(equipmentNamesAdapter);

                equipmentList.addView(newSearchTermLayout, 0);
                newSearchTermLayout.requestFocus();

                ImageButton removeButton = (ImageButton) newSearchTermLayout.findViewById(R.id.remove_search_item);
                removeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        equipmentList.removeView(newSearchTermLayout);
                    }
                });
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

    private void setUpAddInstructionButton() {
        addInstructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout newInstructionLayout = (LinearLayout) inflater.inflate(R.layout.template_new_instruction, null);

                instructionList.addView(newInstructionLayout);
                newInstructionLayout.requestFocus();

                ImageButton removeButton = (ImageButton) newInstructionLayout.findViewById(R.id.remove_item);
                removeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        instructionList.removeView(newInstructionLayout);
                    }
                });
            }
        });
    }

}
