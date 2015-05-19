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
 * This fragment shows all installed user apps in a list view
 */
public class UserAppFragment extends AppFragment {

    private CustomListAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_applist, container, false);
        return rootView;
    }

    public void update() {
        //update list shown in this fragment
            this.adapter = new CustomListAdapter(
                    getActivity().getApplicationContext(),
                    ((MainActivity) getActivity()).getDataProvider().getUserAppList(),
                    ((MainActivity) getActivity()).getDataProvider().getUserAppNamesList());
            setListAdapter(adapter);
            this.adapter.notifyDataSetChanged();
    }

    //this method implements the real functionality: if an app is clicked a pop up should appear
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.dialogFactory.create(1, getActivity(), id).show();

    }
}
