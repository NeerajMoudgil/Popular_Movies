package com.example.android.popularmoviesapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.data.TrailerReview;

import java.util.ArrayList;

public class TrailerReviewAdapter extends ArrayAdapter<TrailerReview> {

   private ArrayList<TrailerReview> trailerlist=null;


    public TrailerReviewAdapter(Context context, int resource, ArrayList<TrailerReview> trailerlist) {
        super(context, resource,trailerlist);
    }


    @Override
    public int getCount() {
        if(trailerlist!=null)
        return trailerlist.size();
        else
            return 0;
    }

    @Nullable
    @Override
    public TrailerReview getItem(int position) {
        return trailerlist.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_trailers, parent, false);
        }

        TrailerReview trailerReview =getItem(position);


            Log.i("TRIALER EVIEW DETAIL", trailerReview.getTrailename());
        TextView trailernameTextView = (TextView) listItemView.findViewById(R.id.trailerName);
        ImageView shareImageView = (ImageView) listItemView.findViewById(R.id.sharebutton);

       // shareImageView.setOnClickListener();

        trailernameTextView.setText(trailerReview.getTrailename());




        return listItemView;
    }

    public void setTrailerlist(ArrayList<TrailerReview> trailerreviewlist)
    {
        Log.i("here lius came",String.valueOf(trailerreviewlist.size()));
        trailerlist=trailerreviewlist;
        notifyDataSetChanged();
    }


}

