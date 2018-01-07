package com.example.nicolas.star1dr;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import database.StarContract;
import database.StarDatabaseHelper;

/**
 * Created by User_1 on 06/01/2018.
 */

public class StarProvider extends ContentProvider {
    private StarDatabaseHelper mHelper = null;

    // AJOUTER REQ : AJOUTER ici un ID : 1,2,3...
    private static final int QUERY_ALL_BR = 1;// requête renvoyant toutes les BusRoutes
    private static final int QUERY_BY_BR_ID = 2;// requête renvoyant une BusRoute selon un ID

    private static final UriMatcher URI_MATCHER =
            new UriMatcher(UriMatcher.NO_MATCH);
    static {
        // AJOUTER REQ : AJOUTER ici l'uri de la requête dans le matcher
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.BusRoutes.CONTENT_PATH, QUERY_ALL_BR);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.BusRoutes.CONTENT_PATH + "/#", QUERY_BY_BR_ID);
    }

    @Override
    public boolean onCreate() {
        mHelper = new StarDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        boolean useAuthorityUri = false;

        // AJOUTER REQ : AJOUTER ici un case avec l'ID de la requête et construire celle-ci à l'aide du builder.
        switch (URI_MATCHER.match(uri)){
            case QUERY_ALL_BR:
                builder.setTables("busroute");
                break;
            case QUERY_BY_BR_ID:
                builder.setTables("busroute");
                builder.appendWhere(StarContract.BusRoutes.BusRouteColumns._ID + " = " +
                        uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
        Cursor cursor = builder.query(db,projection,selection,selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // AJOUTER ici un case avec l'ID de la requête et faire pareil
        switch (URI_MATCHER.match(uri)){
            case QUERY_ALL_BR:
                return  StarContract.BusRoutes.CONTENT_TYPE;
            case QUERY_BY_BR_ID:
                return  StarContract.BusRoutes.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
