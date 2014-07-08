package com.fcd.glasgowcycling.api;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.fcd.glasgowcycling.api.auth.CyclingAuthenticator;
import com.fcd.glasgowcycling.api.http.GoCyclingApiInterface;
import com.google.gson.annotations.Expose;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by chrissloey on 01/07/2014.
 */
public class AuthModel {

    private final String TAG = "AuthModel";

    @Expose
    private String accessToken;

    @Expose
    private String refreshToken;

    public AuthModel(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accountList = accountManager.getAccountsByType(CyclingAuthenticator.ACCOUNT_TYPE);
        Account userAccount = accountList[0];
        String authToken = accountManager.peekAuthToken(userAccount, AccountManager.KEY_AUTHTOKEN);
        String refreshToken = accountManager.peekAuthToken(userAccount, CyclingAuthenticator.KEY_REFRESH_TOKEN);
        setUserToken(authToken);
        setRefreshToken(refreshToken);
        Log.d(TAG, "User token initialized to " + getUserToken());
        Log.d(TAG, "Refresh token initialized to " + getRefreshToken());
    }

    public String getUserToken() {
        return accessToken;
    }

    public void setUserToken(String userToken) {
        this.accessToken = userToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}