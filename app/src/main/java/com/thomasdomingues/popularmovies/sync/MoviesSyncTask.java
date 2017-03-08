package com.thomasdomingues.popularmovies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.thomasdomingues.popularmovies.data.MovieContract;
import com.thomasdomingues.popularmovies.utilities.NetworkUtils;
import com.thomasdomingues.popularmovies.utilities.TMDBJsonUtils;

import java.net.URL;

public class MoviesSyncTask
{
    private static final String PREFERENCE_SORT_BY = "movie_list_sort_by";

    synchronized public static void syncMovies(Context context)
    {
        try
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String criteria = prefs.getString(PREFERENCE_SORT_BY, NetworkUtils.SORT_BY_POPULAR);

            URL movieRequestUrl = NetworkUtils.buildUrl(criteria);

            String jsonMovieListResponse =  NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
            ContentValues[] movieValues = TMDBJsonUtils.getMoviesContentValuesFromJson(jsonMovieListResponse);

            if (movieValues != null && movieValues.length != 0)
            {
                ContentResolver moviesContentResolver = context.getContentResolver();

                moviesContentResolver.delete(
                        MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null);

                moviesContentResolver.bulkInsert(
                        MovieContract.MovieEntry.CONTENT_URI,
                        movieValues);

                // TODO Send notification to user telling him that sync is complete
                /*

                boolean notificationsEnabled = SunshinePreferences.areNotificationsEnabled(context);

                long timeSinceLastNotification = SunshinePreferences
                        .getElapsedTimeSinceLastNotification(context);

                boolean oneDayPassedSinceLastNotification = timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS;

                if (notificationsEnabled && oneDayPassedSinceLastNotification)
                {
                    NotificationUtils.notifyUserOfNewWeather(context);
                }

                */

            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
