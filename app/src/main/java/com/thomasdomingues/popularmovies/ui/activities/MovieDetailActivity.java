package com.thomasdomingues.popularmovies.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.thomasdomingues.popularmovies.R;
import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.ui.fragments.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity implements
        MovieDetailFragment.OnFragmentInteractionListener
{
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE = "movie_tag";

    /* Fragment manager tags */
    private static final String MOVIE_DETAIL_FRAGMENT_TAG = "fragment_movie_detail";

    /* Fragments */
    private MovieDetailFragment mMovieDetailsFragment;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        if (null == movie)
            throw new NullPointerException(TAG + " intent must contain a movie tagged by EXTRA_MOVIE.");

        mMovieDetailsFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentByTag(MOVIE_DETAIL_FRAGMENT_TAG);
        if (null == mMovieDetailsFragment)
        {
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(movie);

            replaceMovieDetailsFragment(fragment);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {
    }

    private void replaceMovieDetailsFragment(MovieDetailFragment fragment)
    {
        // TODO Set custom animations
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_details_frag_placeholder, fragment, MOVIE_DETAIL_FRAGMENT_TAG)
                .commit();
    }
}
