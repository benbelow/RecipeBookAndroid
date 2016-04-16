package com.example.ben.recipebook.android;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.fetching.DataFetchingService;
import com.example.ben.recipebook.models.Equipment;
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

public class StoreCupboardEquipmentFragment extends Fragment implements Saveable, ClickListener {

    private LayoutInflater inflater;

    private List<String> equipmentNames = new ArrayList<>();
    private ArrayAdapter equipmentNamesAdapter;

    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;

    @Inject
    DataFetchingService fetchingService;

    @Inject
    SharedPreferences sharedPreferences;

    StoreCupboardItemAdapter equipmentAdapter;

    @Bind(R.id.equipment)
    RecyclerView equipmentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RecipeApplication) getActivity().getApplication()).getApplicationComponent().inject(this);

        equipmentAdapter = new StoreCupboardItemAdapter(this);
        equipmentNamesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, equipmentNames);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_cupboard_equipment, container, false);
        ButterKnife.bind(this, view);

        equipmentList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        equipmentList.setAdapter(equipmentAdapter);

        fetchEquipmentList();

        final List<String> savedEquipment = new ArrayList<>();

        fetchingService.getService().storeCupboard().enqueue(new Callback<StoreCupboard>() {
            @Override
            public void onResponse(Response<StoreCupboard> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    StoreCupboard storeCupboard = response.body();
                    for (Equipment e: storeCupboard.equipments) {
                        savedEquipment.add(e.Name);
                    }

                    Collections.sort(savedEquipment, String.CASE_INSENSITIVE_ORDER);

                    equipmentAdapter.setItems(savedEquipment);
                    equipmentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        return view;
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

    public void showNewEquipmentDialog(){
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.setContentView(R.layout.dialog_new_store_cupboard_item);
        dialog.setTitle("New Equipment");
        Button setButton = (Button) dialog.findViewById(R.id.set);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel);
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) dialog.findViewById(R.id.new_item_name);
        autoCompleteTextView.setAdapter(equipmentNamesAdapter);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                equipmentAdapter.addItem(autoCompleteTextView.getText().toString());
                equipmentList.smoothScrollToPosition(equipmentAdapter.getItemCount() - 1);
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
        fetchingService.getService().postStoreCupboardEquipments(equipmentAdapter.getItems()).enqueue(new Callback<StoreCupboard>() {
            @Override
            public void onResponse(Response<StoreCupboard> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        if (actionMode != null) {
            toggleSelection(position);
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null) {
            actionMode = ((ActionBarActivity)this.getActivity()).startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);

        return true;
    }

    private void toggleSelection(int position) {
        equipmentAdapter.toggleSelection(position);
        int count = equipmentAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.selected_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    equipmentAdapter.removeItems(equipmentAdapter.getSelectedItems());
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            equipmentAdapter.clearSelection();
            actionMode = null;
        }
    }
}
