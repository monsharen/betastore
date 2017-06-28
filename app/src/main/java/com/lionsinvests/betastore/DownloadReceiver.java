package com.lionsinvests.betastore;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            install(context);
        } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
            Intent dm = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
            dm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(dm);
        }
    }

    private void install(Context context) {
        new ApplicationManager(context).install();
    }
}
