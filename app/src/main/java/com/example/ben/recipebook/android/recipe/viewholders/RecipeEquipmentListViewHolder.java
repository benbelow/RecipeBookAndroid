package com.example.ben.recipebook.android.recipe.viewholders;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;
import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.Ingredient;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeEquipmentListViewHolder extends ViewHolder {

    private final LayoutInflater inflater;
    @Bind(R.id.recipe_equipment_list)
    LinearLayout equipmentList;

    public RecipeEquipmentListViewHolder(List<Equipment> equipment, LayoutInflater inflater) {
        super(inflater.inflate(R.layout.template_recipe_equipment_list, null));
        this.inflater = inflater;
        ButterKnife.bind(this, getView());
        updateContent(equipment);
    }

    @Override
    public void updateContent(Object item) {
        List<Equipment> equipments = (List<Equipment>) item;

        for(Equipment equipment : equipments){
            addEquipmentView(equipment);
        }
    }

    private void addEquipmentView(Equipment equipment) {
        View equipmentView = inflater.inflate(R.layout.template_recipe_equipment, equipmentList, false);
        RecipeEquipmentViewHolder viewHolder = new RecipeEquipmentViewHolder(equipment, equipmentView);
        viewHolder.updateContent(equipment);
        equipmentList.addView(equipmentView);
    }
}
