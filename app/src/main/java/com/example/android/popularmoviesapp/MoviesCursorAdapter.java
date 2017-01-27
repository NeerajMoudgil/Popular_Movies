package com.example.android.popularmoviesapp;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.data.MoviesContract;
import com.squareup.picasso.Picasso;

/**
 * Adapter class for DB cursor query data
 */
public class MoviesCursorAdapter extends RecyclerView.Adapter<MoviesCursorAdapter.MoviesAdapterViewHolder> {


    private Cursor mCursor;
    private Context mContext;


    public MoviesCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_movies;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new MoviesAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MoviesAdapterViewHolder holder, int position) {

        int posterIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER);
       final int titleIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE);
        Log.i("posterIndex", String.valueOf(posterIndex));

        mCursor.moveToPosition(position);

        String posterPath = mCursor.getString(posterIndex);
        final String movieTitle = mCursor.getString(titleIndex);
        Log.i("posterIndex", posterPath);
        holder.mImageView.setVisibility(View.VISIBLE);

        Picasso.with(mContext).load(posterPath.toString()).into(holder.mImageView,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

                Log.i("SUCESSS","IMG sucess");
                holder.mnoImageTextView.setVisibility(View.GONE);


            }

            @Override
            public void onError() {
                holder.mnoImageTextView.setVisibility(View.VISIBLE);
                holder.mImageView.setVisibility(View.GONE);
                holder.mnoImageTextView.setText(movieTitle);
                Log.i("ERROR","IMG error");

            }
        });;

    }


    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {

        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null) {
            Log.i("heer", "notifyDataSetChanged");
            this.notifyDataSetChanged();
        }
        return temp;
    }


    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {


        public final ImageView mImageView;
        public final TextView mnoImageTextView;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.movie_poster);
            mnoImageTextView = (TextView) itemView.findViewById(R.id.noposter_tittle);

        }


    }
}