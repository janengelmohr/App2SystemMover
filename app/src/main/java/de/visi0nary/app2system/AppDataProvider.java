package de.visi0nary.app2system;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;

import java.util.ArrayList;

import de.visi0nary.app2system.MainActivity;

/**
 * Created by visi0nary on 19.05.15.
 * This class contains all data models and keeps them consistent
 */
public class AppDataProvider {

    protected ArrayList<ApplicationInfo> systemAppList;
    protected ArrayList<String> systemAppNamesList;
    protected ArrayList<ApplicationInfo> userAppList;
    protected ArrayList<String> userAppNamesList;
    protected MainActivity callingActivity;
    protected boolean listsPopulated = false;

    public AppDataProvider(Activity callingActivity) {
        this.callingActivity = (MainActivity)callingActivity;
        systemAppList = new ArrayList<>();
        userAppList = new ArrayList<>();
        systemAppNamesList = new ArrayList<>();
        userAppNamesList = new ArrayList<>();
    }


    public ArrayList<ApplicationInfo> getSystemAppList() {
        return systemAppList;
    }

    public ArrayList<String> getSystemAppNamesList() {
        return systemAppNamesList;
    }

    public ArrayList<ApplicationInfo> getUserAppList() {
        return userAppList;
    }

    public ArrayList<String> getUserAppNamesList() {
        return userAppNamesList;
    }

    public void updateLists() {
        AppFetcherTask appFetcherTask = new AppFetcherTask();
        appFetcherTask.execute();
    }

    //inner worker thread class
    //<input, progress, output>
    class AppFetcherTask extends AsyncTask<Void, Integer, Void> {

        ProgressDialog dialog;
        Handler updateDialogHandler;
        int appCount = 0;

        public AppFetcherTask() {
            systemAppList = new ArrayList<>();
            userAppList = new ArrayList<>();
            systemAppNamesList = new ArrayList<>();
            userAppNamesList = new ArrayList<>();
            this.updateDialogHandler = new Handler();
        }



        @Override
        protected Void doInBackground(Void... voids) {
            PackageManager pm = callingActivity.getPackageManager();

            //do the heavy work here (aka filling all lists)
            //an asynctask will be started for each list
            listsPopulated = false;
            systemAppList.clear();
            userAppList.clear();
            systemAppNamesList.clear();
            userAppNamesList.clear();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(dialog.getProgress()<=dialog.getMax()) {
                            Thread.sleep(20);
                            updateDialogHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setProgress(appCount);
                                }
                            });
                            if(dialog.getProgress()>=dialog.getMax()) {
                                dialog.dismiss();
                                appCount = 0;
                            }
                        }
                    }
                    catch (Exception e) {
                    }
                }
            }).start();
            //iterate through all apps and decide whether they're system or user apps and put them into the corresponding list
            for (ApplicationInfo appInfo : pm.getInstalledApplications(PackageManager.GET_META_DATA)) {
                // use human readable app name instead of package name
                if (isSystemApp(appInfo)) {
                    systemAppList.add(appInfo);
                    systemAppNamesList.add(pm.getApplicationLabel(appInfo).toString());
                } else {
                    userAppList.add(appInfo);
                    userAppNamesList.add(pm.getApplicationLabel(appInfo).toString());
                }
                appCount++;
            }
            listsPopulated = true;
            return null;
        }

        @Override
        protected void onPreExecute() {
            //show a nice dialog while the processing is done so the user can see what's going on
            dialog = new ProgressDialog(callingActivity);
            dialog.setTitle("Updating app list");
            dialog.setMessage("Please wait while I fetch all installed apps.");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            //set max to the number of available apps
            dialog.setMax(callingActivity.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA).size());
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Void voi) {
            //once all lists are filled update both fragments and deliver fresh lists
            callingActivity.getPagerAdapter().updateSystemApps();
            callingActivity.getPagerAdapter().updateUserApps();
            super.onPostExecute(voi);
        }

        protected boolean isSystemApp(ApplicationInfo appInfo) {
            // if app is system app return true, else false
            return ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        }
    }
}
