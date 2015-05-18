package de.visi0nary.app2system;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.visi0nary.app2system.Model.ListProvider;
import de.visi0nary.app2system.Model.ListType;


public class MainActivity extends FragmentActivity {

    private AppPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private Process suProcess = null;
    private DataOutputStream stdin = null;
    private BufferedWriter writer = null;
    private boolean rootInitialized;


    protected ArrayList<ApplicationInfo> systemAppList;
    protected ArrayList<String> systemAppNamesList;
    protected ArrayList<ApplicationInfo> userAppList;
    protected ArrayList<String> userAppNamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        systemAppList = new ArrayList<>();
        userAppList = new ArrayList<>();
        systemAppNamesList = new ArrayList<>();
        userAppNamesList = new ArrayList<>();
        //show loading screen until Asynctasks are still working
        AppFetcherTask appFetcherTask = new AppFetcherTask(this);
        appFetcherTask.execute();
        try {
            appFetcherTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //initialize fragments in view pager
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new AppPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //set first page on startup to avoid viewing the setting page on every startup
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onResume() {
        initializeRoot();
        super.onResume();
    }

    @Override
    public void onPause() {
        stopRoot();
        super.onPause();
    }

    private void stopRoot() {
        // stop root, clean up
        if(rootInitialized) {
            try {
                writer.close();
                stdin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                suProcess.destroy();
            }
        }
    }

    public AppPagerAdapter getPagerAdapter() {
        return pagerAdapter;
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


    private void initializeRoot(){
        if (deviceIsRooted()) {
            if(busyboxIsInstalled()) {
                try {
                    // start an SU process
                    suProcess = Runtime.getRuntime().exec("su");
                    stdin = new DataOutputStream(suProcess.getOutputStream());
                    writer = new BufferedWriter(new OutputStreamWriter(stdin));
                }
                catch (Exception e) {
                    Toast.makeText(this.getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
                } finally {
                    if (suProcess != null && stdin != null && writer != null) {
                        rootInitialized = true;
                    }
                }
            }
            else {
                Toast.makeText(this.getApplicationContext(), "Busybox is not installed. :(", Toast.LENGTH_LONG).show();
                rootInitialized = false;
            }
        }
        else {
            Toast.makeText(this.getApplicationContext(), "Couldn't acquire root. :(", Toast.LENGTH_LONG).show();
            rootInitialized = false;
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
                Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            // clean up
            if(testSU!=null) {
                testSU.destroy();
            }
        }
        return(returnValue==0);
    }

    public Process getSuProcess() {
        return suProcess;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public DataOutputStream getStdin() {
        return stdin;
    }

    public boolean isRootInitialized() {
        return rootInitialized;
    }


    //inner worker thread class
    //<input, progress, output>
    class AppFetcherTask extends AsyncTask<Void, Integer, Void> {
        ProgressDialog dialog;

        public AppFetcherTask(Activity activity) {
            dialog = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PackageManager pm = getPackageManager();

            //do the heavy work here (aka filling all lists)
            //an asynctask will be started for each list
            systemAppList.clear();
            userAppList.clear();
            systemAppNamesList.clear();
            userAppNamesList.clear();
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
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.setTitle("Sorry!");
            dialog.setMessage("Please wait while all your installed apps are fetched.");
            dialog.show();
        }


        @Override
        protected void onProgressUpdate(Integer... integers) {
            //update the progress
        }

        @Override
        protected void onPostExecute(Void voi) {
            //set a flag that indicates the data is available
            dialog.dismiss();
        }

        protected boolean isSystemApp(ApplicationInfo appInfo) {
            // if app is system app return true, else false
            return ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        }
    }


    public void rescanAllApps() {
        AppFetcherTask task = new AppFetcherTask(this);
        task.execute();
    }

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
