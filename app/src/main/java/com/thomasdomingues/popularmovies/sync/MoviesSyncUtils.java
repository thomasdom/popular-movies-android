package com.thomasdomingues.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.thomasdomingues.popularmovies.data.MovieContract;

import java.util.concurrent.TimeUnit;

public class MoviesSyncUtils
{
    private static final int SYNC_INTERVAL_MINUTES = 180; // 3 hours
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(SYNC_INTERVAL_MINUTES);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized;

    private static final String MOVIES_SYNC_TAG = "movies-sync";


    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context)
    {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);


        Job syncSunshineJob = dispatcher.newJobBuilder()
                .setService(MoviesFirebaseJobService.class)
                .setTag(MOVIES_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncSunshineJob);
    }

    synchronized public static void initialize(@NonNull final Context context)
    {
        if (sInitialized)
            return;

        sInitialized = true;

        scheduleFirebaseJobDispatcherSync(context);

        Thread checkForEmpty = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Uri moviesQueryUri = MovieContract.MovieEntry.CONTENT_URI;

                String[] projectionColumns = {MovieContract.MovieEntry._ID};

                Cursor cursor = context.getContentResolver().query(
                        moviesQueryUri,
                        projectionColumns,
                        null,
                        null,
                        null);

                if (null == cursor || cursor.getCount() == 0)
                {
                    startImmediateSync(context);
                }

                if (null != cursor)
                    cursor.close();
            }
        });

        checkForEmpty.start();
    }

    public static void startImmediateSync(@NonNull final Context context)
    {
        Intent intentToSyncImmediately = new Intent(context, MoviesSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
