package com.thomasdomingues.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.utilities.NetworkUtils;
import com.thomasdomingues.popularmovies.utilities.TMDBJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mDisplayResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDisplayResponse = (TextView) findViewById(R.id.tv_display_response);

        fetchPopularMovies();
    }

    private void fetchPopularMovies() {
        new FetchMoviesTask().execute(NetworkUtils.SORT_BY_POPULAR);
    }

    private void fetchTopRatedMovies() {
        new FetchMoviesTask().execute(NetworkUtils.SORT_BY_TOP_RATED);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDisplayResponse.setText("Loading movie data...");
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
            if (null != movieData) {
                mDisplayResponse.setText("");
                for (Movie movie : movieData) {
                    mDisplayResponse.append(movie.getTitle() + "\n\n\n");
                }
            } else {
                mDisplayResponse.setText("Could not fetch movie data from TMDB.");
            }

        }
    }
}
