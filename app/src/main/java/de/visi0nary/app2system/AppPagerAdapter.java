package de.visi0nary.app2system;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import de.visi0nary.app2system.Fragments.SettingsPageFragment;
import de.visi0nary.app2system.Fragments.SystemAppFragment;
import de.visi0nary.app2system.Fragments.UserAppFragment;

/**
 * Created by visi0nary on 03.05.15.
 * This class manages layout of all fragments
 */
public class AppPagerAdapter extends FragmentPagerAdapter {

    SettingsPageFragment settingsPageFragment;
    UserAppFragment userAppFragment;
    SystemAppFragment systemAppFragment;

    public AppPagerAdapter(FragmentManager fm) {
        super(fm);
        this.settingsPageFragment = new SettingsPageFragment();
        this.userAppFragment = new UserAppFragment();
        this.systemAppFragment = new SystemAppFragment();
    }


    //invokes an update of the user apps list
    public void updateUserApps() {
        this.userAppFragment.update();
    }

    //invokes an update of the system apps list
    public void updateSystemApps() {
        this.systemAppFragment.update();
    }


    @Override
    public Fragment getItem(int position) {

        switch(position) {
            //return the corresponding fragment for each tab
            case 0: return this.settingsPageFragment;
            case 1: return this.userAppFragment;
            case 2: return this.systemAppFragment;
        }
    return null;
    }

    //set tab titles
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Settings";
            case 1: return "User Apps";
            case 2: return "System Apps";
        }
        return null;
    }

    //return amount of tabs
    @Override
    public int getCount() {
        //return number of tabs
        return 3;
    }


}
