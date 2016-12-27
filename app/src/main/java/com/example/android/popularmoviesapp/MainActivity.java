package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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

import com.example.android.popularmoviesapp.data.Movie;
import com.example.android.popularmoviesapp.data.MoviePrefernces;
import com.example.android.popularmoviesapp.utilities.MovieJSONUtils;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesOnClickHandler {


    private final static String PREFERENCEONE="popular";
    private final static String PREFERENCETWO="top_rated";
    private MoviePrefernces movieprefernce;
    private RecyclerView mRecyclerView;


    private TextView mErrorMessageView;

    private ProgressBar mLoadingIndicator;
    private MoviesAdapter moviesAdapter;
    public static String APIKEY;

    /**
     * creates the activity first screen that will appear on launch
     * initialize reference to all the views in main activity layout to refer later in the code
     * assigns GridLayoutManager to recyclerview with 2 cloumns
     * executes the FetchMoviesDataTask (defined below) to perform async task to get response in background
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        APIKEY=getString(R.string.movieAPIKEY);
        moviesAdapter=new MoviesAdapter(this);

        movieprefernce=new MoviePrefernces(this);

        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_movies);
        mRecyclerView.setAdapter(moviesAdapter);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this,2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);



        mErrorMessageView=(TextView) findViewById(R.id.error_msg);

        mLoadingIndicator=(ProgressBar) findViewById(R.id.loading_indicator);

        String menuItemSelected=movieprefernce.getMoviePrfrnce();
        new FetchMoviesDataTask().execute(menuItemSelected);


    }

    /**
     * shows error view if fail to fetch movies
     * hides view that will show movies
     */

    private void showMoviesData()
    {
        mErrorMessageView.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);

    }
    /**
     * hides error view if fail to fetch movies
     * shows view when able to fetch movies
     */

    private void showErrorView()
    {
        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessageView.setVisibility(View.VISIBLE);
    }

    /**
     * click event on each view of recyclerview
     * @param movie
     */

    @Override
    public void onClick(Movie movie) {
        Log.d("MNIN MOv",movie.toString());
        Intent intent =new Intent(MainActivity.this,MovieDetails.class);
        intent.putExtra("title",movie.getTitle());

        intent.putExtra("rating",movie.getRating());
        intent.putExtra("popularity",movie.getPopularity());
        intent.putExtra("overview",movie.getOverview());
        intent.putExtra("releasedate",movie.getReleaseDate());
        intent.putExtra("posterpath",movie.getPosterPath().toString());
        startActivity(intent);
    }

    /**
     * class defined that will perform background task to fetch data from themovie API
     * on getting the data sets data to adapter
     */

    public class FetchMoviesDataTask extends AsyncTask<String, Void,  ArrayList<Movie>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected  ArrayList<Movie> doInBackground(String... params) {


            if (params.length == 0) {
                return null;
            }

            String prefernce = params[0];
            URL movieUrl = NetworkUtils.buildApiUrl(prefernce,MainActivity.APIKEY);
            Boolean isonline=isOnline();
            Log.d("isOnline",String.valueOf(isonline));
            if(!isOnline()) {
                return null;
            }

            try {
                String jsonMovies = NetworkUtils
                        .getResponseFromHttpUrl(movieUrl);

                Log.v("MainActivity got json",jsonMovies);
                ArrayList<Movie> movieArrayList= MovieJSONUtils.getMoviesFromJSON(MainActivity.this,jsonMovies);


                return movieArrayList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute( ArrayList<Movie> movieArrayList) {

            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieArrayList != null) {
                showMoviesData();
                moviesAdapter.setMoviesData(movieArrayList);
            } else {
                showErrorView();
            }
        }
    }

    /**
     * checks the connectivity
     * @return true if internet is available else false
     */
    public  boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
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
        switch (item.getItemId()) {
            case R.id.top_rated_action:
                loadTopRatedMovies(PREFERENCETWO);
                return true;
            case R.id.popular_action:
                loadPopularMovies(PREFERENCEONE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * set the user preference as per selected option from menu setting.
     * setMoviePrfrnce method defined in MoviepPrefernces class
     * @param preference clicked from menu settings
     */

    public void loadPopularMovies(String preference)
    {
       Boolean errorVisible= checkViewVisibility(mErrorMessageView);
        Boolean samePreference = movieprefernce.checkSamePreferenceClick(preference);
        if(!samePreference || errorVisible)
        {
            moviesAdapter.setMoviesData(null);
            new FetchMoviesDataTask().execute(preference);
            movieprefernce.setMoviePrfrnce(preference);

        }

    }
    public void loadTopRatedMovies(String preference)
    {
        Boolean errorVisible= checkViewVisibility(mErrorMessageView);

        Boolean samePreference = movieprefernce.checkSamePreferenceClick(preference);
        if(!samePreference || errorVisible)
        {
            moviesAdapter.setMoviesData(null);
            new FetchMoviesDataTask().execute(preference);
            movieprefernce.setMoviePrfrnce(preference);
        }

    }

    /**
     *
     * @param v type of view to check
     * @return true if view visible else false
     */

    public boolean checkViewVisibility(View v)
    {
        int visible=v.getVisibility();
        Log.i("MAINACTIVITY VISIBILITY",String.valueOf(visible));
        if(visible==4)
            return false;
        else
            return true;
    }


}
