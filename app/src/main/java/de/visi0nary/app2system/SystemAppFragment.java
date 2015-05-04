package de.visi0nary.app2system;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by visi0nary on 03.05.15.
 */
public class SystemAppFragment extends ListFragment {

    private ArrayList<ApplicationInfo> systemAppList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_systemapp, container, false);

        //TODO: implement adapter sorting logic
        final ArrayList<String> systemAppNamesList = new ArrayList<String>();
        systemAppList = new ArrayList<ApplicationInfo>();
        // get all apps
        final PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        //iterate through all apps and decide whether they're system or user apps and put them into the corresponding list
        for(ApplicationInfo appInfo : packages) {
            // use human readable app name instead of package name
            if(checkIfAppIsSystemApp(appInfo))
                systemAppNamesList.add(pm.getApplicationLabel(appInfo).toString());
                systemAppList.add(appInfo);
        }
        // add apps to adapter
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, systemAppNamesList);
        setListAdapter(adapter);

        return rootView;
    }

    //this method implements the real functionality: if an app is clicked a pop up should appear
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final long appId = id;
        //open dialog pop up
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(R.string.txt_alertdialog_move_to_data);
        alertDialogBuilder.setTitle(R.string.txt_alertdialog_headline);
        alertDialogBuilder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //user clicked ok
                final Long temp = new Long(appId);
                moveApp(systemAppList.get(temp.intValue()));
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //user canceled action
            }
        });
        alertDialogBuilder.create().show();

    }

    private void moveApp(ApplicationInfo appInfo) {

    }

    private boolean checkIfAppIsSystemApp(ApplicationInfo appInfo) {
        // if app is system app return true, else false
        return ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
