package com.thomasdomingues.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.thomasdomingues.popularmovies.BuildConfig;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class NetworkUtils {
    private static volatile OkHttpClient httpClient = null;

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /* Movie data API */
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie";

    private static final String PARAM_API_KEY = "api_key";

    public final static String SORT_BY_POPULAR = "popular";
    public final static String SORT_BY_TOP_RATED = "top_rated";

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

    /* HTTP Client Singleton instance */
    private static OkHttpClient getHTTPClient() {
        // The "Double-Checked Singleton" permits to avoid an expensive call of synchronized,
        // if client instantiation is already done.
        if (null == NetworkUtils.httpClient) {
            // The keyword "synchronized" on this section forbids multiple instantiation,
            // even by different threads.
            synchronized (NetworkUtils.class) {
                if (null == NetworkUtils.httpClient) {
                    NetworkUtils.httpClient = new OkHttpClient();
                }
            }
        }
        return NetworkUtils.httpClient;
    }

    /**
     * Builds the URL used to query themoviedatabase.org API.
     *
     * @param sortByCriteria The criteria that will be queried for.
     * @return The URL to use to query the themoviedatabase.org API.
     */
    public static URL buildUrl(String sortByCriteria) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(sortByCriteria)
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.TMDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Built URI: " + url);

        return url;
    }

    /**
     * Builds the URL used to query posters from themoviedatabase.org API.
     *
     * @param posterPath The path of the poster that will be queried for.
     * @return The URL to use to query the poster from themoviedatabase.org API.
     */
    public static URL buildPosterUrl(String posterPath) {
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

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        Response response = getHTTPClient().newCall(request).execute();
        return response.body().string();
    }
}
