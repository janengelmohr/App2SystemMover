package de.visi0nary.app2system.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import de.visi0nary.app2system.MainActivity;
import de.visi0nary.app2system.R;


public class SettingsActivity extends Activity  {

    boolean mainActivityNeedsRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void setMainActivityNeedsRefresh(boolean mainActivityNeedsRefresh) {
        this.mainActivityNeedsRefresh = mainActivityNeedsRefresh;
        Intent intent = getIntent();
        if(mainActivityNeedsRefresh) {
            intent.putExtra("mainActivityNeedsRefresh", true);
        }
        else {
            intent.putExtra("mainActivityNeedsRefresh", false);
        }
        setResult(RESULT_OK, intent);
    }

}
