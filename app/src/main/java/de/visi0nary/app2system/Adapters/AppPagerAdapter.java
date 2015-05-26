package de.visi0nary.app2system.Adapters;

import android.content.pm.ApplicationInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import de.visi0nary.app2system.Fragments.SystemAppFragment;
import de.visi0nary.app2system.Fragments.UserAppFragment;
import de.visi0nary.app2system.Model.App;

/**
 * Created by visi0nary on 03.05.15.
 * This class manages layout of all fragments
 */
public class AppPagerAdapter extends FragmentStatePagerAdapter {

    UserAppFragment userAppFragment;
    SystemAppFragment systemAppFragment;

    public AppPagerAdapter(FragmentManager fm) {
        super(fm);
        this.userAppFragment = new UserAppFragment();
        this.systemAppFragment = new SystemAppFragment();
    }


    //invokes an update of the user apps list
    public void updateUserApps(ArrayList<App> apps) {
        this.userAppFragment.update(apps);
    }

    //invokes an update of the system apps list
    public void updateSystemApps(ArrayList<App> apps) {
        this.systemAppFragment.update(apps);
    }


    @Override
    public Fragment getItem(int position) {

        switch(position) {
            //return the corresponding fragment for each tab
            case 0: return this.userAppFragment;
            case 1: return this.systemAppFragment;
        }
    return null;
    }

    //set tab titles
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "User Apps";
            case 1: return "System Apps";
        }
        return null;
    }

    //return amount of tabs
    @Override
    public int getCount() {
        //return number of tabs
        return 2;
    }


}
