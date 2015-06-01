package de.visi0nary.app2system;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class SaveCurrentAppLayoutService extends IntentService {
    private static final String ACTION_SAVE_APPS = "de.visi0nary.app2system.action.SAVE";
    private static final String ACTION_RESTORE_APPS = "de.visi0nary.app2system.action.RESTORE";


    public static void saveAppLayout(Context context) {
        Intent intent = new Intent(context, SaveCurrentAppLayoutService.class);
        intent.setAction(ACTION_SAVE_APPS);
        context.startService(intent);
    }

    public static void restoreAppLayout(Context context) {
        Intent intent = new Intent(context, SaveCurrentAppLayoutService.class);
        intent.setAction(ACTION_RESTORE_APPS);
        context.startService(intent);
    }

    public SaveCurrentAppLayoutService() {
        super("SaveCurrentAppLayoutService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SAVE_APPS.equals(action)) {
                handleSaveAppLayout();
            } else if (ACTION_RESTORE_APPS.equals(action)) {
                handleRestoreAppLayout();
            }
        }
    }

    private void handleSaveAppLayout() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleRestoreAppLayout() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
