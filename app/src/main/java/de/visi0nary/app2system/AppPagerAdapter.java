package de.visi0nary.app2system;

import android.content.pm.ApplicationInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import de.visi0nary.app2system.Fragments.SystemAppFragment;
import de.visi0nary.app2system.Fragments.UserAppFragment;

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
    public void updateUserApps(ArrayList<ApplicationInfo> apps, ArrayList<String> names) {
        this.userAppFragment.update(apps, names);
    }

    //invokes an update of the system apps list
    public void updateSystemApps(ArrayList<ApplicationInfo> apps, ArrayList<String> names) {
        this.systemAppFragment.update(apps, names);
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
