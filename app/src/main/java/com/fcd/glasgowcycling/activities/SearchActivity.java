package com.fcd.glasgowcycling.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;

import com.fcd.glasgowcycling.R;
import com.fcd.glasgowcycling.utils.LocationUtil;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class SearchActivity extends RouteListActivity {
    private final String TAG = "Search";
    private Geocoder mGeoCoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGeoCoder = new Geocoder(this);
        handleIntent(getIntent());
        setTitle("Search");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mLoadingMessage = "Searching for routes to " + query;
            mEmptyMessage = "No routes to " + query;

            LatLng userLocation = LocationUtil.getLastKnownLocation(getBaseContext());
            if (userLocation == null) {
                Toast.makeText(getBaseContext(),
                        "Could not locate you - finding any route to destination",
                        Toast.LENGTH_LONG)
                        .show();
            }

            try {
                List<Address> geoInfo = mGeoCoder.getFromLocationName(query, 1);
                Address location = geoInfo.get(0);
                if (location.hasLatitude() && location.hasLongitude()) {
                    Log.d(TAG, "Searching for routes to " + location.getLatitude() + ", " + location.getLongitude());
                    Bundle searchQuery = new Bundle();
                    if (userLocation != null) {
                        searchQuery.putFloat("source_lat", (float) userLocation.latitude);
                        searchQuery.putFloat("source_long", (float) userLocation.longitude);
                    }
                    searchQuery.putFloat("dest_lat", (float)location.getLatitude());
                    searchQuery.putFloat("dest_long", (float)location.getLongitude());
                    mSearcher.search(searchQuery);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void performSearch(Bundle query) {
        // Prevent on-load searches by route list
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}
