package de.visi0nary.app2system.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by visi0nary on 25.05.15.
 */
public class App implements Comparable<App> {


    private Drawable icon;
    private String humanReadableName;
    private String installationPath;
    private boolean isSystemApp;

    public App(Drawable icon, String humanReadableName, String installationPath, boolean isSystemApp) {
        this.icon = icon;
        this.humanReadableName = humanReadableName;
        this.installationPath = installationPath;
        this.isSystemApp = isSystemApp;
    }


    public Drawable getIcon() {
        return icon;
    }

    public String getHumanReadableName() {
        return humanReadableName;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public String getPath() {
        return installationPath;
    }


    @Override
    public int compareTo(App app) {
        //compare human readable names to ensure lexicographical ordering
        return(this.getHumanReadableName().compareTo(app.getHumanReadableName()));
    }
}
