package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nicolas on 24/11/2017.
 */

public class StarDatabaseHelper extends SQLiteOpenHelper {
    //Database name & version
    private static final String DATABASE_NAME = "starData.db";
    private static final int DATABASE_VERSION = 1;
    //Table BUSROUTES
    public static final String TABLE_BUSROUTES = "busroute";
    public static final String SHORT_NAME = "route_short_name";
    public static final String LONG_NAME = "route_long_name";
    public static final String DESCRIPTION = "route_desc";
    public static final String TYPE = "route_type";
    public static final String COLOR = "route_color";
    public static final String TEXT_COLOR = "route_text_color";
    //Table TRIPS
    public static final String TABLE_TRIPS = "trip";
    public static final String ROUTE_ID = "route_id";
    public static final String SERVICE_ID = "service_id";
    public static final String HEADSIGN = "trip_headsign";
    public static final String DIRECTION_ID = "direction_id";
    public static final String BLOCK_ID = "block_id";
    public static final String WHEELCHAIR_ACCESSIBLE = "wheelchair_accessible";
    //Table STOPS
    public static final String TABLE_STOPS = "stop";
    public static final String NAME = "stop_name";
    public static final String STOP_DESCRIPTION = "stop_desc";
    public static final String LATITUDE = "stop_lat";
    public static final String LONGITUDE = "stop_lon";
    public static final String WHEELCHAIR_BOARDING = "wheelchair_boarding";
    //Table STOPTIMES
    public static final String TABLE_STOPTIMES = "stoptime";
    public static final String TRIP_ID = "trip_id";
    public static final String ARRIVAL_TIME = "arrival_time";
    public static final String DEPARTURE_TIME = "departure_time";
    public static final String STOP_ID = "stop_id";
    public static final String STOP_SEQUENCE = "stop_sequence";
    //Table CALENDAR
    public static final String TABLE_CALENDAR = "calendar";
    public static final String MONDAY = "monday";
    public static final String TUESDAY = "tuesday";
    public static final String WEDNESDAY = "wednesday";
    public static final String THURSDAY = "thursday";
    public static final String FRIDAY = "friday";
    public static final String SATURDAY = "saturday";
    public static final String SUNDAY = "sunday";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    //TABLE ROUTEDETAIL
    public static final String TABLE_ROUTEDETAILS = "routedetail";

    // Commande sql pour la création de la base de données
    //Create Table BUSROUTES
    private static final String CREATE_TABLE_BUSROUTES =
            "create table " + TABLE_BUSROUTES + " (" +
                    SHORT_NAME + " VARCHAR, " +
                    LONG_NAME + " VARCHAR, " +
                    DESCRIPTION + " VARCHAR, " +
                    TYPE + " VARCHAR, " +
                    COLOR + " VARCHAR, " +
                    TEXT_COLOR + " VARCHAR );";

    //Create Table TRIPS
    private static final String CREATE_TABLE_TRIPS =
            "create table" + TABLE_TRIPS +"("+
                    ROUTE_ID + "INTEGER, " +
                    SERVICE_ID + "INTEGER,"+
                    HEADSIGN + "VARCHAR," +
                    DIRECTION_ID + "VARCHAR,"+
                    BLOCK_ID + "VARCHAR,"+
                    WHEELCHAIR_ACCESSIBLE + "INTEGER );";

    //Create Table STOPS
    private static final String CREATE_TABLE_STOPS =
            "create table" + TABLE_STOPS +"("+
                    NAME + "VARCHAR,"+
                    STOP_DESCRIPTION +"VARCHAR,"+
                    LATITUDE + "VARCHAR,"+
                    LONGITUDE + "VARCHAR,"+
                    WHEELCHAIR_BOARDING +"INTEGER );";

    //Create Table STOPSTIMES
    private static final String CREATE_TABLE_STOPSTIMES =
            "create table" + TABLE_STOPTIMES +"("+
                    TRIP_ID +"INTEGER,"+
                    ARRIVAL_TIME+"VARCHAR,"+
                    DEPARTURE_TIME+"VARCHAR,"+
                    STOP_ID+"INTEGER,"+
                    STOP_SEQUENCE +"INTEGER );";

    //Create Table CALENDAR
    private static final String CREATE_TABLE_CALENDAR =
            "create table" + TABLE_CALENDAR +"("+
                    MONDAY +"INTEGER,"+
                    TUESDAY +"INTEGER,"+
                    WEDNESDAY +"INTEGER,"+
                    THURSDAY +"INTEGER,"+
                    FRIDAY +"INTEGER,"+
                    SATURDAY +"INTEGER,"+
                    SUNDAY +"INTEGER,"+
                    START_DATE +"INTEGER,"+
                    END_DATE +"INTEGER );";

 //TODO TABLE ROUTEDETAIL?

    public StarDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Execution des requêtes
        db.execSQL(CREATE_TABLE_BUSROUTES);
        db.execSQL(CREATE_TABLE_TRIPS);
        db.execSQL(CREATE_TABLE_STOPS);
        db.execSQL(CREATE_TABLE_STOPSTIMES);
        db.execSQL(CREATE_TABLE_CALENDAR);
        //TODO TABLE ROUTEDETAILS
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(StarDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_BUSROUTES +CREATE_TABLE_TRIPS+CREATE_TABLE_STOPS+CREATE_TABLE_STOPSTIMES+CREATE_TABLE_CALENDAR ); //TODO ROUTEDETAIL
        onCreate(db);
    }
}
