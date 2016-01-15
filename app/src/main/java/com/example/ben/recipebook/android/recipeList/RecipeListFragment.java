package com.example.ben.recipebook.android.recipeList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.RecipeApplication;
import com.example.ben.recipebook.fetching.DataFetchingService;
import com.example.ben.recipebook.fetching.IListFetcher;
import com.example.ben.recipebook.fetching.ImageService;
import com.example.ben.recipebook.fetching.RecipeFetcher;
import com.example.ben.recipebook.fetching.RecipeSearchTerms;
import com.example.ben.recipebook.models.recipe.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RecipeListFragment extends Fragment implements AbsListView.OnItemClickListener {

    @Inject
    DataFetchingService fetchingService;

    @Inject
    ImageService imageService;

    @Bind(R.id.loading_bar_view)
    LinearLayout loadingBarView;

    @Bind(R.id.no_results)
    LinearLayout noResults;

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.recipe_list)
    RecyclerView recipeListView;

    @Inject
    RecipeCardAdapter mAdapter;

    //TODO: Better naming
    private List<Recipe> allRecipes = new ArrayList<>();

    private ArrayList<String> recipeNames = new ArrayList<>();

    public static RecipeListFragment newInstance() {
        RecipeListFragment fragment = new RecipeListFragment();
        return fragment;
    }

    public static RecipeListFragment newInstance(Serializable searchTerms) {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);


        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recipeListView.setLayoutManager(layoutManager);

        loadingBarView.setVisibility(View.VISIBLE);

        Bundle bundle = this.getArguments();

        Serializable searchTerms;

        if (bundle != null) {
            searchTerms = this.getArguments().getSerializable("searchTerms");
        } else {
            searchTerms = new RecipeSearchTerms();
        }

        IListFetcher<Recipe> fetcher;

        if (searchTerms instanceof RecipeSearchTerms) {
            fetcher = new RecipeFetcher(fetchingService, (RecipeSearchTerms) searchTerms);
        } else {
            fetcher = new RecipeFetcher(fetchingService, new RecipeSearchTerms());
        }

        fetcher.fetchListCall().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Response<List<Recipe>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    allRecipes.addAll(response.body());

                    for (Recipe r : allRecipes) {
                        recipeNames.add(r.Name);
                    }

                    mAdapter.notifyDataSetChanged();
                    mAdapter.setRecipes(allRecipes);
                }

                loadingBarView.setVisibility(View.GONE);

                if (allRecipes.isEmpty()) {
                    noResults.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        // Set the adapter
        recipeListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        // Set OnItemClickListener so we can be notified on item clicks
//        recipeListView.setOnItemClickListener(this);
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
//            Intent recipeIntent = new Intent(getActivity(), RecipeActivity.class);
//            recipeIntent.putExtra("Recipe", allRecipes.get(position));
//            startActivity(recipeIntent);
//            Intent recipeIntent = new Intent(getActivity(), SignInActivity.class);
//            startActivity(recipeIntent);
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
