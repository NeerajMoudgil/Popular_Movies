package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Movie>> {


    private final static String PREFERENCEONE="popular";
    private final static String PREFERENCETWO="top_rated";
    private final static int LOADER_ID=0;
    private MoviePrefernces movieprefernce;
    private RecyclerView mRecyclerView;


    private TextView mErrorMessageView;

    private LoaderManager loaderManager;

    private ProgressBar mLoadingIndicator;
    private MoviesAdapter moviesAdapter;
     private ArrayList<Movie> movieArrayList;
    public static String APIKEY;



    /**
     * creates the activity first screen that will appear on launch
     * initialize reference to all the views in main activity layout to refer later in the code
     * assigns GridLayoutManager to recyclerview with 2 cloumns
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APIKEY = getString(R.string.movieAPIKEY);
        moviesAdapter = new MoviesAdapter(this);

        movieprefernce = new MoviePrefernces(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mRecyclerView.setAdapter(moviesAdapter);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);


        mErrorMessageView = (TextView) findViewById(R.id.error_msg);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        LoaderManager.LoaderCallbacks<ArrayList<Movie>> callback= MainActivity.this;

        Bundle bundleForLoader=null;
        /**
         * loader manager saves the state so no need of saveinstancestate
         */

        loaderManager=getSupportLoaderManager();

        loaderManager.initLoader(LOADER_ID, bundleForLoader, callback);


    }

    @Override
    protected void onPause() {
        Log.i("onpause","onpause");
        super.onPause();
    }

  /*  @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("Maniactivity","on save instance called");
        outState.putParcelableArrayList("movies",movieArrayList);
        super.onSaveInstanceState(outState);
    }*/

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

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<ArrayList<Movie>>(MainActivity.this) {
                ArrayList<Movie> lmovieArrayList=null;

                @Override
                protected void onStartLoading() {

                    if(lmovieArrayList==null)
                    {
                        mLoadingIndicator.setVisibility(View.VISIBLE);

                        forceLoad();;
                    }else
                    {
                        deliverResult(lmovieArrayList);
                    }
                }

                @Override
                public void deliverResult(ArrayList<Movie> data) {
                    lmovieArrayList=data;
                    super.deliverResult(data);
                }

                @Override
                public ArrayList<Movie> loadInBackground() {

                    String prefernce = movieprefernce.getMoviePrfrnce();
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
                        lmovieArrayList= MovieJSONUtils.getMoviesFromJSON(MainActivity.this,jsonMovies);


                        return lmovieArrayList;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        movieArrayList=data;
        moviesAdapter.setMoviesData(data);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movieArrayList != null) {
            showMoviesData();
        } else {
            showErrorView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

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
            movieArrayList=null;
            moviesAdapter.setMoviesData(null);
            loaderManager.restartLoader(LOADER_ID,null,MainActivity.this);
            movieprefernce.setMoviePrfrnce(preference);

        }

    }
    public void loadTopRatedMovies(String preference)
    {
        Boolean errorVisible= checkViewVisibility(mErrorMessageView);

        Boolean samePreference = movieprefernce.checkSamePreferenceClick(preference);
        if(!samePreference || errorVisible)
        {
            movieArrayList=null;
            moviesAdapter.setMoviesData(null);
            /**
             * restarts loader from begining and discards current loader
             */
            loaderManager.restartLoader(LOADER_ID,null,MainActivity.this);


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
