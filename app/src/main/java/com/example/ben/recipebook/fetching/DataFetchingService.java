package com.example.ben.recipebook.fetching;

import android.content.SharedPreferences;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import javax.inject.Inject;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


public class DataFetchingService {

    //ToDo: Make a config file
//    private String baseUrl = "http://52.35.36.23/";
    private String baseUrl = "http://8cde5ee3.ngrok.io/";

    private SharedPreferences sharedPreferences;

    private String accessTokenKey = "com.example.app.apiAccessToken";
    private String refreshTokenKey = "com.example.app.apiRefreshToken";

    @Inject
    public DataFetchingService(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setAuthenticator(new TokenAuthenticator());
        httpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", getStoredAccessToken()).build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RecipeService.class);
    }

    private String getStoredAccessToken() {
        return sharedPreferences.getString(accessTokenKey, "");
    }

    private String getStoredRefreshToken() {
        return sharedPreferences.getString(refreshTokenKey, "B3sMWO24VUKknJiMidRpfhuvEr2erlBnl6jMccbFoEk=");
    }

    public RecipeService getService() {
        return service;
    }

    private RecipeService service;

    public class TokenAuthenticator implements Authenticator {
        @Override
        public Request authenticate(Proxy proxy, Response response) throws IOException {
            retrofit.Response refreshResponse = service.refreshToken(new RefreshToken(getStoredRefreshToken())).execute();
            if (refreshResponse.isSuccess()) {
                String accessToken = ((TokenDetails) refreshResponse.body()).AccessToken;
                sharedPreferences.edit().putString(accessTokenKey, accessToken).apply();
            }

            return response.request().newBuilder()
                    .header("Authorization", getStoredAccessToken())
                    .build();
        }

        @Override
        public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
            // Null indicates no attempt to authenticate.
            return null;
        }
    }
}



