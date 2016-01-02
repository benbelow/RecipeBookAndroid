package com.example.ben.recipebook.services;

import com.amazonaws.util.StringUtils;
import com.example.ben.recipebook.models.recipe.Recipe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class S3ImageNamer {

    private static final String S3PREFIX = "recipe-images/recipe";
    private static final String FILEEXTENSION = ".jpg";


    public static String nameS3ImageForRecipe(Recipe recipe) {
        String namePrefix = StringUtils.join("-", S3PREFIX, ((Integer) recipe.Id).toString(), recipe.Name, getTimestamp());
        return namePrefix + FILEEXTENSION;
    }

    private static String getTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
