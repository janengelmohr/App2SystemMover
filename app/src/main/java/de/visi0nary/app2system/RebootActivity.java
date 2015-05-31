package de.visi0nary.app2system;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;


public class RebootActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reboot);
        try {
            Runtime.getRuntime().exec("su -c reboot");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
