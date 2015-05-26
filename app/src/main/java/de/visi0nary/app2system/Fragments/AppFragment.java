package de.visi0nary.app2system.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;

import de.visi0nary.app2system.MainActivity;
import de.visi0nary.app2system.Model.App;
import de.visi0nary.app2system.R;

/**
 * Created by visi0nary on 04.05.15.
 * This class holds all methods that both SystemAppFragment and UserAppFragment use
 */
public class AppFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_applist, container, false);
        return rootView;
    }



}
