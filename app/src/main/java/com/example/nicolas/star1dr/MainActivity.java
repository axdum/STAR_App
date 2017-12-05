package com.example.nicolas.star1dr;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import database.StarDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private StarDatabaseHelper database;
    private SQLiteDatabase db;
    final String url = "https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/download/?format=json&timezone=Europe/Berlin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Set activity layout */
        setContentView(R.layout.activity_main);

        database =new StarDatabaseHelper(this);
       db = database.getWritableDatabase();
     //   db.execSQL("delete from busroute ");
      /*  DataSource bs = new DataSource(this);
        bs.open();
        bs.close();
        String selectQuery = "select count(*) from busroute";
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        if(cursor.moveToFirst()) {
            int result = cursor.getInt(0);
            cursor.moveToFirst();
            String r = cursor.getString(1);
            System.out.print(r);
        }*/
        /* Starting Download Service */
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, StarService.class);
        /* Send optional extras to Download IntentService */
        intent.putExtra("url", url);
        startService(intent);
    }


}
