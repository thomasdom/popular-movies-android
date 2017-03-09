package com.thomasdomingues.popularmovies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PreferenceUtils
{
    /* Key to access the sorting criteria saved in shared preferences */
    private static final String PREFERENCE_SORT_BY = "movie_list_sort_by";

    /* Sorting criterion */
    public final static String SORT_BY_POPULAR = "popular";
    public final static String SORT_BY_TOP_RATED = "top_rated";

    /**
     * Saves the sorting criteria for movie list.
     *
     * @param sortCriteria The sorting criteria. Must be {@link PreferenceUtils#SORT_BY_POPULAR}
     *                     or {@link PreferenceUtils#SORT_BY_TOP_RATED}
     */
    public static void setUserSortCriteria(Context context, String sortCriteria)
    {
        if (null == sortCriteria) return;

        SharedPreferences prefs = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(PREFERENCE_SORT_BY, sortCriteria);
        edit.apply();
    }

    public static String getUserSortCriteria(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREFERENCE_SORT_BY, SORT_BY_POPULAR);
    }
}
