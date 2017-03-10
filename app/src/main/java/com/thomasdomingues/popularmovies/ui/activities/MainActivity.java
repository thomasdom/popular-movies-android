package com.thomasdomingues.popularmovies.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.thomasdomingues.popularmovies.R;
import com.thomasdomingues.popularmovies.data.MovieContract;
import com.thomasdomingues.popularmovies.ui.adapters.MovieListAdapter;
import com.thomasdomingues.popularmovies.ui.fragments.MovieDetailFragment;
import com.thomasdomingues.popularmovies.ui.fragments.MovieListFragment;
import com.thomasdomingues.popularmovies.utilities.PreferenceUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class MainActivity extends RxAppCompatActivity implements
        MovieListAdapter.MovieListAdapterOnClickHandler,
        MovieListFragment.OnFragmentInteractionListener,
        MovieDetailFragment.OnFragmentInteractionListener
{
    /* Log tag */
    private static final String TAG = MainActivity.class.getSimpleName();

    /* Fragment manager tags */
    private static final String MOVIE_LIST_FRAGMENT_TAG = "fragment_movie_list";
    private static final String MOVIE_DETAIL_FRAGMENT_TAG = "fragment_movie_detail";

    /* Indicates if we are in tablet mode or handset mode */
    private boolean mTwoPanes;

    /* Fragments */
    private MovieListFragment mMovieListFragment;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTwoPanes = findViewById(R.id.movie_details_frag_placeholder) != null;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        mMovieListFragment = (MovieListFragment) getSupportFragmentManager().findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
        if (null == mMovieListFragment)
        {
            // TODO Switch fragments if favorites or movie list called
            MovieListFragment fragment = MovieListFragment.newInstance();

            replaceMoviesFragment(fragment);
        }
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
                PreferenceUtils.setUserSortCriteria(this, PreferenceUtils.SORT_BY_POPULAR);
                mMovieListFragment.fetchMovies();
                return true;

            case R.id.action_sort_by_top_rated:
                PreferenceUtils.setUserSortCriteria(this, PreferenceUtils.SORT_BY_TOP_RATED);
                mMovieListFragment.fetchMovies();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TODO Implement save instance state callbacks

    private void replaceMoviesFragment(MovieListFragment fragment)
    {
        // TODO Set custom animations
        mMovieListFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_list_frag_placeholder, fragment, MOVIE_LIST_FRAGMENT_TAG)
                .commit();
    }

    private void replaceMovieDetailsFragment(MovieDetailFragment fragment)
    {
        // TODO Set custom animations
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_details_frag_placeholder, fragment, MOVIE_DETAIL_FRAGMENT_TAG)
                .commit();
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

        if (mTwoPanes)
        {
            MovieDetailFragment fragment = MovieDetailFragment.newInstance("", "");
            replaceMovieDetailsFragment(fragment);
        } else
        {
            Intent movieDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
            Uri uriForMovieClicked = MovieContract.MovieEntry.buildMovieUriWithId(movieId);
            movieDetailIntent.setData(uriForMovieClicked);

            startActivity(movieDetailIntent);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
