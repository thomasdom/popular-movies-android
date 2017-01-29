package com.thomasdomingues.popularmovies.utilities;

import com.thomasdomingues.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility functions to handle The Movie DB JSON data.
 */
public final class TMDBJsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Movie objects
     * describing the movies fetched from The Movie Database API.
     *
     * @param moviesJsonStr JSON response from server
     *
     * @return Array of Movie objects describing movies
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static Movie[] getMoviesFromJson(String moviesJsonStr)
            throws JSONException, ParseException {

        /* Movies list. Each movie is an element of the "results" array */
        final String TMDB_RESULTS = "results";

        /* Movie title */
        final String TMDB_MOVIE_TITLE = "title";
        /* Movie poster URL */
        final String TMDB_MOVIE_POSTER_URL = "poster_path";
        /* Movie release date */
        final String TMDB_MOVIE_RELEASE_DATE = "release_date";
        /* Movie vote average */
        final String TMDB_MOVIE_VOTE_AVG = "vote_average";
        /* Movie plot synopsis */
        final String TMDB_MOVIE_SYNOPSIS = "overview";

        /* TMDB API error code */
        final String TMDB_ERROR_CODE = "status_code";

        /* Movie array to hold each movie */
        Movie[] parsedMovieData;

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (moviesJson.has(TMDB_ERROR_CODE)) {
            int errorCode = moviesJson.getInt(TMDB_ERROR_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    /* Bad or not given API key */
                    return null;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray movieArray = moviesJson.getJSONArray(TMDB_RESULTS);

        parsedMovieData = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            /* These are the values that will be collected */
            String posterPath;
            String title;
            Date releaseDate;
            double voteAverage;
            String synopsis;

            /* Get the JSON object representing the movie */
            JSONObject movieObject = movieArray.getJSONObject(i);

            /* Get actual movie data */
            posterPath = movieObject.getString(TMDB_MOVIE_POSTER_URL);
            title = movieObject.getString(TMDB_MOVIE_TITLE);
            voteAverage = movieObject.getDouble(TMDB_MOVIE_VOTE_AVG);
            synopsis = movieObject.getString(TMDB_MOVIE_SYNOPSIS);

            /* Remove beginning slash from poster path */
            if (posterPath.startsWith("/")) {
                posterPath = posterPath.substring(1, posterPath.length());
            }

            /* Parse directly release date String to java Date object */
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            releaseDate = format.parse(movieObject.getString(TMDB_MOVIE_RELEASE_DATE));

            parsedMovieData[i] = new Movie(title, releaseDate, posterPath, voteAverage, synopsis);
        }

        return parsedMovieData;
    }
}
