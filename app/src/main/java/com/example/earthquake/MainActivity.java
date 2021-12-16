package com.example.earthquake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final int GONE = 2;
    /**
     * Adapter for the list of earthquakes
     */
    private EarthQuakeAdapter mAdapter;
    private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        // Get a reference to the ConnectivityManager to check state of network connectivity
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        // Get details on the currently active default data network
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//
//        // If there is a network connection, fetch data
//        if (networkInfo != null && networkInfo.isConnected()) {
//
//
//        }
//
//        else if(networkInfo == null ){
//            mEmptyStateTextView.setText (R.string.no_internet);
//        }
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById (R.id.list);

        //Setting the empty view to the List view
        mEmptyStateTextView = (TextView) findViewById (R.id.empty_view);
        earthquakeListView.setEmptyView (mEmptyStateTextView);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthQuakeAdapter (this, new ArrayList<EarthQuake> ());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter (mAdapter);

        earthquakeListView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                EarthQuake currentEarthquake = mAdapter.getItem (i);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse (currentEarthquake.getLink ());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent (Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity (websiteIntent);
            }
        });
        LoaderManager.getInstance (this).initLoader (EARTHQUAKE_LOADER_ID, null, this).forceLoad ();

    }

    @NonNull
    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int id, @Nullable Bundle args) {
//        Log.e ("MainActivity.this", " onCreateLoader method");
        return new EarthQuakeLoader (this, SAMPLE_JSON_RESPONSE);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthQuake>> loader, List<EarthQuake> data) {

        ProgressBar progressBar = (ProgressBar) findViewById (R.id.loading_spinner);
        progressBar.setVisibility (View.GONE);
        //check for internet connection
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo ();
        if (networkInfo == null) {
            //state that there is no internet connection
            mEmptyStateTextView.setText (R.string.no_internet);
        } else if (networkInfo != null && networkInfo.isConnected ()) {
            //There is internet but list is still empty
            mEmptyStateTextView.setText (R.string.no_earthquakes);
        }

        // Clear the adapter of previous earthquake data
        mAdapter.clear ();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty ()) {
            mAdapter.addAll (data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<EarthQuake>> loader) {
//        Log.e ("MainActivity.this", " onLoaderReset method");

        mAdapter.clear ();
    }
}

