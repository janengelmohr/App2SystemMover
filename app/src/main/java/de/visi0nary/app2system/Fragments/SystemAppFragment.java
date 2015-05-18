package de.visi0nary.app2system.Fragments;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import de.visi0nary.app2system.CustomListAdapter;
import de.visi0nary.app2system.MainActivity;
import de.visi0nary.app2system.Model.ListProvider;
import de.visi0nary.app2system.R;

/**
 * Created by visi0nary on 03.05.15.
 */
public class SystemAppFragment extends AppFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_applist, container, false);

        //TODO: implement adapter sorting logic
        ListProvider provider = ListProvider.create(getActivity());
        // add apps to adapter
        final CustomListAdapter adapter = new CustomListAdapter(
                getActivity().getApplicationContext(),
                ((MainActivity)getActivity()).getSystemAppList(),
                ((MainActivity)getActivity()).getSystemAppNamesList());

        setListAdapter(adapter);

        return rootView;
    }

    //this method implements the real functionality: if an app is clicked a pop up should appear
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.dialogFactory.create(0, getActivity(), id).show();
    }



}
