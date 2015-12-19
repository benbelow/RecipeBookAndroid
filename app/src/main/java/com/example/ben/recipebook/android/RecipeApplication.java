package com.example.ben.recipebook.android;

import android.app.Application;

public class RecipeApplication extends Application{

    private AppContextComponent appContextComponent;

    @Override
    public void onCreate(){
        super.onCreate();
        appContextComponent = DaggerAppContextComponent.builder().appContextModule(new AppContextModule(this)).build();
    }

    public AppContextComponent getAppContextComponent(){
        return appContextComponent;
    }

}
