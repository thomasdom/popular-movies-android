package com.thomasdomingues.popularmovies.utilities;

import android.database.Cursor;

import com.thomasdomingues.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataUtils
{
    private static final int INDEX_MOVIE_TITLE = 0;
    private static final int INDEX_MOVIE_RELEASE_DATE = 1;
    private static final int INDEX_MOVIE_POSTER_PATH = 2;
    private static final int INDEX_MOVIE_SYNOPSIS = 3;
    private static final int INDEX_MOVIE_VOTE_AVERAGE = 4;
    private static final int INDEX_MOVIE_TMDB_ID = 5;

    public static List<Movie> cursorToMovieList(Cursor data)
    {
        List<Movie> movies = new ArrayList<>();

        boolean cursorHasValidData = false;
        if (null != data && data.moveToFirst())
        {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData)
        {
            return movies;
        }

        do
        {
            String title = data.getString(INDEX_MOVIE_TITLE);
            Date releaseDate = new Date(data.getLong(INDEX_MOVIE_RELEASE_DATE));
            String posterPath = data.getString(INDEX_MOVIE_POSTER_PATH);
            double voteAverage = data.getDouble(INDEX_MOVIE_VOTE_AVERAGE);
            String synopsis = data.getString(INDEX_MOVIE_SYNOPSIS);
            int tmdbId = data.getInt(INDEX_MOVIE_TMDB_ID);

            Movie movie = new Movie(posterPath, false, synopsis, releaseDate, new ArrayList<>(), tmdbId, title, "en", title, "", 0.0, 0, false, voteAverage);
            movies.add(movie);
        }
        while (data.moveToNext());

        return movies;
    }
}
