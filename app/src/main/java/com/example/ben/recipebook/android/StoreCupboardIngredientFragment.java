package com.example.ben.recipebook.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
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

    @Bind(R.id.add_search_ingredient)
    ImageButton addSearchIngredientButton;

    @Bind(R.id.recipe_search_ingredient_list)
    LinearLayout ingredientList;

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

        fetchIngredientList();
        setUpAddSearchTermButton(addSearchIngredientButton, ingredientList, ingredientNamesAdapter);

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
                        addViewsForSavedSearchTerms(savedIngredients, ingredientList, ingredientNamesAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        return view;
    }

    private void addViewsForSavedSearchTerms(List<String> savedStrings, final LinearLayout layout, ArrayAdapter adapter) {
        for (String s : savedStrings) {
            final LinearLayout newSearchTermLayout = (LinearLayout) this.inflater.inflate(R.layout.template_search_item, null);
            final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) newSearchTermLayout.findViewById(R.id.search_item);

            autoCompleteTextView.setAdapter(adapter);

            layout.addView(newSearchTermLayout, 0);

            autoCompleteTextView.setText(s);

            ImageButton removeButton = (ImageButton) newSearchTermLayout.findViewById(R.id.remove_search_item);
            removeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    layout.removeView(newSearchTermLayout);
                }
            });
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

    @Override
    public void save() {
        List<String> ingredientNames = new ArrayList<>();
        for (int i = 0; i < ingredientList.getChildCount(); i++) {
            View view = ingredientList.getChildAt(i);
            if (view instanceof LinearLayout) {
                AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.search_item);
                String ingredientName = textView.getText().toString();
                ingredientNames.add(ingredientName);
            }
        }

        fetchingService.getService().postStoreCupboardIngredients(ingredientNames).enqueue(new Callback<StoreCupboard>() {
            @Override
            public void onResponse(Response<StoreCupboard> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
