package com.fcd.glasgowcycling;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.fcd.glasgowcycling.activities.SignInActivity;
import com.fcd.glasgowcycling.api.auth.CyclingAuthenticator;
import com.fcd.glasgowcycling.api.http.ApiClientModule;

import dagger.ObjectGraph;

/**
 * Created by chrissloey on 02/07/2014.
 */
public class CyclingApplication extends Application {
    private ObjectGraph graph;
    private final String TAG = "Cycling Application";

    @Override
    public void onCreate() {
        super.onCreate();

        graph = ObjectGraph.create(getModules());
    }

    private Object[] getModules() {
        return new Object[] { new ApiClientModule(getApplicationContext(), this) };
    }

    public void inject(Object target) {
        graph.inject(target);
    }

    public void logout() {
        Log.d(TAG, "Logging out");

        // Remote account
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType(CyclingAuthenticator.ACCOUNT_TYPE);
        if (accounts.length > 0) {
            Account account = accounts[0];
            accountManager.removeAccount(account, null, null);
        }

        // Start sign in activity
        Intent startSignIn = new Intent(this, SignInActivity.class);
        startSignIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(startSignIn);
    }
}
