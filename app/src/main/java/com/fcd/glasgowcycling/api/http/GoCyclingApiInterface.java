package com.fcd.glasgowcycling.api.http;

import com.fcd.glasgowcycling.api.AuthResult;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by chrissloey on 01/07/2014.
 */
public interface GoCyclingApiInterface {
    @GET("/signin.json")
    void signin(@Query("user[email") String email, @Query("user[password]") String password, Callback<AuthResult> callback);
}