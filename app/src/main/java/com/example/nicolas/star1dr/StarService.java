package com.example.nicolas.star1dr;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import database.*;

public class StarService extends IntentService {

    private ArrayList<StarJsonFile> arrayJsonFile = new ArrayList<StarJsonFile>();
    private StarDatabaseHelper database;
    private SQLiteDatabase db;
    private InputStream inputStream = null;
    private HttpURLConnection urlConnection = null;
    private int statusCode;

    public StarService() {
        super(StarService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    /*    showToast("Starting IntentService");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        showToast("Finishing IntentService");*/


        String starurl = intent.getStringExtra("url");
            try {
               // URL object

                statusCode = urlConnection(starurl);
                //200 represents HTTP OK
                if (statusCode == 200) {
                    MainActivity.getmInstanceActivity().progressBarSet("Connection etablie",6);
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    // On récupère le tableau JSON complet
                    JSONArray jsonArray = new JSONArray(response);
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

                    database = new StarDatabaseHelper(this);
                    db = database.getWritableDatabase();
                    //Création d'une requêt pour voir si bdd vide ou pas
                    String selectQuery = "select* from busroute";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date currentDate = new Date();
                    formatter.format(currentDate);

                    //Si > 0 alors bdd contient des infos sinon bdd vide
                    if(cursor.getCount() == 0){
                        StarJsonFile jsonFile = new StarJsonFile();
                        //On récupère le premier fichier
                        jsonFile = arrayJsonFile.get(0);
                        String finValidite = jsonFile.getFinvalidite().toString();
                        Date dateFin = formatter.parse(finValidite);
                        //on regarde si la date de fin de validité de la version n'est pas inférieur à la date courante
                        if(currentDate.compareTo(dateFin)<0){
                            String urlData = jsonFile.getUrl().toString();
                            statusCode = urlConnection(urlData);
                            if(statusCode == 200){
                                 extractZip();
                          }
                            MainActivity.getmInstanceActivity().progressBarSet("Fichier inséré",100);
                        }
                    }
                    else{
                        //si nouvelle version on créer la notif pour annoncer la nouvelle version
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

    private int urlConnection (String url){
        try {
            URL urlConnect = new URL(url);
            urlConnection = (HttpURLConnection) urlConnect.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("GET");
            return urlConnection.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;

     }

    private void extractZip(){
        MainActivity.getmInstanceActivity().progressBarSet("Extraction du fichier",14);
         try{
             ZipInputStream inputStreamzip = new ZipInputStream(urlConnection.getInputStream());
             ZipEntry entry = inputStreamzip.getNextEntry();
             ArrayList<String[]> arrayLine = new ArrayList<String[]>();
             MainActivity.getmInstanceActivity().progressBarSet("Insertion route",20);
             while(entry != null){

                 switch(entry.getName()){
                     case "calendar.txt" :
                         extractFileLine(inputStreamzip,entry);
                         MainActivity.getmInstanceActivity().progressBarSet("Insertion trips",60);
                         entry = inputStreamzip.getNextEntry();
                         break;
                     case "routes.txt" :
                         extractFileLine(inputStreamzip,entry);
                         MainActivity.getmInstanceActivity().progressBarSet("Insertion stop",30);
                         entry = inputStreamzip.getNextEntry();
                         break;
                     case "stops.txt" :
                         extractFileLine(inputStreamzip,entry);
                         MainActivity.getmInstanceActivity().progressBarSet("Insertion calendar",40);
                         entry = inputStreamzip.getNextEntry();
                         break;
                     case "stop_times.txt" :
                         extractFileLine(inputStreamzip,entry);
                         MainActivity.getmInstanceActivity().progressBarSet("Insertion stoptimes",90);
                         entry = inputStreamzip.getNextEntry();
                         break;
                     case "trips.txt" :
                         extractFileLine(inputStreamzip,entry);
                         MainActivity.getmInstanceActivity().progressBarSet("Insertion stoptimes",90);
                         entry = inputStreamzip.getNextEntry();
                         break;
                     default:
                         entry = inputStreamzip.getNextEntry();
                         break;
                 }
             }
             inputStreamzip.close();

         } catch (IOException e) {
             e.printStackTrace();
         }
     }

    private void extractFileLine(ZipInputStream inputStreamzip,  ZipEntry entry){

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStreamzip));
            String line = null;

            in.readLine();
            DataSource dataSource = new DataSource(this);
            int id = 0;
            while((line = in.readLine()) != null) {
                StringBuilder responseData = new StringBuilder(line);
                String[] l = responseData.toString().replace("\"","").split(",");
                inseretInBDD(l,entry,dataSource,id);
                id++;
            }
            if(entry.getName().equals("stop_times.txt")) {
                dataSource.execSqlQuerryStoptime();
            }
            else if(entry.getName().equals("trips.txt"))
            {
                dataSource.execSqlQuerryTrip();
            }
            dataSource.close();
         }
          catch (IOException e) {
             e.printStackTrace();
         }
     }

    private void inseretInBDD(String[] sLine ,ZipEntry entry,DataSource dataSource,int id){

            switch (entry.getName()) {
                case "calendar.txt":
                    database.Calendar calendar = new database.Calendar();
                        calendar.setId(Integer.parseInt(sLine[0]));
                        calendar.setMonday(Integer.parseInt(sLine[1]));
                        calendar.setTuesday(Integer.parseInt(sLine[2]));
                        calendar.setWednesday(Integer.parseInt(sLine[3]));
                        calendar.setThursday(Integer.parseInt(sLine[4]));
                        calendar.setFriday(Integer.parseInt(sLine[5]));
                        calendar.setSaturday(Integer.parseInt(sLine[6]));
                        calendar.setSunday(Integer.parseInt(sLine[7]));
                        calendar.setStartDate(Integer.parseInt(sLine[8]));
                        calendar.setEndDate(Integer.parseInt(sLine[9]));
                        dataSource.insertCalendar(calendar);
                    break;
                case "routes.txt":
                    BusRoute busRoute = new BusRoute();
                        busRoute.setId(Integer.parseInt(sLine[0]));
                        busRoute.setRouteShortName(sLine[2]);
                        busRoute.setRouteLongName(sLine[3]);
                        busRoute.setRouteDescritpion(sLine[4]);
                        busRoute.setRoutetype(sLine[5]);
                        busRoute.setRouteColor(sLine[7]);
                        busRoute.setRouteTextColor(sLine[8]);
                        dataSource.insertBusRoute(busRoute);

                    break;
                case "stops.txt":
                    Stop stop = new Stop();
                        stop.setId(sLine[0]);
                        stop.setStopName(sLine[2]);
                        stop.setStopDesc(sLine[3]);
                        stop.setStopLat(sLine[4]);
                        stop.setStopLon(sLine[5]);
                        stop.setWheelchairBoarding(Integer.parseInt(sLine[11]));
                        dataSource.insertStop(stop);

                    break;
                case "stop_times.txt":
                    dataSource.insertIntoStopTimes(sLine,id);
                    break;
                case "trips.txt":
                   dataSource.insertIntoTrip(sLine,id);
                    break;
            }
     }
    public void showToast(final String msg){
        //gets the main thread
        Handler handler = new Handler(Looper.getMainLooper());
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, this, StarService.class);
        final String url = "https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/download/?format=json&timezone=Europe/Berlin";
        handler.post(new Runnable() {
            @Override
            public void run() {
                // run this code in the main thread
                intent.putExtra("url", url);
                onHandleIntent(intent);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    }

