package de.visi0nary.app2system.Settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.visi0nary.app2system.R;

public class AdvancedSettingsFragment extends PreferenceFragment {

    SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advanced_settings, container, false);
        //no need to implement logic here as Android will manage all changes in the settings itself! \o/
    }

    @Override
    public void onResume() {
        super.onResume();
        //listen for a change of "pref_show_core_apps" which needs a recycler view update afterwards
        this.onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if(s.toString().equals("pref_show_core_apps")) {
                    SettingsActivity settingsActivity = (SettingsActivity) getActivity();
                    //view will be refreshed when closing the settings once this preference was changed
                    settingsActivity.setMainActivityNeedsRefresh(true);
                }
            }
        };
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this.onSharedPreferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the listener again
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this.onSharedPreferenceChangeListener);
    }
}
