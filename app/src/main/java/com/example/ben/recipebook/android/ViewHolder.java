package com.example.ben.recipebook.android;

import android.view.View;

public abstract class ViewHolder {

    private View view;

    protected ViewHolder(View view) {
        this.view = view;
        view.setTag(this);
    }

    public View getView(){
        return view;
    }

    public abstract void updateContent(Object item);
}
