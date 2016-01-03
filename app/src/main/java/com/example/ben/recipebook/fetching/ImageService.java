package com.example.ben.recipebook.fetching;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.ben.recipebook.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class ImageService {

    private final Picasso picasso;
    private final Context context;

    private final String baseUrl = "http://recipes-assets.s3-eu-west-1.amazonaws.com/";

    @Inject
    public ImageService(Picasso picasso, Context context) {
        this.picasso = picasso;
        this.context = context;
    }

    public void loadImageIntoView(final ImageView view, String imageSource){
        loadImageIntoView(view, imageSource, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                view.setVisibility(View.GONE);
            }
        });
    }

    public void loadImageIntoView(ImageView view, String imageSource, Callback callback){
        String url = getUrl(imageSource);
        picasso.load(url)
                .fit()
                .placeholder(context.getResources().getDrawable(R.drawable.image_placeholder))
                .noFade()
                .centerCrop()
                .into(view, callback);
    }

    private String getUrl(String imageSource) {
        return baseUrl + imageSource;
    }

}