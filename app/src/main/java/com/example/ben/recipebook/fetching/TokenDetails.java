package com.example.ben.recipebook.fetching;

public class TokenDetails {

    public final String AccessToken;
    public final double AccessTokenExpiry;
    public final String RefreshToken;

    public TokenDetails(String accessToken, double accessTokenExpiry, String refreshToken) {
        AccessToken = accessToken;
        AccessTokenExpiry = accessTokenExpiry;
        RefreshToken = refreshToken;
    }
}
