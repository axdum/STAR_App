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

    // Commande sql pour la création de la base de données
    //Create Table BUSROUTES
    private static final String CREATE_TABLE_BUSROUTES ="CREATE TABLE "+ StarContract.BusRoutes.CONTENT_PATH + " (" +
                    StarContract.BusRoutes.BusRouteColumns._ID + " INTEGER PRIMARY KEY," +
                    StarContract.BusRoutes.BusRouteColumns.SHORT_NAME + " TEXT," +
                    StarContract.BusRoutes.BusRouteColumns.LONG_NAME + " TEXT," +
                    StarContract.BusRoutes.BusRouteColumns.DESCRIPTION +" TEXT," +
                    StarContract.BusRoutes.BusRouteColumns.TYPE + " INTEGER," +
                    StarContract.BusRoutes.BusRouteColumns.COLOR + " TEXT," +
                    StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR + " TEXT );";

    //Create Table TRIPS
    private static final String CREATE_TABLE_TRIPS = "CREATE TABLE "+ StarContract.Trips.CONTENT_PATH +" ("+
                    StarContract.Trips.TripColumns._ID + " INTEGER PRIMARY KEY, " +
                    StarContract.Trips.TripColumns.SERVICE_ID + " INTEGER, " +
                    StarContract.Trips.TripColumns.ROUTE_ID + " INTEGER, " +
                    StarContract.Trips.TripColumns.HEADSIGN + " TEXT, " +
                    StarContract.Trips.TripColumns.DIRECTION_ID + " INTEGER, " +
                    StarContract.Trips.TripColumns.BLOCK_ID + " TEXT, " +
                    StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE + " INTEGER );";

    //Create Table STOPS
    private static final String CREATE_TABLE_STOPS = "CREATE TABLE "+ StarContract.Stops.CONTENT_PATH +" ("+
                    StarContract.Stops.StopColumns._ID + " TEXT PRIMARY KEY, "+
                    StarContract.Stops.StopColumns.DESCRIPTION  + " TEXT, "+
                    StarContract.Stops.StopColumns.LATITUDE + " REAL, "+
                    StarContract.Stops.StopColumns.LONGITUDE+ " REAL, "+
                    StarContract.Stops.StopColumns.NAME+ " TEXT, "+
                    StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING + " INTEGER );";

    //Create Table STOPSTIMES
    private static final String CREATE_TABLE_STOPSTIMES = "CREATE TABLE "+ StarContract.StopTimes.CONTENT_PATH +" ("+
                    StarContract.StopTimes.StopTimeColumns._ID+" INTEGER PRIMARY KEY,"+
                    StarContract.StopTimes.StopTimeColumns.TRIP_ID+" INTEGER,"+
                    StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME+" TEXT,"+
                    StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME+" TEXT,"+
                    StarContract.StopTimes.StopTimeColumns.STOP_ID+" TEXT,"+
                    StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE+" INTEGER );";

    //Create Table CALENDAR
    private static final String CREATE_TABLE_CALENDAR = "CREATE TABLE "+ StarContract.Calendar.CONTENT_PATH +" ("+
                    StarContract.Calendar.CalendarColumns._ID +" INTEGER PRIMARY KEY,"+
                    StarContract.Calendar.CalendarColumns.MONDAY+" INTEGER,"+
                    StarContract.Calendar.CalendarColumns.TUESDAY+" INTEGER,"+
                    StarContract.Calendar.CalendarColumns.WEDNESDAY+" INTEGER,"+
                    StarContract.Calendar.CalendarColumns.THURSDAY+" INTEGER,"+
                    StarContract.Calendar.CalendarColumns.FRIDAY+" INTEGER,"+
                    StarContract.Calendar.CalendarColumns.SATURDAY+" INTEGER,"+
                    StarContract.Calendar.CalendarColumns.SUNDAY+" INTEGER,"+
                    StarContract.Calendar.CalendarColumns.START_DATE+" INTEGER,"+
                    StarContract.Calendar.CalendarColumns.END_DATE +" INTEGER );";


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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(StarDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " +CREATE_TABLE_BUSROUTES +
                                            CREATE_TABLE_TRIPS+
                                            CREATE_TABLE_STOPS+
                                            CREATE_TABLE_STOPSTIMES+
                                            CREATE_TABLE_CALENDAR );
        onCreate(db);
    }

    public void onDelete(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " +StarContract.BusRoutes.CONTENT_PATH +
                StarContract.Trips.CONTENT_PATH+
                StarContract.Stops.CONTENT_PATH+
                StarContract.StopTimes.CONTENT_PATH+
                StarContract.Calendar.CONTENT_PATH );

    }

}
