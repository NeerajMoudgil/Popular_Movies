 package com.example.android.popularmoviesapp;

 import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

 public class MovieDetails extends AppCompatActivity {
    private ImageView imageView;
     private TextView textViewReleaseDt;
     private TextView textViewTitle;
     private TextView textViewRating;
     private TextView textViewOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        textViewTitle=(TextView)findViewById(R.id.movie_title);

        textViewReleaseDt=(TextView)findViewById(R.id.movie_releasedt);

        textViewRating=(TextView)findViewById(R.id.rating);
        textViewOverview=(TextView)findViewById(R.id.movie_overview);

        imageView=(ImageView)findViewById(R.id.movie_img);

        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String releaseDate=intent.getStringExtra("releasedate");
        Long rating=intent.getLongExtra("rating",0);
        String overview=intent.getStringExtra("overview");
        String posterpath=intent.getStringExtra("posterpath");

        //Log.i("detailactivity",rating);
        if(title!=null)
        {
            textViewTitle.setText(title);
        }
        if(releaseDate!=null)
        {
            textViewReleaseDt.setText(releaseDate.substring(0,4));
        }
        if(rating!=null)
        {
            textViewRating.setText(String.valueOf(rating)+"/10");
        }
        if(posterpath!=null)
        {
            Picasso.with(this).load(posterpath).into(imageView);

        }
        if(overview!=null)
        {
            textViewOverview.setText(overview);

        }
    }
}
