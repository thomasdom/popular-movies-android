package com.thomasdomingues.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.thomasdomingues.popularmovies.data.api.MoviesApiClient;
import com.thomasdomingues.popularmovies.data.api.MoviesApiService;
import com.thomasdomingues.popularmovies.data.api.responses.MovieListResponse;
import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.utilities.PreferenceUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity implements
        MovieListAdapter.MovieListAdapterOnClickHandler
{
    private static final String TAG = MainActivity.class.getSimpleName();

    /* Default number of columns displayed in the grid */
    private static final int GRID_SPAN_COUNT = 2;

    /* TMDB API service */
    private MoviesApiService mTmdbApiService;

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

        /* Setup TMDB API service */
        mTmdbApiService = MoviesApiClient.getMoviesApiClientInstance();

        /* Setup adapter */
        mMovieListAdapter = new MovieListAdapter(this, this);

        /* Setup RecyclerView */
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, GRID_SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        mRecyclerView
                .setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mMovieListAdapter);

        /* Fetch movie list from API */
        fetchMovies();
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
                fetchMovies();
                return true;

            case R.id.action_sort_by_top_rated:
                PreferenceUtils.setUserSortCriteria(this, PreferenceUtils.SORT_BY_TOP_RATED);
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
        /* Setup loading UI */
        showMovieList();
        mLoadingIndicator.setVisibility(View.VISIBLE);

        String sortByCriteria = PreferenceUtils.getUserSortCriteria(this);

        mTmdbApiService.discoverMovies(sortByCriteria, 1)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .map(MovieListResponse::getResults)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Movie>>()
                {
                    boolean error = false;

                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        Log.d(TAG, "onSubscribe : " + d.isDisposed());
                    }

                    @Override
                    public void onNext(List<Movie> movies)
                    {
                        mMovieListAdapter.setMovieList(movies);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.e(TAG, e.getMessage());
                        error = true;
                    }

                    @Override
                    public void onComplete()
                    {
                        mLoadingIndicator.setVisibility(View.INVISIBLE);

                        if (mPosition == RecyclerView.NO_POSITION)
                            mPosition = 0;

                        mRecyclerView.smoothScrollToPosition(mPosition);

                        if (error)
                        {
                            showErrorMessage();
                        } else
                        {
                            showMovieList();
                        }
                    }
                });
    }

    /**
     * This method will make the View for the movie list data visible and
     * hide the error message.
     */
    private void showMovieList()
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
}
