package com.thomasdomingues.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thomasdomingues.popularmovies.data.MovieContract;
import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>
{

    public static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_FAVORITE
    };

    public static final int INDEX_MOVIE_TITLE = 0;
    public static final int INDEX_MOVIE_RELEASE_DATE = 1;
    public static final int INDEX_MOVIE_POSTER_PATH = 2;
    public static final int INDEX_MOVIE_SYNOPSIS = 3;
    public static final int INDEX_MOVIE_VOTE_AVERAGE = 4;
    public static final int INDEX_MOVIE_IS_FAVORITE = 5;

    private static final int MOVIE_DETAILS_LOADER_ID = 51;

    private Uri mUri;

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        /* Instantiate child views */
        ButterKnife.bind(this);

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

        setupMovieDetails(movie);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    /**
     * This method binds the movie data to the children views of {@link MovieDetailActivity}.
     *
     * @param movie The movie you want to bind
     */
    private void setupMovieDetails(Movie movie)
    {
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
