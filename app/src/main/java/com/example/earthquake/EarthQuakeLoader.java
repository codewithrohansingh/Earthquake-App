package com.example.earthquake;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = EarthQuakeLoader.class.getName ();

    /**
     * Query URL
     */
    private String mUrl;

    public EarthQuakeLoader(@NonNull Context context, String url) {
        super (context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
//        Log.e ("MainActivity.this", " onStartLoading method");

        forceLoad ();
    }

    @Override
    public List<EarthQuake> loadInBackground() {
//        Log.e ("MainActivity.this", " loadInBackground method");

        if (mUrl == null) {
            return null;
        }
        // Perform the HTTP request for earthquake data and process the response.
        List<EarthQuake> earthQuake = QueryUtils.fetchEarthquakeData (mUrl);
        return earthQuake;
    }
}
