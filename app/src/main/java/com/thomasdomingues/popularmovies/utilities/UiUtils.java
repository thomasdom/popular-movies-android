package com.thomasdomingues.popularmovies.utilities;

import android.content.Context;

public class UiUtils
{
    public static int getGridSpanCount()
    {
        // TODO Return grid span count by screen size / orientation
        return 0;
    }

    public static boolean isFavoriteMovieUserPrefs(Context context) {
        return PreferenceUtils.getUserSortCriteria(context).equals(PreferenceUtils.FAVORITE_MOVIES);
    }
}
