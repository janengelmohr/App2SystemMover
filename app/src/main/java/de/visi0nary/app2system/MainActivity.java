package de.visi0nary.app2system;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MainActivity extends ListActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: implement adapter sorting logic
        final ArrayList<String> systemAppList = new ArrayList<String>();
        final ArrayList<String> userAppList = new ArrayList<String>();
        // get all apps
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        //iterate through all apps and decide whether they're system or user apps and put them into the corresponding list
        for(ApplicationInfo appInfo : packages) {
            // use human readable app name instead of package name
            if(checkIfAppIsSystemApp(appInfo))
                    systemAppList.add(pm.getApplicationLabel(appInfo).toString());
            else
                    userAppList.add(pm.getApplicationLabel(appInfo).toString());
        }
        // add apps to adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, systemAppList);
        setListAdapter(adapter);



        //startSU();
    }

    private boolean checkIfAppIsSystemApp(ApplicationInfo appInfo) {
        if((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
            //app is system app
            return true;
        else
            //app is user-installed app
            return false;
    }

    /*private void startSU() {
        try {
            // start an SU process
            this.suProcess = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(suProcess.getOutputStream());
            // check for root
            if (deviceIsRooted()) {
                alterPermissions();

                // get settings storage reference
                final SharedPreferences settings = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
                // get current setting
                boolean applyMinfrees = settings.getBoolean(getResources().getString(R.string.text_minfree_values), false);
                if (applyMinfrees) {
                    // if switch is checked, apply reasonable minfree values
                    setReasonableMinfrees();
                }

                if (checkIfPermissionsAreSetCorrect()) {
                    Toast.makeText(getApplicationContext(), "Everything went fine. Enjoy multitasking! :)", Toast.LENGTH_SHORT).show();
                } else {
                    // should never happen, but just in case... :)
                    Toast.makeText(getApplicationContext(), "Mhm... Something went wrong. The permissions can't be altered even though we are rooted.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Couldn't acquire root. :(", Toast.LENGTH_LONG).show();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            // if an exception is thrown, the process is destroyed
            this.suProcess.destroy();
        }
        this.suProcess.destroy();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
