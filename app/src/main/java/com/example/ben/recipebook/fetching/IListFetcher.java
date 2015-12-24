package com.example.ben.recipebook.fetching;

import java.util.List;

import retrofit.Call;

public interface IListFetcher <T> {

    Call<List<T>> fetchListCall();

}
