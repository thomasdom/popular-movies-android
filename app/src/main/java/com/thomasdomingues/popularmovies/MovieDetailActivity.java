package com.thomasdomingues.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String TAG_MOVIE_DATA = "movie_data";

    /* Child views */
    @BindView(R.id.iv_movie_detail_poster)
    protected ImageView mPoster;

    @BindView(R.id.tv_movie_detail_title)
    protected TextView mTitle;

    @BindView(R.id.tv_movie_detail_release_date)
    protected TextView mReleaseDate;

    @BindView(R.id.tv_movie_detail_vote_average)
    protected TextView mVoteAverage;

    @BindView(R.id.tv_movie_detail_synopsis)
    protected TextView mSynopsis;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        /* Instantiate child views */
        ButterKnife.bind(this);

        /* Get intent passed movie and display details */
        Intent intent = getIntent();

        if (intent.hasExtra(TAG_MOVIE_DATA)) {
            Movie movie = intent.getParcelableExtra(TAG_MOVIE_DATA);

            if (null != movie)
                setupMovieDetails(movie);
        }
    }

    /**
     * This method binds the movie data to the children views of {@link MovieDetailActivity}.
     *
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
        mSynopsis.setText(movie.getOverview());

        /* Display only the year of release date */
        DateFormat format = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        mReleaseDate.setText(format.format(movie.getReleaseDate()));

        /* Fetch movie poster from the database via Picasso */
        URL posterPath = NetworkUtils.buildPosterUrl(movie.getPosterPath());
        Uri posterUrl = Uri.parse(posterPath.toString());
        Picasso.with(MovieDetailActivity.this).load(posterUrl).into(mPoster);
    }
}
