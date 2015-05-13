package de.visi0nary.app2system.Views;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by visi0nary on 10.05.15.
 */
public class AppListView {

    private ImageView appIcon;
    private TextView appName;


    public TextView getAppName() {
        return appName;
    }

    public void setAppName(TextView appName) {
        this.appName = appName;
    }

    public ImageView getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(ImageView appIcon) {
        this.appIcon = appIcon;
    }



}
