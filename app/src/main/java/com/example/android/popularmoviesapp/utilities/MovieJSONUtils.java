package com.example.android.popularmoviesapp.utilities;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesapp.data.Movie;
import com.example.android.popularmoviesapp.data.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MovieJSONUtils {

    //Testing for insert dataa

   public static ContentValues[] contentValuesarr= new ContentValues[9];

    static ArrayList<Movie> movieArryList =null;

   private final static String MOVIES_LIST = "results";


    private final static String POSTER_PATH = "poster_path";
    private final static String MOVIE_OVERVIEW = "overview";
    private final static String MOVIE_RELEASE_DATE = "release_date";
    private final static String MOVIE_TITLE = "original_title";
    private final static String MOVIE_POPULARITY = "popularity";
    private final static String MOVIE_RATING = "vote_average";

    public static ArrayList<Movie> getMoviesFromJSON(Context context, String moviesJSON)
            throws JSONException {
        movieArryList=new ArrayList<Movie>();
        JSONObject movieObject =new JSONObject(moviesJSON);
        JSONArray resultArray= movieObject.getJSONArray(MOVIES_LIST);
        Log.v("JSON GOTTTT",resultArray.toString());
        int resultLength=resultArray.length();

        //trying insert
        ContentValues contentValues = new ContentValues();

        for (int iter=0; iter<resultLength; iter++) {
            JSONObject movie = resultArray.getJSONObject(iter);


            String title = movie.getString(MOVIE_TITLE);

            long rating = movie.getLong(MOVIE_RATING);

            long popularity = movie.getLong(MOVIE_POPULARITY);
            String overview = movie.getString(MOVIE_OVERVIEW);


            String releaseDate = movie.getString(MOVIE_RELEASE_DATE);

            String posterPath = movie.getString(POSTER_PATH);

            URL moviePosterUrl = NetworkUtils.buildImageURL(posterPath);

//Testing insert
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE,title);

            contentValues.put(MoviesContract.MoviesEntry.COLUMN_USERRATING,rating);

            contentValues.put(MoviesContract.MoviesEntry.COLUMN_SYNOPSIS,overview);
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASEDATE,System.currentTimeMillis());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_FAVOURITE,0);
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER,"abc");
            if(iter<9) {
                contentValuesarr[iter] = contentValues;
            }


            movieArryList.add( new Movie(title, rating, popularity, overview, releaseDate, moviePosterUrl));
        }

        return movieArryList;
    }



}
