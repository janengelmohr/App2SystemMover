package de.visi0nary.app2system.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;

import de.visi0nary.app2system.MainActivity;
import de.visi0nary.app2system.R;

/**
 * Created by visi0nary on 04.05.15.
 * This class holds all methods that both SystemAppFragment and UserAppFragment use
 */
public class AppFragment extends ListFragment {

    protected MoveAlertDialogFactory dialogFactory = new MoveAlertDialogFactory();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_applist, container, false);
        return rootView;
    }


    private void moveApp(ApplicationInfo appInfo, int isUserApp, boolean isUndone) {
        //method that moves the app with a simple shell command (mv)
        MainActivity activity = (MainActivity) getActivity();
        if(activity.isRootInitialized()) {
            BufferedWriter writer = activity.getWriter();
            try {
                // remount system as read/write
                writer.write("mount -o remount,rw /system");
                writer.newLine();
                writer.flush();
                // move the app
                String moveCommand = determineMoveCommand(appInfo, isUserApp);
                Log.i("output ", moveCommand);
                writer.write(moveCommand);
                writer.newLine();
                writer.flush();
                // and remount system as read only again
                writer.write("mount -o remount,ro /system");
                writer.newLine();
                writer.flush();
                if(isSystemApp(appInfo)) {
                    //TODO add snackbar to revert action
                   /* if(!isUndone) {
                        SnackbarManager.show(Snackbar.with(activity.getApplicationContext())
                                .text(activity.getPackageManager().getApplicationLabel(appInfo).toString() + " moved to /data")
                                .actionLabel("Undo")
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        //moveApp(appInfo, 1, true);
                                    }
                                }));
                    }*/
                    activity.getDataProvider().getSystemAppList().remove(appInfo);
                    activity.getDataProvider().getSystemAppNamesList().remove(activity.getPackageManager().getApplicationLabel(appInfo).toString());
                    activity.getDataProvider().getUserAppList().add(appInfo);
                    activity.getDataProvider().getUserAppNamesList().add(activity.getPackageManager().getApplicationLabel(appInfo).toString());
                }
                else {
                    /*if(!isUndone) {
                        SnackbarManager.show(Snackbar.with(activity.getApplicationContext())
                                .text(activity.getPackageManager().getApplicationLabel(appInfo).toString() + " moved to /system")
                                .actionLabel("Undo")
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        //moveApp(appInfo, 0, true);
                                    }
                                }));
                    }*/
                    Toast.makeText(getActivity().getApplicationContext(), activity.getPackageManager().getApplicationLabel(appInfo).toString() + " successfully moved to /system!", Toast.LENGTH_LONG).show();
                    activity.getDataProvider().getUserAppList().remove(appInfo);
                    activity.getDataProvider().getUserAppNamesList().remove(activity.getPackageManager().getApplicationLabel(appInfo).toString());
                    activity.getDataProvider().getSystemAppList().add(appInfo);
                    activity.getDataProvider().getSystemAppNamesList().add(activity.getPackageManager().getApplicationLabel(appInfo).toString());
                }
                activity.getPagerAdapter().updateSystemApps();
                activity.getPagerAdapter().updateUserApps();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean isSystemApp(ApplicationInfo appInfo) {
        // if app is system app return true, else false
        return ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    private String determineMoveCommand(ApplicationInfo appInfo, int isUserApp) {
        // this method returns the move command (busybox mv *source* *destination*)
        StringBuilder finalCommandBuilder = new StringBuilder("busybox mv ");
        String path = new String(appInfo.sourceDir);
        // thanks to Markus Heider for the idea of using split instead of a regex
        String[] splittedPath = path.split("/");
        StringBuilder pathBuilder = new StringBuilder();
        for (int i = 0; i < splittedPath.length - 1; i++) {
            pathBuilder.append(splittedPath[i] + "/");
            finalCommandBuilder.append(splittedPath[i] + "/");
        }
        String targetPath;
        if (isUserApp == 0) {
            targetPath = pathBuilder.toString().replace("system", "data");
        } else {
            targetPath = pathBuilder.toString().replace("data", "system");
        }
        finalCommandBuilder.append(" " + targetPath);

        return finalCommandBuilder.toString();
    }

    // inner factory class that creates confirmation messages before an app is really moved
    public class MoveAlertDialogFactory {
        protected long appId;
        // if type == 0 it's a system app, if 1 it's a user app
        public AlertDialog create(int isUserApp, final Activity activity, long id) {
            final MainActivity mainActivity = (MainActivity) activity;
            appId=id;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            switch (isUserApp) {
                //it's a system app
                case 0:
                    alertDialogBuilder.setMessage(R.string.txt_alertdialog_move_to_data);
                    alertDialogBuilder.setTitle(R.string.txt_alertdialog_headline);
                    alertDialogBuilder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //user clicked ok
                            Long temp = Long.valueOf(appId);
                            moveApp(mainActivity.getDataProvider().getSystemAppList().get(temp.intValue()), 0, false);
                        }
                    });

                    alertDialogBuilder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //user canceled action
                        }
                    });
                    break;
                //it's a user app
                case 1:
                    alertDialogBuilder.setMessage(R.string.txt_alertdialog_move_to_system);
                    alertDialogBuilder.setTitle(R.string.txt_alertdialog_headline);
                    alertDialogBuilder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //user clicked ok
                            Long temp = Long.valueOf(appId);
                            moveApp(mainActivity.getDataProvider().getUserAppList().get(temp.intValue()), 1, false);
                        }
                    });

                    alertDialogBuilder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //user canceled action
                        }
                    });
                    break;
                //something went wrong (should never happen)
                default:
                    alertDialogBuilder.setMessage(R.string.txt_error_creating_dialog);
                    alertDialogBuilder.setTitle(R.string.txt_error_creating_dialog_headline);
                    alertDialogBuilder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //user canceled action
                    }
                });

            }
            return alertDialogBuilder.create();
        }
    }
}
