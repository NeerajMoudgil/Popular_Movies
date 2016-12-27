package com.example.android.popularmoviesapp.data;

import android.content.Context;
import android.content.SharedPreferences;



public class MoviePrefernces {
    private SharedPreferences sharedPreferences;
    private final static String MYPREFERENCE="mypreference";
    private final static String MOVIESETTING="moviesetting";
    private String moviefilter;

    /**
     * get the shared preference or create if doesn't exist
     * @param c
     */

    public MoviePrefernces(Context c) {
        sharedPreferences= c.getSharedPreferences(MYPREFERENCE,0);

    }

    /**
     * gets the type of movies user prefer to see
     * @return moviefilter string stored as selected by user from menu
     */

    public String getMoviePrfrnce(){

        moviefilter= sharedPreferences.getString(MOVIESETTING,null);
        return moviefilter;
    }

    /**
     * /**
     * sets the type of movies user prefer to see
     * @param s
     */

    public void setMoviePrfrnce(String s){
        moviefilter=s;

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(MOVIESETTING,s);
        editor.commit();
    }

    /**
     * checks if the same prefernce is selected again in order to avoid same load again
     * @param clickedPreference
     * @return true if same prefernce selected again else false
     */

    public boolean checkSamePreferenceClick(String clickedPreference)
    {
        String currentPrefernce= getMoviePrfrnce();
        if(currentPrefernce.equals(clickedPreference))
            return true;
        else
            return false;

    }
}
