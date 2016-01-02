package com.example.ben.recipebook.fetching;

public class JsonPatchOperation {

    public final String Operation;
    public final String PropertyName;
    public final String Value;

    public JsonPatchOperation(String op, String path, String value){
        this.Operation = op;
        this.PropertyName = path;
        this.Value = value;
    }

}