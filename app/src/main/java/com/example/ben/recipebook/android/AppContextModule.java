package com.example.ben.recipebook.android;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppContextModule {

    private final Context context;

    public AppContextModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return context;
    }

}

