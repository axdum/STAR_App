package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by nicolas on 26/11/2017.
 */

public class DataSource {
    private SQLiteDatabase database;
    private StarDatabaseHelper dbHelper;

    public DataSource(Context context){
        dbHelper = new StarDatabaseHelper(context);
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

}
