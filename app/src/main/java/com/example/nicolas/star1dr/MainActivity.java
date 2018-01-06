package com.example.nicolas.star1dr;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
        //Start progressing

   //     this.deleteDatabase("starData.db");
        String selectQuery ="select* from trip";
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        int nb = cursor.getCount();
        int i = 0;
       /* int[] tab = new int[50];
        while(cursor.moveToNext()) {
            tab[i] = cursor.getInt(i);
            i++;
        }*/
    /*    String selectQuery2 ="select* from busroute";
        Cursor cursor2 = db.rawQuery(selectQuery2,null);
        cursor2.moveToFirst();
        int nb2 = cursor2.getCount();
        int i2 = 0;
        String selectQuery3 ="select* from calendar";
        Cursor cursor3 = db.rawQuery(selectQuery3,null);
        cursor3.moveToFirst();
        int nb3 = cursor3.getCount();
        int i3 = 0;
        String selectQuery4 ="select* from stop";
        Cursor cursor4 = db.rawQuery(selectQuery4,null);
        cursor4.moveToFirst();
        int nb4 = cursor4.getCount();
        int i4 = 0;
        String selectQuery5 ="select* from stoptime";
        Cursor cursor5 = db.rawQuery(selectQuery5,null);
        cursor5.moveToFirst();
        int nb5 = cursor5.getCount();
        int i5 = 0;*/
     //   db.execSQL("delete from busroute ");
     //   DataSource bs = new DataSource(this);

   //     bs.close();
      /*  String selectQuery = "select count(*) from calendar";
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        int s = cursor.getCount();
        if(cursor.moveToFirst()) {
            int result = cursor.getInt(0);
            cursor.moveToFirst();
            int r = cursor.getInt(1);
            System.out.print(r);
        }/*

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
                            //Status update in textview
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

}
