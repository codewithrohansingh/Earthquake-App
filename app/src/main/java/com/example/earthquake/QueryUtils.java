package com.example.earthquake;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QueryUtils {
    /**
     * Sample JSON response for a USGS query
     */

//    private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static final String LOG_TAG = QueryUtils.class.getSimpleName ();

    /**
     * Method to change the time in milliseconds to formatted date
     */

    private static String dateFormatter(long time) {
        Date dateObject = new Date (time);
        SimpleDateFormat dateFormat = new SimpleDateFormat ("d MMM, yyyy");
        return dateFormat.format (dateObject);
    }

    /**
     * Method to change the time in milliseconds to formatted time
     */

    private static String timeFormatter(long time) {
        Date timeObject = new Date (time);
        SimpleDateFormat timeFormat = new SimpleDateFormat ("H:mm a");
        return timeFormat.format (timeObject);
    }

    /**
     * method to
     */


    public static List<EarthQuake> fetchEarthquakeData(String requestUrl) {

         //Making to Sleep the background thread for 2 seconds
        try {
            Thread.sleep (1000);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }

        // Create URL object
        URL url = createUrl (requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = " ";
        try {
            jsonResponse = makeHttpRequest (url);
        } catch (IOException e) {
            Log.e (LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<EarthQuake> earthquake = extractFeatureFromJson (jsonResponse);

        // Return the {@link Event}
        return earthquake;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL (stringUrl);
        } catch (MalformedURLException e) {
            Log.e (LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection ();
            urlConnection.setRequestMethod ("GET");
            urlConnection.setReadTimeout (10000 /* milliseconds */);
            urlConnection.setConnectTimeout (15000 /* milliseconds */);
            urlConnection.connect ();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode () == 200) {
                inputStream = urlConnection.getInputStream ();
                jsonResponse = readFromStream (inputStream);
            } else {
                Log.e (LOG_TAG, "Error response code: " + urlConnection.getResponseCode ());
            }
        } catch (IOException e) {
            Log.e (LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect ();
            }
            if (inputStream != null) {
                inputStream.close ();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder ();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader (inputStream, Charset.forName ("UTF-8"));
            BufferedReader reader = new BufferedReader (inputStreamReader);
            String line = reader.readLine ();
            while (line != null) {
                output.append (line);
                line = reader.readLine ();
            }
        }
        return output.toString ();
    }

    /**
     * Return an {@link EarthQuake} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private static List<EarthQuake> extractFeatureFromJson(String earthquakeJSON) {


        ArrayList<EarthQuake> earthquakes1 = new ArrayList<> ();
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty (earthquakeJSON)) {
            return null;
        }

        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject jsonObject = new JSONObject (earthquakeJSON);
            JSONArray jsonArray = jsonObject.getJSONArray ("features");

            for (int i = 0; i < jsonArray.length (); i++) {
                JSONObject firstElementOfArray = jsonArray.getJSONObject (i);
                JSONObject prop = firstElementOfArray.getJSONObject ("properties");
                double magnitude = prop.getDouble ("mag");
                String place = prop.getString ("place");
                long time = prop.getLong ("time");
                String link = prop.getString ("url");
                earthquakes1.add (new EarthQuake (magnitude, place, dateFormatter (time), timeFormatter (time), link));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e (LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes1;

    }

}