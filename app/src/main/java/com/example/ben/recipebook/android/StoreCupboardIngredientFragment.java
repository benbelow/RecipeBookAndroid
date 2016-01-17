package com.example.ben.recipebook.android;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.fetching.DataFetchingService;
import com.example.ben.recipebook.models.Ingredient;
import com.example.ben.recipebook.models.StoreCupboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class StoreCupboardIngredientFragment extends Fragment implements Saveable {

    private LayoutInflater inflater;

    private List<String> ingredientNames = new ArrayList<>();
    private ArrayAdapter ingredientNamesAdapter;

    @Inject
    DataFetchingService fetchingService;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    StoreCupboardItemAdapter ingredientAdapter;

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

    public void showNewIngredientDialog(){
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.setContentView(R.layout.dialog_new_store_cupboard_item);
        dialog.setTitle("New Ingredient");
        Button setButton = (Button) dialog.findViewById(R.id.set);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel);
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) dialog.findViewById(R.id.new_item_name);
        autoCompleteTextView.setAdapter(ingredientNamesAdapter);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientAdapter.addItem(autoCompleteTextView.getText().toString());
                ingredientList.smoothScrollToPosition(ingredientAdapter.getItemCount() - 1);
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
