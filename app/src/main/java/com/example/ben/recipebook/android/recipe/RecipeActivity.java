package com.example.ben.recipebook.android.recipe;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.ben.recipebook.R;
import com.example.ben.recipebook.android.ImageUploadTask;
import com.example.ben.recipebook.android.RecipeApplication;
import com.example.ben.recipebook.fetching.DataFetchingService;
import com.example.ben.recipebook.fetching.JsonPatchDocument;
import com.example.ben.recipebook.fetching.JsonPatchOperation;
import com.example.ben.recipebook.models.recipe.Recipe;
import com.squareup.picasso.Picasso;

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
    private static final Object S3PREFIX = "recipe-images/";

    Recipe recipe;

    @Inject
    RecipeAdapter recipeAdapter;

    @Inject
    DataFetchingService dataFetchingService;

    @Bind(R.id.recipe_items)
    ListView recipeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        ((RecipeApplication) getApplication()).getApplicationComponent().inject(this);

        recipe = (Recipe) getIntent().getExtras().getSerializable("Recipe");

        this.setTitle(recipe.Name);

        recipeAdapter.setRecipe(recipe);

        recipeItems.setAdapter(recipeAdapter);
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

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.recipe_upload_image)
    public void upload_image() {
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
            String s3Key = S3PREFIX + "recipe-" + recipe.Id + "-" + recipe.Name.trim() + ".jpg";
            PutObjectRequest por = new PutObjectRequest(BUCKET_NAME, s3Key, new java.io.File(filePath));
            new ImageUploadTask().execute(por);

            JsonPatchOperation changeSourceOperation = new JsonPatchOperation("replace", "ImageSource", s3Key);
            List<JsonPatchOperation> operations = new ArrayList<>();
            operations.add(changeSourceOperation);
            JsonPatchDocument patchData = new JsonPatchDocument(operations);
            Call<Recipe> patchCall = dataFetchingService.getService().patchRecipe(recipe.Id, patchData);

            patchCall.enqueue(new Callback<Recipe>() {
                @Override
                public void onResponse(Response<Recipe> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        Recipe newRecipe = (Recipe) response.body();
                        recipeAdapter.setRecipe(newRecipe);
                        recipeAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
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

    private Bitmap generateBitmap(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }
}
