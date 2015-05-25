package de.visi0nary.app2system.Fragments;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import de.visi0nary.app2system.CustomListAdapter;
import de.visi0nary.app2system.MainActivity;
import de.visi0nary.app2system.Model.App;
import de.visi0nary.app2system.R;

/**
 * Created by visi0nary on 03.05.15.
 * This fragment shows all installed user apps in a list view
 */
public class UserAppFragment extends AppFragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_applist, container, false);
        this.adapter = new CustomListAdapter(new ArrayList<App>());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recylerview);

        layoutManager = new LinearLayoutManager((getActivity()));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    public void update(ArrayList<App> apps) {
        //update list shown in this fragment
        this.adapter = new CustomListAdapter(apps);
        recyclerView.swapAdapter(adapter, false);
    }

    //this method implements the real functionality: if an app is clicked a pop up should appear
  //  @Override
  //  public void onListItemClick(ListView l, View v, int position, long id) {
  //      super.dialogFactory.create(1, getActivity(), id).show();

  //  }
}
