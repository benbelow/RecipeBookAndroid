package com.example.ben.recipebook.fetching;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class ImageService {

    private final Picasso picasso;

    private final String baseUrl = "http://recipes-assets.s3-eu-west-1.amazonaws.com/";

    @Inject
    public ImageService(Picasso picasso) {
        this.picasso = picasso;
    }

    public void loadImageIntoView(ImageView view, String imageSource){
        String url = getUrl(imageSource);
        picasso.load(url)
                .fit()
                .centerCrop()
                .into(view);
    }

    private String getUrl(String imageSource) {
        return baseUrl + imageSource;
    }

}