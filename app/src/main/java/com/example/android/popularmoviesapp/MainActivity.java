package com.example.android.popularmoviesapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

private SharedPreferences sharedprefrnce ;
    private final static String MYPREFERENCE="mypreference";
    private final static String MOVIESETTING="moviesetting";
    public static  String APIKEY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedprefrnce= getSharedPreferences(MYPREFERENCE,0);
        APIKEY=getString(R.string.movieAPIKEY);

        setContentView(R.layout.activity_main);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MANACTIVITY","createdddddddd menu");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);


        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("MANACTIVITY","preparecallleddddddddd");

        /**
         * everytime menu is created check for user preference and set same menu to checked
         */

        String menuItemSelected=sharedprefrnce.getString(MOVIESETTING,null);
        if (menuItemSelected!=null) {
            Log.d("MAINACTIVITY", menuItemSelected);
            if(menuItemSelected.equals("popular"))
            {
                MenuItem menuitem= menu.findItem(R.id.popular_action);
                menuitem.setChecked(true);

            }else
            {
                MenuItem menuitem= menu.findItem(R.id.top_rated_action);
                menuitem.setChecked(true);
            }
        }else
        {
            SharedPreferences.Editor editor= sharedprefrnce.edit();
            editor.putString(MOVIESETTING,"popular");
            editor.commit();

        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_rated_action:
                setPreference("top_rated");
                return true;
            case R.id.popular_action:
                setPreference("popular");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * changes the user preference either popular or toprated as selected from menu setting
     * @param prefernce preference selected by user
     */

    private void setPreference(String prefernce) {
        SharedPreferences.Editor editor= sharedprefrnce.edit();
        editor.putString(MOVIESETTING,prefernce);
        editor.commit();

    }
}
