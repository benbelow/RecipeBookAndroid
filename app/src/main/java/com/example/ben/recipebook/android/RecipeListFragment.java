package com.example.ben.recipebook.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.recipe.RecipeActivity;
import com.example.ben.recipebook.fetching.DataFetchingService;
import com.example.ben.recipebook.fetching.RecipeFetcher;
import com.example.ben.recipebook.fetching.RecipeSearchTerms;
import com.example.ben.recipebook.models.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RecipeListFragment extends Fragment implements AbsListView.OnItemClickListener {

    @Inject
    DataFetchingService fetchingService;

    private OnFragmentInteractionListener mListener;

    private AbsListView mListView;

    private ArrayAdapter mAdapter;

    //TODO: Better naming
    private List<Recipe> allRecipes = new ArrayList<>();

    private ArrayList<String> recipeNames = new ArrayList<>();

    public static RecipeListFragment newInstance() {
        RecipeListFragment fragment = new RecipeListFragment();
        return fragment;
    }

    public static RecipeListFragment newInstance(RecipeSearchTerms searchTerms) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("searchTerms", searchTerms);
        fragment.setArguments(bundle);
        return fragment;
    }

    public RecipeListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((RecipeApplication) getActivity().getApplication()).getApplicationComponent().inject(this);

        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, recipeNames);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);

        Bundle bundle = this.getArguments();

        RecipeSearchTerms searchTerms;

        if (bundle != null) {
            searchTerms = (RecipeSearchTerms) this.getArguments().getSerializable("searchTerms");
        } else {
            searchTerms = new RecipeSearchTerms();
        }

        RecipeFetcher fetcher = new RecipeFetcher(fetchingService, searchTerms);
        fetcher.fetchListCall().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Response<List<Recipe>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    allRecipes.addAll(response.body());

                    for (Recipe r : allRecipes) {
                        recipeNames.add(r.Name);
                    }

                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        // Set the adapter
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            Intent recipeIntent = new Intent(getActivity(), RecipeActivity.class);
            recipeIntent.putExtra("Recipe", allRecipes.get(position));
            startActivity(recipeIntent);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
