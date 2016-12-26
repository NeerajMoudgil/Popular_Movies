package com.example.android.popularmoviesapp.data;

import java.net.URL;


public class Movie {
    private String title ;
    private long rating ;
    private long popularity;
    private String synopsis ;
    private String releaseDate;

    private URL posterPath;

    public Movie(String title, long rating, long popularity, String synopsis, String releaseDate, URL posterPath) {
        this.title = title;
        this.rating = rating;
        this.popularity = popularity;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

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

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
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
