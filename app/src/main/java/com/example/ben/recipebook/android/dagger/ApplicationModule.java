package com.example.ben.recipebook.android.dagger;

import android.content.Context;
import android.view.LayoutInflater;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Context context;

    public ApplicationModule(Context context){
        this.context = context;
    }

    @Provides
    public Context provideApplicationContext() {
        return context;
    }

    @Provides
    public LayoutInflater provideLayoutInflater(){
        return LayoutInflater.from(context);
    }


}

