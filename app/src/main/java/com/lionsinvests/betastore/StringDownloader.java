package com.lionsinvests.betastore;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class StringDownloader {

    static String download(String url) throws Exception {
        HttpURLConnection connection;
        BufferedReader reader;

        URL urlObj = new URL(url);
        connection = (HttpURLConnection) urlObj.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();

        reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();

    }
}
