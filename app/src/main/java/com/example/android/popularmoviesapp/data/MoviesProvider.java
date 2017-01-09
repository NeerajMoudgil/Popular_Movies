package com.example.android.popularmoviesapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;


public class MoviesProvider extends ContentProvider {



    final static int MOVIESALL = 100;
    final static int MOVIESBYID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mmoviedbhelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
        urimatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_NAME, MOVIESALL);
        urimatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_NAME + "/#", MOVIESBYID);
        return urimatcher;

    }


    @Override
    public boolean onCreate() {
        mmoviedbhelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArg, String sortOrder) {
        Cursor cursor;


        Log.i("MOVIEPROVIDER URI", uri.toString());

        switch (sUriMatcher.match(uri)) {
            case MOVIESALL: {
                cursor = mmoviedbhelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArg,
                        null,
                        null,
                        sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                break;
            }
            case MOVIESBYID: {

                String movieId = uri.getLastPathSegment();

                String[] Selectionargs = new String[]{movieId};

                cursor = mmoviedbhelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection + " = ?  ",
                        Selectionargs,
                        null,
                        null,
                        sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                break;

            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mmoviedbhelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case MOVIESALL:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {


                        long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                    Log.i("MOVIES inserted",String.valueOf(rowsInserted));
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String whereclause, String[] strings) {
        int updatedrow;
        switch (sUriMatcher.match(uri)) {
            case MOVIESBYID: {
                String movieId = uri.getLastPathSegment();

                String[] whereargs = new String[]{movieId};
              updatedrow=   mmoviedbhelper.getWritableDatabase().update(MoviesContract.MoviesEntry.TABLE_NAME, contentValues,
                        whereclause + " = ?  ", whereargs);


                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        return updatedrow;
    }
}
