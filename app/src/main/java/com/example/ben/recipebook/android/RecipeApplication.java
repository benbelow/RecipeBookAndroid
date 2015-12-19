package com.example.ben.recipebook.android;

import android.app.Application;

import com.example.ben.recipebook.android.dagger.ApplicationComponent;
import com.example.ben.recipebook.android.dagger.ApplicationModule;
import com.example.ben.recipebook.android.dagger.DaggerApplicationComponent;

public class RecipeApplication extends Application{

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate(){
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }

}
