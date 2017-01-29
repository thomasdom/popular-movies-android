package com.thomasdomingues.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String TAG_MOVIE_DATA = "movie_data";

    /* Child views */
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private TextView mSynopsis;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        /* Instantiate child views */
        mPoster = (ImageView) findViewById(R.id.iv_movie_detail_poster);
        mTitle = (TextView) findViewById(R.id.tv_movie_detail_title);
        mReleaseDate = (TextView) findViewById(R.id.tv_movie_detail_release_date);
        mVoteAverage = (TextView) findViewById(R.id.tv_movie_detail_vote_average);
        mSynopsis = (TextView) findViewById(R.id.tv_movie_detail_synopsis);

        /* Get intent passed movie and display details */
        Intent intent = getIntent();

        if (intent.hasExtra(TAG_MOVIE_DATA)) {
            Movie movie = (Movie) intent.getSerializableExtra(TAG_MOVIE_DATA);

            if (null != movie)
                setupMovieDetails(movie);
        }
    }

    /**
     * This method binds the movie data to the children views of {@link MovieDetailActivity}.
     * @param movie The movie you want to bind
     */
    private void setupMovieDetails(Movie movie) {
        /* Setup basic movie string data */
        mTitle.setText(movie.getTitle());
        mVoteAverage.setText(String.format(
                Locale.ENGLISH,
                getResources().getString(R.string.movie_detail_vote_format),
                movie.getVoteAverage())
        );
        mSynopsis.setText(movie.getSynopsis());

        /* Display only the year of release date */
        DateFormat format = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        mReleaseDate.setText(format.format(movie.getReleaseDate()));

        /* Fetch movie poster from the database via Picasso */
        URL posterPath = NetworkUtils.buildPosterUrl(movie.getPosterUrl());
        Uri posterUrl = Uri.parse(posterPath.toString());
        Picasso.with(MovieDetailActivity.this).load(posterUrl).into(mPoster);
    }
}
