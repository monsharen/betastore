package com.lionsinvests.betastore;

import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String APPLICATION_DATABASE_URL = "http://dev.flexionmobile.com/betastore/apps.json";
    /* Json file format:
        {
           "apps":
           [{
              "name": "Sleeptracker Wear",
              "url": "http://dev.flexionmobile.com/betastore/sleeptracker_wearapk.apk",
              "packageName": "com.urbandroid.sleep",
              "icon": "http://i.imgur.com/GFNInvr.png"
           }]
        }
    */

    private ApplicationAdapter applicationAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isStoragePermissionGranted()) {
            loadApplications();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (applicationAdapter != null) {
            applicationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission: " + permissions[0] + " was " + grantResults[0]);
            loadApplications();
        }
    }

    private void loadApplications() {
        new JsonDownloader(APPLICATION_DATABASE_URL).execute();
    }

    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    class JsonDownloader extends AsyncTask<String, Void, List<Application>> {
        private final String url;

        JsonDownloader(String url) {
            this.url = url;
        }

        @Override
        protected List<Application> doInBackground(String... strings) {
            try {
                String json = StringDownloader.download(url);
                return JsonParser.parse(json);
            } catch (Exception e) {
                Log.e(TAG, "failed to parse applications", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Application> apps) {
            super.onPostExecute(apps);
            if (apps == null) {
                ToastUtil.showToast(MainActivity.this, "Failed to download list");
                return;
            }
            applicationAdapter = new ApplicationAdapter(MainActivity.this, apps);
            ListView listView = (ListView) findViewById(R.id.list_apps);
            listView.setAdapter(applicationAdapter);
        }
    }
}
