package com.example.earthquake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthQuakeAdapter(@NonNull Context context, ArrayList<EarthQuake> resource) {
        super (context, 0, resource);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from (getContext ()).inflate (
                    R.layout.simple_list_item_1, parent, false);
        }

        EarthQuake earthQuake = getItem (position);

        TextView magnitude = (TextView) listItemView.findViewById (R.id.magnitude);
        double mag = earthQuake.getMagnitude ();
        magnitude.setText (getDecimal (mag));

       // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground ();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor (earthQuake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor (magnitudeColor);



        String primaryLocation;
        String locationOffset;
        String originalLocation = earthQuake.getPlace ();
        if (originalLocation.contains (LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split (LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            locationOffset = getContext ().getString (R.string.off_set);
            primaryLocation = originalLocation;
        }

        TextView place = (TextView) listItemView.findViewById (R.id.location_offset);
        place.setText (locationOffset);

        TextView primary = (TextView) listItemView.findViewById (R.id.primary_location);
        primary.setText (primaryLocation);

        TextView date = (TextView) listItemView.findViewById (R.id.date);
        date.setText (earthQuake.getDate ());

        TextView time = (TextView) listItemView.findViewById (R.id.time);
        time.setText (earthQuake.getTime ());


        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor (magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor (getContext (), magnitudeColorResourceId);
    }

    private static String getDecimal(double num) {
        DecimalFormat formatter = new DecimalFormat ("0.0");
        String output = formatter.format (num);
        return output;
    }

}