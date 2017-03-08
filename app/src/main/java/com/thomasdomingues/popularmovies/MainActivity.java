package com.thomasdomingues.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thomasdomingues.popularmovies.adapters.MovieListAdapter;
import com.thomasdomingues.popularmovies.data.MovieContract;
import com.thomasdomingues.popularmovies.sync.MoviesSyncUtils;
import com.thomasdomingues.popularmovies.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        MovieListAdapter.MovieListAdapterOnClickHandler
{
    private static final String TAG = MainActivity.class.getSimpleName();

    /* Key to access the sorting criteria saved in shared preferences */
    private static final String PREFERENCE_SORT_BY = "movie_list_sort_by";

    public static final String[] MAIN_MOVIES_PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_POSTER_PATH = 1;

    /* Movie grid loader ID */
    private static final int MOVIES_FETCH_LOADER_ID = 41;

    /* Default number of columns displayed in the grid */
    private static final int GRID_SPAN_COUNT = 2;

    /* Movie grid adapter */
    private MovieListAdapter mMovieListAdapter;

    /* Recycler view position */
    private int mPosition = RecyclerView.NO_POSITION;

    /* Child views */
    @BindView(R.id.rv_movie_list)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.tv_error_message_display)
    protected TextView mErrorMessageDisplay;

    @BindView(R.id.pb_loading_indicator)
    protected ProgressBar mLoadingIndicator;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Setup children views of main activity's layout */
        ButterKnife.bind(this);

        /* Set grid layout manager for the RecyclerView */
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, GRID_SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieListAdapter = new MovieListAdapter(this, this);

        mRecyclerView.setAdapter(mMovieListAdapter);

        showMovieListDataView();

        getSupportLoaderManager().initLoader(MOVIES_FETCH_LOADER_ID, null, this);

        MoviesSyncUtils.initialize(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_sort_by_popular:
                saveMovieListSorting(NetworkUtils.SORT_BY_POPULAR);
                fetchMovies();
                return true;

            case R.id.action_sort_by_top_rated:
                saveMovieListSorting(NetworkUtils.SORT_BY_TOP_RATED);
                fetchMovies();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method fetch movie list by saved criteria in SharedPreferences, right after
     * cleaning the RecyclerView.
     */
    private void fetchMovies()
    {
        showMovieListDataView();

        getSupportLoaderManager().restartLoader(MOVIES_FETCH_LOADER_ID, null, this);
    }

    /**
     * This method will make the View for the movie list data visible and
     * hide the error message.
     */
    private void showMovieListDataView()
    {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie list
     * View.
     */
    private void showErrorMessage()
    {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * Starts an activity to display details of the selected movie.
     *
     * @param movieId The selected movie ID from the list.
     */
    @Override
    public void onClick(long movieId)
    {
        Log.d(TAG, "Clicked on movie ID: " + movieId);

        Intent movieDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
        Uri uriForMovieClicked = MovieContract.MovieEntry.buildMovieUriWithId(movieId);
        movieDetailIntent.setData(uriForMovieClicked);

        startActivity(movieDetailIntent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args)
    {
        mLoadingIndicator.setVisibility(View.VISIBLE);

        switch (loaderId)
        {
            case MOVIES_FETCH_LOADER_ID:
                Uri moviesQueryUri = MovieContract.MovieEntry.CONTENT_URI;
                // TODO Adjust sort order according to user's preferences
                String sortOrder = MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " ASC";

                return new CursorLoader(this,
                        moviesQueryUri,
                        MAIN_MOVIES_PROJECTION,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        mMovieListAdapter.swapCursor(data);

        if (mPosition == RecyclerView.NO_POSITION)
            mPosition = 0;

        mRecyclerView.smoothScrollToPosition(mPosition);

        if (data.getCount() > 0)
        {
            showMovieListDataView();
        } else
        {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mMovieListAdapter.swapCursor(null);
    }

    /**
     * Saves the sorting criteria for movie list.
     *
     * @param sortBy The sorting criteria. Must be {@link NetworkUtils#SORT_BY_POPULAR}
     *               or {@link NetworkUtils#SORT_BY_TOP_RATED}
     */
    public void saveMovieListSorting(String sortBy)
    {
        if (null == sortBy)
        {
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(PREFERENCE_SORT_BY, sortBy);
        edit.apply();
    }

    /**
     * Get the sorting criteria saved in SharedPreferences.
     *
     * @return The sorting criteria
     */
    public String getMovieListSorting()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(PREFERENCE_SORT_BY, NetworkUtils.SORT_BY_POPULAR);
    }
}
