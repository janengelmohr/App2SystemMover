package de.visi0nary.app2system;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by visi0nary on 04.05.15.
 */
public class AppFragment extends ListFragment {

    protected ArrayList<ApplicationInfo> systemAppList;
    protected ArrayList<ApplicationInfo> userAppList;
    protected MoveAlertDialogFactory dialogFactory = new MoveAlertDialogFactory();
    protected static boolean deviceIsRooted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_systemapp, container, false);

        return rootView;
    }



    private void moveApp(ApplicationInfo appInfo, int isUserApp) {
        if (deviceIsRooted()) {
            if(busyboxIsInstalled()) {
                Process suProcess = null;
                try {
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
                    finalCommandBuilder.append(" " + targetPath + "\n");
                    Log.i("output ", finalCommandBuilder.toString());

                    // start an SU process
                    suProcess = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
                    // remount system as read/write
                    os.writeBytes("mount -o remount,rw /system \n");
                    // write final command into stream...
                    os.writeBytes(finalCommandBuilder.toString());
                    // remount system as read only again
                    os.writeBytes("mount -o remount,ro /system \n");
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (suProcess != null)
                        // clean up
                        suProcess.destroy();
                }
            }
            else {
                Toast.makeText(getActivity().getApplicationContext(), "Busybox is not installed. :(", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Couldn't acquire root. :(", Toast.LENGTH_LONG).show();
        }
    }

    private boolean busyboxIsInstalled() {
        int returnValue = -1;

        Process busybox = null;
        try {
            // open a busybox
            busybox = Runtime.getRuntime().exec("busybox");
            try {
                // busybox will immediately exit if it exists
                busybox.waitFor();
                // check for the exit value (0 if busybox is installed)
                returnValue = busybox.exitValue();
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            // clean up
            if(busybox!=null) {
                busybox.destroy();
            }
        }
        return(returnValue==0);
    }

    // returns true if device is rooted and false if not
    private boolean deviceIsRooted() {
        int returnValue = -1;
        // create temporary SU process
        Process testSU = null;
        try {
            // try to create a root process
            testSU = Runtime.getRuntime().exec("su");
            DataOutputStream testingStream = new DataOutputStream(testSU.getOutputStream());
            // exit the process again
            testingStream.writeBytes("exit\n");
            testingStream.flush();
            try {
                // wait for the SU process to terminate (if it exists)
                testSU.waitFor();
                // check for the exit value (0 if it was a root process, 255 if not)
                returnValue = testSU.exitValue();
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            // clean up
            if(testSU!=null) {
                testSU.destroy();
            }
        }
        return(returnValue==0);
    }




    // inner factory class
    public class MoveAlertDialogFactory {

        // if type == 0 it's a system app, if 1 it's a user app
        public AlertDialog create(int isUserApp, Activity activity, long id) {
            final long appId = id;
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
                            final Long temp = new Long(appId);
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
                            final Long temp = new Long(appId);
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
