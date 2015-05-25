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

import de.visi0nary.app2system.Model.App;

/**
 * Created by visi0nary on 13.05.15.
 */
public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {

    private ArrayList<App> apps;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.label);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

    public CustomListAdapter(ArrayList<App> apps) {
        this.apps = apps;
    }

    public void refreshItems(ArrayList<App> apps) {
        this.apps = apps;
        notifyDataSetChanged();
    }

    public void addItem(int position, App item) {
        apps.add(position, item);
        notifyItemInserted(position);
    }

    public void removeItem(App item) {
        int position = apps.indexOf(item);
        apps.remove(item);
        notifyItemRemoved(position);
    }

    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.singleentrylayout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomListAdapter.ViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String name = apps.get(position).getHumanReadableName();
        final App app = apps.get(position);
        viewHolder.text.setText(name);
        viewHolder.icon.setImageDrawable((apps.get(position).getIcon()));
        viewHolder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(app);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    //TODO: implement adapter sorting logic
}
