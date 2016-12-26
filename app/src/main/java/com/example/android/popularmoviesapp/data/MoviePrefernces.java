package com.example.android.popularmoviesapp.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Neeraj on 12/26/2016.
 */

public class MoviePrefernces {
    private SharedPreferences sharedPreferences;
    private final static String MYPREFERENCE="mypreference";
    private final static String MOVIESETTING="moviesetting";
    private String moviefilter;

    public MoviePrefernces(Context c) {
        sharedPreferences= c.getSharedPreferences(MYPREFERENCE,0);

    }

    public String getMoviePrfrnce(){

        moviefilter= sharedPreferences.getString(MOVIESETTING,null);
        return moviefilter;
    }

    public void setMoviePrfrnce(String s){
        moviefilter=s;

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(MOVIESETTING,s);
        editor.commit();
    }

}
