package de.visi0nary.app2system.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by visi0nary on 25.05.15.
 */
public class App {


    Drawable icon;
    String humanReadableName;
    String installationPath;

    public App(Drawable icon, String humanReadableName, String installationPath) {
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

    public String getPath() {
        return installationPath;
    }

    public void setPath(String installationPath) {
        this.installationPath = installationPath;
    }
}
