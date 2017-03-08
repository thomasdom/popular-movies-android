package com.thomasdomingues.popularmovies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class MoviesSyncIntentService extends IntentService
{
    public MoviesSyncIntentService()
    {
        super(MoviesSyncIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        MoviesSyncTask.syncMovies(this);
    }
}
