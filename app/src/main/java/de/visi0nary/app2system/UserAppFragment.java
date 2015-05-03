package de.visi0nary.app2system;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by visi0nary on 03.05.15.
 */
public class UserAppFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_systemapp, container, false);

        //TODO: implement adapter sorting logic
        final ArrayList<String> userAppList = new ArrayList<String>();
        // get all apps
        final PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        //iterate through all apps and decide whether they're system or user apps and put them into the corresponding list
        for(ApplicationInfo appInfo : packages) {
            // use human readable app name instead of package name
            if(!checkIfAppIsSystemApp(appInfo))
                userAppList.add(pm.getApplicationLabel(appInfo).toString());
        }
        // add apps to adapter
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, userAppList);
        setListAdapter(adapter);

        return rootView;
    }

    private boolean checkIfAppIsSystemApp(ApplicationInfo appInfo) {
        // if app is system app return true, else false
        return ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
