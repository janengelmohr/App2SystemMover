package de.visi0nary.app2system;

import android.app.Activity;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import java.util.ArrayList;
import java.util.Collections;

import de.visi0nary.app2system.Model.App;

/**
 * Created by visi0nary on 19.05.15.
 * This class contains all data models and keeps them consistent
 */
public class AppDataService extends IntentService {

    protected ArrayList<App> systemAppList;
    protected ArrayList<App> userAppList;
    protected boolean listsPopulated = false;

    protected static final String ACTION_SAVE_APPS = "de.visi0nary.app2system.action.SAVE";
    protected static final String ACTION_RESTORE_APPS = "de.visi0nary.app2system.action.RESTORE";
    protected static final String ACTION_REFRESH_APPS = "de.visi0nary.app2system.action.REFRESH_APPLIST";
    private ProgressDialog dialog;
    private Handler updateDialogHandler;
    private int appCount = 0;

    public AppDataService() {
        super("AppDataService");
    }

    public ArrayList<App> getSystemAppList() {
        return systemAppList;
    }

    public ArrayList<App> getUserAppList() {
        return userAppList;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SAVE_APPS.equals(action)) {
                handleSaveAppLayout();
            } else if (ACTION_RESTORE_APPS.equals(action)) {
                handleRestoreAppLayout();
            }
            else if (ACTION_REFRESH_APPS.equals(action)) {
                handleRefreshAppList(intent);
            }
        }
    }

    private void handleSaveAppLayout() {
        PackageManager pm = getApplicationContext().getPackageManager();
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleRestoreAppLayout() {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private void handleRefreshAppList(Intent intent) {
        PackageManager pm = getApplicationContext().getPackageManager();
        updateDialogHandler = new Handler();
        systemAppList = new ArrayList<>();
        userAppList = new ArrayList<>();

        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle data = new Bundle();

        //do the heavy work here (aka filling all lists)
        listsPopulated = false;
        systemAppList.clear();
        userAppList.clear();

        //iterate through all apps and decide whether they're system or user apps and put them into the corresponding list
        for (ApplicationInfo appInfo : pm.getInstalledApplications(PackageManager.GET_META_DATA)) {
            // use human readable app name instead of package name
            if (isSystemApp(appInfo)) {
                systemAppList.add(new App(pm.getApplicationIcon(appInfo),
                        pm.getApplicationLabel(appInfo).toString(),
                        appInfo.sourceDir, isSystemApp(appInfo)));
                //once all lists are filled update both fragments and deliver fresh lists
            } else {
                userAppList.add(new App(pm.getApplicationIcon(appInfo),
                        pm.getApplicationLabel(appInfo).toString(),
                        appInfo.sourceDir, isSystemApp(appInfo)));
            }
            appCount++;
            data.putInt("progress", appCount);
            receiver.send(0, data);
        }
        listsPopulated = true;
        //send code to dismiss dialog
        data.putParcelableArrayList("systemapps", systemAppList);
        receiver.send(1, data);

    }

    protected boolean isSystemApp(ApplicationInfo appInfo) {
        // if app is system app return true, else false
        return ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

}
