package de.visi0nary.app2system.Model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by visi0nary on 25.05.15.
 */
public class App implements Comparable<App>, Parcelable {


    private Drawable icon;
    private String humanReadableName;
    private String installationPath;

    private boolean isSystemApp;

    public App(Drawable icon, String humanReadableName, String installationPath, boolean isSystemApp) {
        this.icon = icon;
        this.humanReadableName = humanReadableName;
        this.installationPath = installationPath;
    }


    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getHumanReadableName() {
        return humanReadableName;
    }

    public void setHumanReadableName(String humanReadableName) {
        this.humanReadableName = humanReadableName;
    }

    public void setIsSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public String getPath() {
        return installationPath;
    }

    public void setPath(String installationPath) {
        this.installationPath = installationPath;
    }

    @Override
    public int compareTo(App app) {
        //compare human readable names to ensure lexicographical ordering
        return(this.getHumanReadableName().compareTo(app.getHumanReadableName()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

    }
}
