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
import com.example.ben.recipebook.models.Equipment;
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

public class StoreCupboardEquipmentFragment extends Fragment {

    private LayoutInflater inflater;

    private List<String> equipmentNames = new ArrayList<>();
    private ArrayAdapter equipmentNamesAdapter;

    @Inject
    DataFetchingService fetchingService;

    @Inject
    SharedPreferences sharedPreferences;

    @Bind(R.id.add_search_equipment)
    ImageButton addSearchEquipmentButton;

    @Bind(R.id.recipe_search_equipment_list)
    LinearLayout equipmentList;

    public static StoreCupboardEquipmentFragment newInstance() {
        StoreCupboardEquipmentFragment fragment = new StoreCupboardEquipmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RecipeApplication) getActivity().getApplication()).getApplicationComponent().inject(this);

        equipmentNamesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, equipmentNames);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_cupboard_equipment, container, false);
        ButterKnife.bind(this, view);
        this.inflater = inflater;

        fetchEquipmentList();
        setUpAddSearchTermButton(addSearchEquipmentButton, equipmentList, equipmentNamesAdapter);

        final List<String> savedEquipment = new ArrayList<>();

        fetchingService.getService().storeCupboard().enqueue(new Callback<StoreCupboard>() {
            @Override
            public void onResponse(Response<StoreCupboard> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    StoreCupboard storeCupboard = response.body();
                    for (Equipment e : storeCupboard.equipments) {
                        savedEquipment.add(e.Name);
                    }

                    Collections.sort(savedEquipment, String.CASE_INSENSITIVE_ORDER);
                    Collections.reverse(savedEquipment);

                    if (!savedEquipment.isEmpty()) {
                        addViewsForSavedSearchTerms(savedEquipment, equipmentList, equipmentNamesAdapter);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        List<String> equipmentNames = new ArrayList<>();
        for (int i = 0; i < equipmentList.getChildCount(); i++) {
            View view = equipmentList.getChildAt(i);
            if (view instanceof LinearLayout) {
                AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.search_item);
                String equipmentName = textView.getText().toString();
                equipmentNames.add(equipmentName);
            }
        }

        fetchingService.getService().postStoreCupboardEquipments(equipmentNames).enqueue(new Callback<StoreCupboard>() {
            @Override
            public void onResponse(Response<StoreCupboard> response, Retrofit retrofit) {

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
