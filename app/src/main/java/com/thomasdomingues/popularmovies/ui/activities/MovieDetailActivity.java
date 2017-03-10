package com.thomasdomingues.popularmovies.ui.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.thomasdomingues.popularmovies.R;
import com.thomasdomingues.popularmovies.data.MovieContract;
import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.ui.fragments.MovieDetailFragment;

import java.util.Date;

public class MovieDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        MovieDetailFragment.OnFragmentInteractionListener
{

    public static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE
    };

    public static final int INDEX_MOVIE_TITLE = 0;
    public static final int INDEX_MOVIE_RELEASE_DATE = 1;
    public static final int INDEX_MOVIE_POSTER_PATH = 2;
    public static final int INDEX_MOVIE_SYNOPSIS = 3;
    public static final int INDEX_MOVIE_VOTE_AVERAGE = 4;

    private static final int MOVIE_DETAILS_LOADER_ID = 51;

    private Uri mUri;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // TODO Move loader in fragment

        mUri = getIntent().getData();

        if (null == mUri)
            throw new NullPointerException("URI for " + MovieDetailActivity.class.getSimpleName() + " cannot be null");

        getSupportLoaderManager().initLoader(MOVIE_DETAILS_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args)
    {
        switch (loaderId)
        {
            case MOVIE_DETAILS_LOADER_ID:
                return new CursorLoader(this,
                        mUri,
                        MOVIE_DETAIL_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        boolean cursorHasValidData = false;
        if (null != data && data.moveToFirst())
        {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }

        String title = data.getString(INDEX_MOVIE_TITLE);
        Date releaseDate = new Date(data.getLong(INDEX_MOVIE_RELEASE_DATE));
        String posterPath = data.getString(INDEX_MOVIE_POSTER_PATH);
        double voteAverage = data.getDouble(INDEX_MOVIE_VOTE_AVERAGE);
        String synopsis = data.getString(INDEX_MOVIE_SYNOPSIS);

        Movie movie = new Movie(posterPath, false, synopsis, releaseDate, null, 0, title, "en", title, null, 0.0, 0, false, voteAverage);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
