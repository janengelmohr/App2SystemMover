package de.visi0nary.app2system;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by visi0nary on 13.05.15.
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private ArrayList<ApplicationInfo> apps;
    private ArrayList<String> appNames;

    public CustomListAdapter(Context context, ArrayList<ApplicationInfo> apps, ArrayList<String> appNames) {
        super(context, R.layout.singleentrylayout, appNames);
        this.context = context;
        this.apps = apps;
        this.appNames = appNames;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.singleentrylayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setTextColor(Color.BLACK);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(this.appNames.get(position));
        Drawable appIcon = context.getPackageManager().getApplicationIcon(apps.get(position));
        imageView.setImageDrawable(appIcon);

        return rowView;

    }
}
