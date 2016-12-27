package com.example.android.popularmoviesapp.data;

import java.net.URL;


public class Movie {
    private String title ;
    private long rating ;
    private long popularity;
    private String overview ;
    private String releaseDate;

    private URL posterPath;

    /**
     * constructor Creates movie object
     * @param title
     * @param rating
     * @param popularity
     * @param overview
     * @param releaseDate
     * @param posterPath
     */

    public Movie(String title, long rating, long popularity, String overview, String releaseDate, URL posterPath) {
        this.title = title;
        this.rating = rating;
        this.popularity = popularity;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    /**
     * Below are the setter and getter methods for all properties of Movie Object type
     */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String synopsis) {
        this.overview = synopsis;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public URL getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(URL posterPath) {
        this.posterPath = posterPath;
    }


}
