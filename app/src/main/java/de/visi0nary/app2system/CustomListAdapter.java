package de.visi0nary.app2system;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
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
public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {

    private ArrayList<ApplicationInfo> apps;
    private ArrayList<String> appNames;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.label);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

    public CustomListAdapter(Context context, ArrayList<ApplicationInfo> apps, ArrayList<String> appNames) {
        this.apps = apps;
        this.appNames = appNames;
        this.context = context;
    }

    public void refreshItems(ArrayList<ApplicationInfo> apps, ArrayList<String> names) {
        this.apps = apps;
        this.appNames = names;
        notifyDataSetChanged();
    }

    public void addItem(int position, ApplicationInfo item, String appName) {
        apps.add(position, item);
        appNames.add(position, appName);
        notifyItemInserted(position);
    }

    public void removeItem(ApplicationInfo item, String appName) {
        int position = appNames.indexOf(appName);
        apps.remove(item);
        appNames.remove(appName);
        notifyItemRemoved(position);
    }

    /*@Override
    public View getView(int position, View view, ViewGroup parent) {
        //inflates the list while scrolling
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.singleentrylayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setTextColor(Color.BLACK);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(this.appNames.get(position));
        Drawable appIcon = context.getPackageManager().getApplicationIcon(apps.get(position));
        imageView.setImageDrawable(appIcon);

        return rowView;
        //TODO; fix performance issues while scrolling
    }*/

    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.singleentrylayout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomListAdapter.ViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = appNames.get(position);
        final ApplicationInfo app = apps.get(position);
        viewHolder.text.setText(appNames.get(position));
        viewHolder.icon.setImageDrawable(context.getPackageManager().getApplicationIcon(apps.get(position)));
        viewHolder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(app, name);
            }
        });

        viewHolder.text.setText(appNames.get(position));
    }

    @Override
    public int getItemCount() {
        return appNames.size();
    }

    //TODO: implement adapter sorting logic
}
