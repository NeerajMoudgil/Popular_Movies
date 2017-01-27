 package com.example.android.popularmoviesapp;

 import android.content.ContentValues;
 import android.content.Intent;
 import android.net.Uri;
 import android.os.Bundle;
 import android.support.design.widget.FloatingActionButton;
 import android.support.v7.app.AppCompatActivity;
 import android.util.Log;
 import android.view.View;
 import android.widget.AdapterView;
 import android.widget.ImageView;
 import android.widget.ListView;
 import android.widget.TextView;

 import com.example.android.popularmoviesapp.data.MoviesContract;
 import com.example.android.popularmoviesapp.data.TrailerReview;
 import com.example.android.popularmoviesapp.utilities.NetworkUtils;
 import com.example.android.popularmoviesapp.utilities.TrailerReviewJSONUtils;
 import com.squareup.picasso.Picasso;

 import java.net.URL;
 import java.util.ArrayList;

 public class MovieDetails extends AppCompatActivity implements  NetworkUtils.onResponseHandler{
    private ImageView imageView;
     private TextView textViewReleaseDt;
     private TextView textViewTitle;
     private TextView textViewRating;
     private TextView textViewOverview;
     private TextView textViewTrailers;
     private TextView textViewReviews;
     private ListView listViewTrailers;
     private ListView listViewReviews;
     private FloatingActionButton favoriteButton;

     private TrailerAdapter trailerAdapter;
     private ReviewAdapter reviewAdapter;
     private long movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        textViewTitle=(TextView)findViewById(R.id.movie_title);

        textViewReleaseDt=(TextView)findViewById(R.id.movie_releasedt);
        textViewTrailers=(TextView)findViewById(R.id.trailer_text);
        textViewReviews=(TextView)findViewById(R.id.review_text);

        favoriteButton= (FloatingActionButton)findViewById((R.id.favoritebutn));
        favoriteButton.setTag(R.id.favouritemovie,R.drawable.ic_favorite_border_white_24dp);



        textViewRating=(TextView)findViewById(R.id.rating);
        textViewOverview=(TextView)findViewById(R.id.movie_overview);

        imageView=(ImageView)findViewById(R.id.movie_img);

        trailerAdapter=new TrailerAdapter(this,0,null);
        reviewAdapter=new ReviewAdapter(this,0,null);



        listViewTrailers= (ListView)findViewById(R.id.trailersListview);

        listViewTrailers.setAdapter(trailerAdapter);

        listViewTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int iposition, long l) {
                TrailerReview trailer=(TrailerReview)adapterView.getItemAtPosition(iposition);
                Log.i("trailerGotonclick",trailer.getTrailerurl());
                runTrailer(trailer.getTrailerurl());

            }
        });

        listViewReviews= (ListView)findViewById(R.id.reviewsListView);
        listViewReviews.setAdapter(reviewAdapter);

        Intent intent=getIntent();
        final String title=intent.getStringExtra("title");
       final String releaseDate=intent.getStringExtra("releasedate");
        final Long rating=intent.getLongExtra("rating",0);
        final String overview=intent.getStringExtra("overview");
        final String posterpath=intent.getStringExtra("posterpath");


        if(intent.hasExtra("movieID"))
        {
            movieId=intent.getLongExtra("movieID",0);
            URL reviewtrailerURL=NetworkUtils.buildReviewTrailerURL(movieId,MainActivity.APIKEY );

            NetworkUtils.getResponseUsingVolley(reviewtrailerURL.toString(),this);
            Log.i("MovieId",String.valueOf(movieId));

        }
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

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int currentImage=(Integer) favoriteButton.getTag(R.id.favouritemovie);
                if(currentImage==R.drawable.ic_favorite_white_24dp)
                {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    favoriteButton.setTag(R.id.favouritemovie,R.drawable.ic_favorite_border_white_24dp);
                    deletetMoviefromFAvorite(movieId);

                }else
                {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
                    favoriteButton.setTag(R.id.favouritemovie,R.drawable.ic_favorite_white_24dp);
                    insertMovietoFAvorite(title,posterpath,overview,rating,releaseDate,0,movieId);




                }
                Log.i("currentImg",String.valueOf(currentImage));

                Log.i("clicked favouite","cc");
            }
        });
    }

     @Override
     public void onResponse(String response) {

         ArrayList<TrailerReview> trailerlist=TrailerReviewJSONUtils.getTrailersFromJSON(response);
         trailerAdapter.setTrailerlist(trailerlist);
         ArrayList<TrailerReview> reviewlist=TrailerReviewJSONUtils.getReviewsFromJSON(response);
         reviewAdapter.setReviewlist(reviewlist);

     }

     public void runTrailer(String url)
     {
         Intent videoIntent= (new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
         startActivity(videoIntent);

     }

     public void  shareTrailer(String url){
         Intent sendIntent = new Intent();
         sendIntent.setAction(Intent.ACTION_SEND);
         sendIntent.putExtra(Intent.EXTRA_TEXT, url);
         sendIntent.setType("text/plain");
         startActivity(sendIntent);
     }

     public void insertMovietoFAvorite(String title,String poster,String synopsis,Long userrating, String releasedate,int favourite,long movieid){

         ContentValues contentValues = new ContentValues();
         contentValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE,title);
         contentValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER,poster);
         contentValues.put(MoviesContract.MoviesEntry.COLUMN_SYNOPSIS,synopsis);
         contentValues.put(MoviesContract.MoviesEntry.COLUMN_USERRATING,userrating);
         contentValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASEDATE,releasedate);
         contentValues.put(MoviesContract.MoviesEntry.COLUMN_FAVOURITE,favourite);
         contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIEID,movieid);

         Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI,contentValues);



     }
     public void deletetMoviefromFAvorite(long movieID){
         Uri deleturi= MoviesContract.MoviesEntry.buildMovieUriWithId(movieID);
         getContentResolver().delete(deleturi,null,null);

     }


 }
