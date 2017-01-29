package com.thomasdomingues.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thomasdomingues.popularmovies.adapters.MovieListAdapter;
import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.utilities.NetworkUtils;
import com.thomasdomingues.popularmovies.utilities.TMDBJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int GRID_SPAN_COUNT = 2;

    private RecyclerView mRecyclerView;
    private MovieListAdapter mMovieListAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Setup children views of main activity's layout */
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /* Set grid layout manager for the RecyclerView */
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, GRID_SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieListAdapter = new MovieListAdapter(this);

        mRecyclerView.setAdapter(mMovieListAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        fetchPopularMovies();
    }

    private void fetchPopularMovies() {
        showMovieListDataView();
        new FetchMoviesTask().execute(NetworkUtils.SORT_BY_POPULAR);
    }

    private void fetchTopRatedMovies() {
        showMovieListDataView();
        new FetchMoviesTask().execute(NetworkUtils.SORT_BY_TOP_RATED);
    }

    /**
     * This method will make the View for the movie list data visible and
     * hide the error message.
     */
    private void showMovieListDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie list
     * View.
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        if (null != movie) {
            Log.d(TAG, "Clicked on movie: " + movie.getTitle());

            Intent movieDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
            movieDetailIntent.putExtra(MovieDetailActivity.TAG_MOVIE_DATA, movie);

            startActivity(movieDetailIntent);
        }
        else {
            if (null != mToast) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, R.string.error_message_movie_detail, Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            /* If there's no criteria, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String criteria = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(criteria);

            try {
                String jsonMovieListResponse =  NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                return TMDBJsonUtils.getMoviesFromJson(jsonMovieListResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (null != movieData) {
                showMovieListDataView();
                mMovieListAdapter.setMovieListData(movieData);
            } else {
                showErrorMessage();
            }

        }
    }
}
