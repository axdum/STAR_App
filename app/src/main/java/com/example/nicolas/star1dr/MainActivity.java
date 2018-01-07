package com.example.nicolas.star1dr;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.widget.ProgressBar;
import android.widget.TextView;

import database.DataSource;
import database.StarDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private StarDatabaseHelper database;
    private SQLiteDatabase db;
    final String url = "https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/download/?format=json&timezone=Europe/Berlin";
   // final String url = "http://www.dbs.bzh/portfolio/docs/tco-busmetro-horaires-gtfs-versions-td.json";
   private static MainActivity mInstanceActivity;
    public static MainActivity getmInstanceActivity(){return mInstanceActivity;}
    ProgressBar androidProgressBar;
    int progressStatusCounter = 0;
    TextView textView;
    TextView txtInfos;
    Handler progressHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Set activity layout */
        setContentView(R.layout.activity_main);
        database =new StarDatabaseHelper(this);
        db = database.getWritableDatabase();
        androidProgressBar = (ProgressBar) findViewById(R.id.horizontal_progress_bar);
        textView = (TextView) findViewById(R.id.textView);
        txtInfos = (TextView) findViewById(R.id.textViewInfos);
        mInstanceActivity = this;

      //  this.deleteDatabase("starData.db");

        /* Starting Download Service */
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, StarService.class);
        /* Send optional extras to Download IntentService */
        intent.putExtra("url", url);
        startService(intent);
    }

    public void progressBarSet(String infos,int status) {
        final String inf = infos;
        final int st = status;
        new Thread(new Runnable() {
            public void run() {
                while (progressStatusCounter < st) {
                    progressStatusCounter += 2;
                    progressHandler.post(new Runnable() {
                        public void run() {
                            androidProgressBar.setProgress(progressStatusCounter);

                            textView.setText("Status: " + progressStatusCounter + "/" + androidProgressBar.getMax());
                            txtInfos.setText(inf);
                        }
                    });
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

   public  void createNotification(){

        NotificationManager notificationManager = (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);

       Intent intent = new Intent(this, MainActivity.class);

       PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

       NotificationCompat.Builder mBuilder =
               (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                       .setSmallIcon(R.drawable.ic_starbus)
                       .setContentTitle("Star service")
                       .setContentText("Une nouvelle version est disponible")
                       .setAutoCancel(true)
                        .setContentIntent(pIntent);

       notificationManager.notify(0, mBuilder.build());

   }

}


