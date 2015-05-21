package de.visi0nary.app2system;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class MainActivity extends Activity {

    private AppPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private Process suProcess = null;
    private DataOutputStream stdin = null;
    private BufferedWriter writer = null;
    private boolean rootInitialized;
    private AppDataProvider dataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.dataProvider = new AppDataProvider(this);
        dataProvider.updateLists();

        //initialize fragments in view pager
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new AppPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //set first page on startup to avoid viewing the setting page on every startup
        viewPager.setCurrentItem(0);
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

    public AppPagerAdapter getPagerAdapter() {
        return pagerAdapter;
    }

    public AppDataProvider getDataProvider() {
        return dataProvider;
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
