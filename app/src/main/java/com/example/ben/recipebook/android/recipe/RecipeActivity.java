package com.example.ben.recipebook.android.recipe;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.MainActivity;
import com.example.ben.recipebook.android.RecipeApplication;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeEquipmentViewHolder;
import com.example.ben.recipebook.android.recipe.viewholders.RecipeInstructionViewHolder;
import com.example.ben.recipebook.fetching.DataFetchingService;
import com.example.ben.recipebook.fetching.ImageService;
import com.example.ben.recipebook.fetching.ImageUploadTask;
import com.example.ben.recipebook.fetching.JsonPatchDocument;
import com.example.ben.recipebook.fetching.JsonPatchOperation;
import com.example.ben.recipebook.models.Equipment;
import com.example.ben.recipebook.models.recipe.Instruction;
import com.example.ben.recipebook.models.recipe.Recipe;
import com.example.ben.recipebook.services.S3ImageNamer;
import com.example.ben.recipebook.services.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class RecipeActivity extends ActionBarActivity {

    //ToDo: CONFIG!
    private static final String BUCKET_NAME = "recipes-assets";

    Recipe recipe;

    @Inject
    DataFetchingService dataFetchingService;

    @Inject
    ImageService imageService;

    @Inject
    LayoutInflater inflater;

    @Inject
    RecipeIngredientAdapter ingredientAdapter;

    @Bind(R.id.recipe_author)
    TextView authorView;

    @Bind(R.id.recipe_equipment_list)
    LinearLayout equipmentList;

    @Bind(R.id.recipe_image)
    ImageView image;

    @Bind(R.id.recipe_ingredients_list)
    LinearLayout ingredientList;

    @Bind(R.id.recipe_instruction_list)
    LinearLayout instructionList;

    @Bind(R.id.recipe_mealtype)
    TextView mealTypeView;

    @Bind(R.id.recipe_name_text)
    TextView nameView;

    @Bind(R.id.recipe_serving_number)
    TextView servingsView;

    @Bind(R.id.recipe_total_time)
    TextView totalTimeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        ((RecipeApplication) getApplication()).getApplicationComponent().inject(this);

        Recipe recipe = (Recipe) getIntent().getExtras().getSerializable("Recipe");
        setRecipe(recipe);
    }

    private void setRecipe(Recipe recipe) {
        this.recipe = recipe;

        if (recipe.ImageSource != null && !recipe.ImageSource.isEmpty()) {
            imageService.loadImageIntoView(image, recipe.ImageSource);
        } else {
            image.setVisibility(View.GONE);
        }
        nameView.setText(recipe.Name);
        authorView.setText("By " + recipe.Author);
        servingsView.setText(Integer.toString(recipe.NumberOfServings));

        int totalMinutes = recipe.PreparationTime + recipe.CookTime;
        totalTimeView.setText(TimeFormatter.format(totalMinutes));

        ingredientAdapter.setIngredients(recipe.Ingredients);

        for (int i = 0; i < ingredientAdapter.getCount(); i++) {
            ingredientList.addView(ingredientAdapter.getView(i, null, ingredientList));
        }


        for (Equipment equipment : recipe.Equipment) {
            addEquipmentView(equipment);
        }

        for (Instruction instruction : recipe.Instructions) {
            addInstructionView(instruction);
        }

        this.setTitle(recipe.Name);
    }

    //ToDo: make adapters for these things
    private void addEquipmentView(Equipment equipment) {
        View equipmentView = inflater.inflate(R.layout.template_recipe_equipment, equipmentList, false);
        RecipeEquipmentViewHolder viewHolder = new RecipeEquipmentViewHolder(equipment, equipmentView);
        viewHolder.updateContent(equipment);
        equipmentList.addView(equipmentView);
    }

    private void addInstructionView(Instruction instruction) {
        View instructionView = inflater.inflate(R.layout.template_recipe_instruction, instructionList, false);
        RecipeInstructionViewHolder viewHolder = new RecipeInstructionViewHolder(instruction, instructionView);
        viewHolder.updateContent(instruction);
        instructionList.addView(instructionView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_upload_image) {
            uploadImage();
        }

        if (id == R.id.action_delete_recipe) {
            Call<Void> call = dataFetchingService.getService().deleteRecipe(recipe.Id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Response<Void> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.recipe_scale_servings)
    public void scaleServings() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_number_picker);
        dialog.setTitle("Servings");
        Button setButton = (Button) dialog.findViewById(R.id.set);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel);
        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(30);
        numberPicker.setMinValue(1);
        numberPicker.setValue(Integer.parseInt(servingsView.getText().toString()));
        numberPicker.setWrapSelectorWheel(false);

        //ToDo: Make adapter with reference to ingredient models, rather than calculation based on strings
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newServings = numberPicker.getValue();
                servingsView.setText(String.valueOf(newServings));
                float scaleFactor = ((float) newServings) / recipe.NumberOfServings;
                ingredientAdapter.scaleIngredientAmounts(scaleFactor);
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

    public void uploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String filePath = getPath(data.getData());

            String s3Key = S3ImageNamer.nameS3ImageForRecipe(recipe);

            uploadImageToS3(filePath, s3Key);
            updateRecipeImageSource(s3Key);
        }
    }

    private void updateRecipeImageSource(String newSource) {
        JsonPatchOperation changeSourceOperation = new JsonPatchOperation("replace", "ImageSource", newSource);
        List<JsonPatchOperation> operations = new ArrayList<>();
        operations.add(changeSourceOperation);
        JsonPatchDocument patchData = new JsonPatchDocument(operations);
        Call<Recipe> patchCall = dataFetchingService.getService().patchRecipe(recipe.Id, patchData);

        patchCall.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Response<Recipe> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void uploadImageToS3(String filePath, String s3Key) {
        PutObjectRequest por = new PutObjectRequest(BUCKET_NAME, s3Key, new java.io.File(filePath));
        new ImageUploadTask(this).execute(por);
    }

    private String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();

        return filePath;
    }

}
