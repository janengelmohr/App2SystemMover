package de.visi0nary.app2system;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    private AppPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize fragments
        //get "root view"
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new AppPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);




        //startSU();
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
