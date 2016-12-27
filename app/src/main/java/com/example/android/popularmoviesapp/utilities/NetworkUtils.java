package com.example.android.popularmoviesapp.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {

    private final static String BASE_MOVIE_URL="http://api.themoviedb.org/3/movie/";

    private final static String BASE_IMG_URL="http://image.tmdb.org/t/p/";
    private final static String API_KEY_PARAM="api_key";
    private final static String IMAGE_SIZE="w185";




    public static URL buildApiUrl(String prefernce, String apiKey){
        Uri builtURI = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendEncodedPath(prefernce)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;

        try { url = new URL(builtURI.toString()); }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d("NETWORKUTILS", "URL " + url);

        return url;
    }

    public static URL buildImageURL(String imageresponse){
        Uri builtURI = Uri.parse(BASE_IMG_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendEncodedPath(imageresponse)
                .build();

        URL url = null;

        try { url = new URL(builtURI.toString()); }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else
                return null;
        }  finally {
            httpURLConnection.disconnect();
        }
    }

}
