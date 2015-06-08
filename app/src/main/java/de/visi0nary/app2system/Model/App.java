package de.visi0nary.app2system.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by visi0nary on 25.05.15.
 */
public class App implements Comparable<App> {


    private Drawable icon;
    private String humanReadableName;
    private String installationPath;
    private AppType type;

    public App(Drawable icon, String humanReadableName, String installationPath, AppType type) {
        this.icon = icon;
        this.humanReadableName = humanReadableName;
        this.installationPath = installationPath;
        this.type = type;
    }


    public Drawable getIcon() {
        return icon;
    }

    public String getHumanReadableName() {
        return humanReadableName;
    }

    public AppType getAppType() {
        return type;
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
