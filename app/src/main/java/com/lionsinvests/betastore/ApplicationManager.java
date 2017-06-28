package com.lionsinvests.betastore;

import java.io.File;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

class ApplicationManager {

    private static final String TAG = "DownloadHandler";

    private static final String APK_FILE_NAME = "temp-app-12345.apk";
    private Context context;

    ApplicationManager(Context context) {
        this.context = context;
    }

    void install() {
        Intent intent = new Intent();
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction( Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + APK_FILE_NAME)),
                "application/vnd.android.package-archive");
        context.startActivity( intent);
    }

    boolean isInstalled(Application application) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(application.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    void uninstall(Application application) {
        Uri packageURI = Uri.parse("package:" + application.getPackageName());
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }

    boolean download(Application application) {

        // FIXME: Ugh.. Need to use the download manager properly

        if (isFileExist()) {
            Log.d(TAG, "file " + APK_FILE_NAME + " already existed. deleting...");
            if (!deleteFile()) {
                Log.e(TAG, "failed to delete " + APK_FILE_NAME + "");
                return false;
            };
        }

        Log.d(TAG, "downloading " + application.getName() + " at url " + application.getUrl());
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request mRqRequest = new DownloadManager.Request(Uri.parse(application.getUrl()));
        mRqRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, APK_FILE_NAME);

        mRqRequest.setMimeType("application/vnd.android.package-archive");
        mRqRequest.setDescription("Download " + application.getName());
        long idDownLoad = downloadManager.enqueue(mRqRequest);
        Log.d(TAG, "download id: "+ idDownLoad);
        return true;
    }

    private boolean isFileExist() {
        File folder1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + APK_FILE_NAME);
        return folder1.exists();
    }

    private boolean deleteFile() {
        File folder1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + APK_FILE_NAME);
        return folder1.delete();
    }
 }
