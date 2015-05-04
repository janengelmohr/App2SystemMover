package de.visi0nary.app2system;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by visi0nary on 03.05.15.
 */
public class AppPagerAdapter extends FragmentPagerAdapter {
    public AppPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            //return the corresponding fragment for each tab
            case 0: return new SettingsPageFragment();
            case 1: return new UserAppFragment();
            case 2: return new SystemAppFragment();
        }
    return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Settings";
            case 1: return "User Apps";
            case 2: return "System Apps";
        }
        return null;
    }

    @Override
    public int getCount() {
        //return number of tabs
        return 3;
    }


}
