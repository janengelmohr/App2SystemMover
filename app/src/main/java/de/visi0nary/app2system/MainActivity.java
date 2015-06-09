package de.visi0nary.app2system;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import de.visi0nary.app2system.Adapters.AppPagerAdapter;
import de.visi0nary.app2system.Settings.SettingsActivity;
import de.visi0nary.app2system.external.SlidingTabLayout;


public class MainActivity extends AppCompatActivity {

    private AppPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private SlidingTabLayout tabs;

    private Process suProcess = null;
    private DataOutputStream stdin = null;
    private BufferedWriter writer = null;

    private boolean rootInitialized;
    private AppDataProvider dataProvider;

    public static final int START_SETTINGS_ACTIVITY = 1;

    // this flag indicates whether the user has already moved at least one app
    private boolean appsAreDirty = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up the toolbar which is used for material design
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set pager adapter, which is responsible for populating the activity with fragments
        pagerAdapter = new AppPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        //set first page on startup to avoid viewing the setting page on every startup
        viewPager.setCurrentItem(0);

        //set tabs layout used for material design
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(viewPager);
        this.dataProvider = new AppDataProvider(this);
        dataProvider.updateLists();
    }

    @Override
    public void onResume() {
        initializeRoot();
        super.onResume();
    }

    @Override
    public void onPause() {
        stopRoot();
        if(appsAreDirty) {
            createRestartNotification();
        }
        super.onPause();
    }

    public void setDirtyState() {
        this.appsAreDirty = true;
    }


    private void createRestartNotification() {
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
        Intent rebootIntent = new Intent(this, RebootActivity.class);
        PendingIntent pendingRebootIntent = PendingIntent.getActivity(this, 0, rebootIntent, 0);
        nBuilder.setSmallIcon(android.R.drawable.ic_delete).setContentTitle("App2/system")
                .setContentText("It is mandatory to reboot the device after apps have been moved.")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("It is mandatory to reboot the device after apps have been moved."))
                .addAction(android.R.drawable.ic_dialog_alert, "Reboot now", pendingRebootIntent);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(281291, nBuilder.build());
    }

    public AppPagerAdapter getPagerAdapter() {
        return pagerAdapter;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, START_SETTINGS_ACTIVITY);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if the settings activity returns and the main activity needs a refresh (i.e. view mode was changed) it is triggered here
        if((requestCode==START_SETTINGS_ACTIVITY) && (resultCode==RESULT_OK) && data.getBooleanExtra("mainActivityNeedsRefresh", false)) {
            dataProvider.updateLists();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
