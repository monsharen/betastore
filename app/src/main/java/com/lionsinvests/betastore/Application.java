package com.lionsinvests.betastore;

import android.graphics.drawable.Drawable;

public class Application {

    private final String name;
    private final String url;
    private final String packageName;
    private final Drawable icon;

    public Application(String name, String url, String packageName, Drawable icon) {
        this.name = name;
        this.url = url;
        this.packageName = packageName;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getIcon() {
        return icon;
    }
}
