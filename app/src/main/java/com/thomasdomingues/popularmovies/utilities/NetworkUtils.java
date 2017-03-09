package com.thomasdomingues.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /* Poster images API */
    private static final String TMDB_POSTER_URL = "https://image.tmdb.org/t/p";

    // Poster sizes (default to "M")
    public final static String POSTER_SIZE_XS = "w92";
    public final static String POSTER_SIZE_S = "w154";
    public final static String POSTER_SIZE_M = "w185";
    public final static String POSTER_SIZE_L = "w342";
    public final static String POSTER_SIZE_XL = "w500";
    public final static String POSTER_SIZE_XXL = "w780";
    public final static String POSTER_SIZE_ORIGINAL = "original";

    private final static String POSTER_SIZE_DEFAULT = POSTER_SIZE_M;

    /**
     * Builds the URL used to query posters from themoviedatabase.org API.
     *
     * @param posterPath The path of the poster that will be queried for.
     * @return The URL to use to query the poster from themoviedatabase.org API.
     */
    public static URL buildPosterUrl(String posterPath) {
        if (posterPath.startsWith("/"))
            posterPath = posterPath.substring(1);

        Uri builtUri = Uri.parse(TMDB_POSTER_URL).buildUpon()
                .appendPath(POSTER_SIZE_DEFAULT)
                .appendPath(posterPath)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Built poster URI: " + url);

        return url;
    }
}
