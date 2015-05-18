package de.visi0nary.app2system.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import de.visi0nary.app2system.CustomListAdapter;
import de.visi0nary.app2system.MainActivity;
import de.visi0nary.app2system.R;

/**
 * Created by visi0nary on 03.05.15.
 */
public class UserAppFragment extends AppFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_applist, container, false);
        //TODO: implement adapter sorting logic
        // add apps to adapter
        final CustomListAdapter adapter = new CustomListAdapter(
                getActivity().getApplicationContext(),
                ((MainActivity)getActivity()).getUserAppList(),
                ((MainActivity)getActivity()).getUserAppNamesList());
        setListAdapter(adapter);

        return rootView;
    }

    //this method implements the real functionality: if an app is clicked a pop up should appear
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.dialogFactory.create(1, getActivity(), id).show();

    }
}
