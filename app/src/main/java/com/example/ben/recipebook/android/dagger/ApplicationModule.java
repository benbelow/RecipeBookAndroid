package com.example.ben.recipebook.android.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Context context;
    private final SharedPreferences sharedPreferences;

    public ApplicationModule(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
    }

    @Provides
    public Context provideApplicationContext() {
        return context;
    }

    @Provides
    public LayoutInflater provideLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Provides
    public SharedPreferences provideSharedPreferences() {
        return sharedPreferences;
    }

}

