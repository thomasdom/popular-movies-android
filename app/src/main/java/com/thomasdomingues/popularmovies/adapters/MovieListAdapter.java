package com.thomasdomingues.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.thomasdomingues.popularmovies.MainActivity;
import com.thomasdomingues.popularmovies.R;
import com.thomasdomingues.popularmovies.utilities.NetworkUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * {@link MovieListAdapter} exposes a list of movies to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    private final MovieListAdapterOnClickHandler mClickHandler;

    public MovieListAdapter(@NonNull Context context, MovieListAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.movie_list_item, viewGroup, false);

        return new MovieListAdapterViewHolder(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(MovieListAdapterViewHolder movieListAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

        long movieId = mCursor.getLong(MainActivity.INDEX_MOVIE_ID);
        String posterPath = mCursor.getString(MainActivity.INDEX_POSTER_PATH);

        movieListAdapterViewHolder.itemView.setTag(movieId);

        URL currentMoviePosterURL = NetworkUtils.buildPosterUrl(posterPath);
        Uri currentMoviePosterUri = Uri.parse(currentMoviePosterURL.toString());

        Picasso.with(mContext)
                .load(currentMoviePosterUri)
                .into(movieListAdapterViewHolder.mPosterImageView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        if (null == mCursor)
            return 0;
        return mCursor.getCount();
    }

    /**
     * This method is used to set the movie list data on a MovieListAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new MovieListAdapter to display it.
     *
     * @param movieListData The new movie list data to be displayed.
     */
    public void swapCursor(Cursor movieListData) {
        mCursor = movieListData;
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a movie list item.
     */
    class MovieListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_movie_poster)
        ImageView mPosterImageView;

        MovieListAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "Clicked on a movie.");
            long selectedMovieId = (long) view.getTag();
            mClickHandler.onClick(selectedMovieId);
        }
    }

    public interface MovieListAdapterOnClickHandler {
        void onClick(long movieId);
    }
}
