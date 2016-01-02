package com.example.ben.recipebook.fetching;

import java.util.List;

public class JsonPatchDocument {

    public final List<JsonPatchOperation> Operations;

    public JsonPatchDocument(List<JsonPatchOperation> operations) {
        Operations = operations;
    }

}
