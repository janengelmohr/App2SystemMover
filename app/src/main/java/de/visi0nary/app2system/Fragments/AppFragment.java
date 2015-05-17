package de.visi0nary.app2system.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.util.ArrayList;

import de.visi0nary.app2system.MainActivity;
import de.visi0nary.app2system.R;

/**
 * Created by visi0nary on 04.05.15.
 */
public class AppFragment extends ListFragment {

    protected ArrayList<ApplicationInfo> systemAppList;
    protected ArrayList<ApplicationInfo> userAppList;
    protected MoveAlertDialogFactory dialogFactory = new MoveAlertDialogFactory();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_systemapp, container, false);

        return rootView;
    }


    private void moveApp(ApplicationInfo appInfo, int isUserApp) {
        MainActivity activity = (MainActivity) getActivity();
        if(activity.isRootInitialized()) {
            BufferedWriter writer = activity.getWriter();
            try {
                // remount system as read/write
                writer.write("mount -o remount,rw /system");
                writer.newLine();
                writer.flush();
                // write final command into stream...
                String moveCommand = determineMoveCommand(appInfo, isUserApp);
                Log.i("output ", moveCommand);
                writer.write(moveCommand);
                writer.newLine();
                writer.flush();
                writer.write("mount -o remount,ro /system");
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String determineMoveCommand(ApplicationInfo appInfo, int isUserApp) {
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




    // inner factory class
    public class MoveAlertDialogFactory {
        protected long appId;
        // if type == 0 it's a system app, if 1 it's a user app
        public AlertDialog create(int isUserApp, Activity activity, long id) {
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
                            moveApp(systemAppList.get(temp.intValue()), 0);
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
                            moveApp(userAppList.get(temp.intValue()), 1);
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
