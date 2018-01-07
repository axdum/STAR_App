package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.params.StreamConfigurationMap;

/**
 * Created by nicolas on 26/11/2017.
 */

public class DataSource {
    private SQLiteDatabase database;
    private StarDatabaseHelper dbHelper;
    private int i =0;
    String sqlST = "INSERT INTO stoptime VALUES ";
    String sqlTR = "INSERT INTO trip VALUES ";
    public DataSource(Context context){
        dbHelper = new StarDatabaseHelper(context);
        open();
    }

    public void open() throws SQLException{

     database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertBusRoute(BusRoute busRoute){
        ContentValues values = new ContentValues();
        values.put(StarContract.BusRoutes.BusRouteColumns._ID,busRoute.getId());
        values.put(StarContract.BusRoutes.BusRouteColumns.SHORT_NAME,busRoute.getRouteShortName());
        values.put(StarContract.BusRoutes.BusRouteColumns.LONG_NAME,busRoute.getRouteLongName());
        values.put(StarContract.BusRoutes.BusRouteColumns.DESCRIPTION,busRoute.getRouteDescritpion());
        values.put(StarContract.BusRoutes.BusRouteColumns.TYPE,busRoute.getRoutetype());
        values.put(StarContract.BusRoutes.BusRouteColumns.COLOR,busRoute.getRouteColor());
        values.put(StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR,busRoute.getRouteTextColor());
        database.insert(StarContract.BusRoutes.CONTENT_PATH,null,values);
    }

    public void insertCalendar(Calendar calendar){
        ContentValues values = new ContentValues();
        values.put(StarContract.Calendar.CalendarColumns._ID,calendar.getId());
        values.put(StarContract.Calendar.CalendarColumns.MONDAY,calendar.getMonday());
        values.put(StarContract.Calendar.CalendarColumns.TUESDAY,calendar.getTuesday());
        values.put(StarContract.Calendar.CalendarColumns.WEDNESDAY,calendar.getWednesday());
        values.put(StarContract.Calendar.CalendarColumns.THURSDAY,calendar.getThursday());
        values.put(StarContract.Calendar.CalendarColumns.FRIDAY,calendar.getFriday());
        values.put(StarContract.Calendar.CalendarColumns.SATURDAY,calendar.getSaturday());
        values.put(StarContract.Calendar.CalendarColumns.SUNDAY,calendar.getSunday());
        values.put(StarContract.Calendar.CalendarColumns.START_DATE,calendar.getStartDate());
        values.put(StarContract.Calendar.CalendarColumns.END_DATE,calendar.getEndDate());
        database.insert(StarContract.Calendar.CONTENT_PATH,null,values);
    }

    public void insertStop(Stop stop){
        ContentValues values = new ContentValues();
        values.put(StarContract.Stops.StopColumns._ID,stop.getId());
        values.put(StarContract.Stops.StopColumns.DESCRIPTION,stop.getStopDesc());
        values.put(StarContract.Stops.StopColumns.LATITUDE,stop.getStopLat());
        values.put(StarContract.Stops.StopColumns.LONGITUDE,stop.getStopLon());
        values.put(StarContract.Stops.StopColumns.NAME,stop.getStopName());
        values.put(StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING,stop.getWheelchairBoarding());
        database.insert(StarContract.Stops.CONTENT_PATH,null,values);
    }
    //trop long
   /* public void insertStopTimes(StopTime stopTime){
        ContentValues values = new ContentValues();
        values.put(StarContract.StopTimes.StopTimeColumns._ID,stopTime.getId());
        values.put(StarContract.StopTimes.StopTimeColumns.TRIP_ID,stopTime.getTripId());
        values.put(StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME,stopTime.getArrivalTime());
        values.put(StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME,stopTime.getDepartureTime());
        values.put(StarContract.StopTimes.StopTimeColumns.STOP_ID,stopTime.getStopId());
        values.put(StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE,stopTime.getStopSequence());
        database.insert(StarContract.StopTimes.CONTENT_PATH,null,values);
    }*/

  /*  public void insertTrip(Trip trip){
        ContentValues values = new ContentValues();
        values.put(StarContract.Trips.TripColumns._ID,trip.getId());
        values.put(StarContract.Trips.TripColumns.SERVICE_ID,trip.getServiceId());
        values.put(StarContract.Trips.TripColumns.ROUTE_ID,trip.getRouteId());
        values.put(StarContract.Trips.TripColumns.HEADSIGN,trip.getTripHeadsign());
        values.put(StarContract.Trips.TripColumns.DIRECTION_ID,trip.getDirectionId());
        values.put(StarContract.Trips.TripColumns.BLOCK_ID,trip.getBlockId());
        values.put(StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE,trip.getWheelchairAccessible());
        database.insert(StarContract.Trips.CONTENT_PATH,null,values);
    }*/

    public void insertIntoTrip(String[] value,int id){
        //On récupère les valeurs dans le tableau de string puis on les ajoutes dans la requête
        sqlTR+="("+id+","+value[1]+","+value[0]+","+"\""+value[3]+"\""+","+value[5]+","+"\""+value[6]+"\""+","+value[8]+"),";
        i++;
        // Si la requête contient 500 lignes alors on l'execute
        if(i==500){
            execSqlQuerryTrip();
        }
    }
    public void execSqlQuerryTrip(){
        if(!sqlTR.equals("INSERT INTO trip VALUES ")) {
            sqlTR = sqlTR.substring(0, sqlTR.length() -1);
            database.execSQL(sqlTR);
            //On remet la requête à son etat initiale
            sqlTR = "INSERT INTO trip VALUES ";
            i = 0;
        }
    }
    public void insertIntoStopTimes(String[] value,int id){
        sqlST +="("+id+","+value[0]+","+"\""+value[1]+"\""+","+"\""+value[2]+"\""+","+"\""+value[3]+"\""+","+value[4]+"),";
        i++;
        if(i==500){
          execSqlQuerryStoptime();
        }
    }
    public void execSqlQuerryStoptime(){
        if(!sqlST.equals("INSERT INTO stoptime VALUES ")) {
            sqlST = sqlST.substring(0, sqlST.length() -1);
            database.execSQL(sqlST);
            sqlST = "INSERT INTO stoptime VALUES ";
            i = 0;
        }
    }

}
