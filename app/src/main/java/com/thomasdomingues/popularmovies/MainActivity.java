package com.thomasdomingues.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.thomasdomingues.popularmovies.utilities.NetworkUtils;

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

    public class FetchMoviesTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDisplayResponse.setText("Loading movie data...");
        }

        @Override
        protected String doInBackground(String... params) {
            /* If there's no criteria, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String criteria = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(criteria);

            try {
                return NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String movieData) {
            if (null != movieData) {
                mDisplayResponse.setText(movieData);
            } else {
                mDisplayResponse.setText("Could not fetch movie data from TMDB.");
            }

        }
    }
}
