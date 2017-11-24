package com.example.nicolas.star1dr;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;


import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StarService extends IntentService {

    ArrayList<StarJsonFile> arrayJsonFile = new ArrayList<StarJsonFile>();

    public StarService() {
        super(StarService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String starurl = intent.getStringExtra("url");

            try {
                InputStream inputStream = null;
                HttpURLConnection urlConnection = null;

               // URL object
                URL url = new URL(starurl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                //200 represents HTTP OK
                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    // On récupère le tableau JSON complet
                    JSONArray jsonArray = new JSONArray(response);
                    // On récupère le tableau d'objets qui nous concernent

                    // Pour tous les objets(lignes) on récupère les infos souhaité
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // On récupère un objet JSON du tableau
                        StarJsonFile jsonFile = new StarJsonFile();
                        JSONObject obj = new JSONObject(jsonArray.getString(i));
                        jsonFile.setDebutvalidite(obj.getJSONObject("fields").getString("debutvalidite"));
                        jsonFile.setFinvalidite(obj.getJSONObject("fields").getString("finvalidite"));
                        jsonFile.setUrl(obj.getJSONObject("fields").getString("url"));
                            // On ajoute la version à la liste
                                arrayJsonFile.add(jsonFile);

                        //Notification si nouvelle version -> marche pas encore
                    /*    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        Notification notification = new Notification.Builder(this)
                                        .setContentTitle("My notification")
                                        .setContentText("Hello World!")
                                        .build();

                        notificationManager.notify(0, notification);*/

                    }
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    }

