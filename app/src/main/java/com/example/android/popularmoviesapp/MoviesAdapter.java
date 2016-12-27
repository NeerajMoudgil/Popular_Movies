package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesapp.data.Movie;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

/**
 * class defines the list of movies
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {



    private ArrayList<Movie> movieArrList;
    private Context mcontext;

    private final MoviesOnClickHandler mClickHandler;


    /**
     * The interface that provides onclick.
     */
    public interface MoviesOnClickHandler {
        void onClick(Movie movie);
    }

    public MoviesAdapter(MoviesOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }
    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mImageView;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView=(ImageView) itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            int adapterPosition = getAdapterPosition();
            Log.d("adapter posnn",String.valueOf(adapterPosition));
            Movie movieclicked=movieArrList.get(adapterPosition);
            mClickHandler.onClick(movieclicked);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_movies;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        mcontext=view.getContext();
        return new MoviesAdapterViewHolder(view);    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        Movie movie = movieArrList.get(position);
        URL posterPath= movie.getPosterPath();
        Picasso.with(mcontext).load(posterPath.toString()).into(holder.mImageView);

       // holder.mImageView.setImageResource(movie.getPosterPath());


    }

    @Override
    public int getItemCount() {
        if(movieArrList==null) {
            return 0;
        }else
        {
            return movieArrList.size();
        }
    }

    /**
     * sets the arraylist to list of new movies
     * @param moviesList new movie list to be displayed
     */
    public void setMoviesData(ArrayList<Movie> moviesList)
    {
        movieArrList=moviesList;
        notifyDataSetChanged();

    }
}
