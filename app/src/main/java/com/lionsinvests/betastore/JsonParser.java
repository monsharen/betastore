package com.lionsinvests.betastore;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.util.Log;

class JsonParser {

    private static final String TAG = "JsonParser";

    static List<Application> parse(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray apps = jsonObject.getJSONArray("apps");
        List<Application> applications = new ArrayList<>(apps.length());
        for (int i = 0; i < apps.length(); i++) {
            JSONObject app = apps.getJSONObject(i);
            String iconUrl = app.getString("icon");
            Drawable icon = getIcon(iconUrl);
            Application application = new Application(app.getString("name"), app.getString("url"), app.getString("packageName"), icon);
            applications.add(application);
        }
        return applications;
    }

    private static Drawable getIcon(String url) {
        if (!"".equals(url)) {
            try {
                return downloadImage(url);
            } catch (Exception e) {
                Log.e(TAG, "Failed to download icon from url '" + url + "'");
            }
        }
        return null;
    }

    private static Drawable downloadImage(String url) throws Exception {
        return Drawable.createFromStream(((java.io.InputStream) new java.net.URL(url).getContent()), "tempIcon.png");
    }
}
