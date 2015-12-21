package com.example.ben.recipebook.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.models.recipe.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeSearchResultsFragment extends Fragment {

    private ListView listView;

    private ArrayAdapter adapter;

    private ArrayList<String> recipeNames = new ArrayList<>();

    public static RecipeSearchResultsFragment newInstance(List<Recipe> recipes){
        RecipeSearchResultsFragment fragment = new RecipeSearchResultsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipes", (Serializable) recipes);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, recipeNames);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list_list, container, false);

        List<Recipe> recipes = (List<Recipe>) this.getArguments().getSerializable("recipes");
        for(Recipe r : recipes){
            recipeNames.add(r.Name);
        }

        // Set the adapter
        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        return view;
    }

}
