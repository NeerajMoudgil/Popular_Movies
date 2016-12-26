package com.example.android.popularmoviesapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesapp.data.MoviePrefernces;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private final static String PREFERENCEONE="popular";
    private final static String PREFERENCETWO="top_rated";
    private MoviePrefernces movieprefernce;
    private RecyclerView mRecyclerView;

    private TextView mErrorMessageView;

    private ProgressBar mLoadingIndicator;
    public static String APIKEY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        APIKEY=getString(R.string.movieAPIKEY);

        movieprefernce=new MoviePrefernces(this);

        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_movies);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this,2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mErrorMessageView=(TextView) findViewById(R.id.error_msg);

        mLoadingIndicator=(ProgressBar) findViewById(R.id.loading_indicator);

        String menuItemSelected=movieprefernce.getMoviePrfrnce();
        new FetchMoviesDataTask().execute(menuItemSelected);


    }

    private void showMoviesData()
    {
        mErrorMessageView.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);

    }

    private void showErrorView()
    {
        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessageView.setVisibility(View.VISIBLE);
    }


    public class FetchMoviesDataTask extends AsyncTask<String, Void, String[]> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {


            if (params.length == 0) {
                return null;
            }

            String prefernce = params[0];
            URL weatherRequestUrl = NetworkUtils.buildApiUrl(prefernce,MainActivity.APIKEY);

            try {
                String jsonMovies = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                Log.v("MainActivity got json",jsonMovies);

               String[] sample={"anccc"};

                return sample;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                showMoviesData();
               // mForecastAdapter.setWeatherData(weatherData);
            } else {
                showErrorView();
            }
        }
    }
    /**
     * Inflate filter options menu
     * @param menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MANACTIVITY","createdddddddd menu");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);


        return true;
    }

    /**
     * is called everytime menu is created.
     * check the user prefernce and check the right option
     * @param menu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("MANACTIVITY","preparecallleddddddddd");

        /**
         * everytime menu is created check for user preference and set same menu to checked
         */

        String menuItemSelected=movieprefernce.getMoviePrfrnce();
        if (menuItemSelected!=null) {
            Log.d("MAINACTIVITY", menuItemSelected);
            if(menuItemSelected.equals(PREFERENCEONE))
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
            movieprefernce.setMoviePrfrnce(PREFERENCEONE);


        }
        return true;

    }

    /**
     * handle click event on menu
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * set the user prefernce as per selected option from menu setting.
         * setMoviePrfrnce method defined in MoviepPrefernces class
         */
        switch (item.getItemId()) {
            case R.id.top_rated_action:

                new FetchMoviesDataTask().execute(PREFERENCETWO);
                movieprefernce.setMoviePrfrnce(PREFERENCETWO);
                return true;
            case R.id.popular_action:
                new FetchMoviesDataTask().execute(PREFERENCEONE);
                movieprefernce.setMoviePrfrnce(PREFERENCEONE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
