package com.example.ben.recipebook.android.recipe.viewholders;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ViewHolder;
import com.example.ben.recipebook.models.Equipment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeEquipmentViewHolder extends ViewHolder {

    @Bind(R.id.recipe_equipment_name)
    TextView equipmentView;

    public RecipeEquipmentViewHolder(Equipment equipment, LayoutInflater inflater){
        super(inflater.inflate(R.layout.template_recipe_equipment, null));
        ButterKnife.bind(this, getView());
        updateContent(equipment);
    }

    @Override
    public void updateContent(Object item) {
        Equipment equipment = (Equipment) item;
        equipmentView.setText(equipment.Name);
    }
}
