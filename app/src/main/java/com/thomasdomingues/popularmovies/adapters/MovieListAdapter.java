package com.thomasdomingues.popularmovies.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.thomasdomingues.popularmovies.R;
import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.utilities.NetworkUtils;

import java.net.URL;

/**
 * {@link MovieListAdapter} exposes a list of movies to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {

    private Context mContext;
    private Movie[] mMovieListData;

    /**
     * Cache of the children views for a movie list item.
     */
    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mPosterImageView;

        public MovieListAdapterViewHolder(View view) {
            super(view);
            mPosterImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
        }
    }

    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (null == mContext) {
            mContext = viewGroup.getContext();
        }
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.movie_list_item, viewGroup, false);
        return new MovieListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieListAdapterViewHolder movieListAdapterViewHolder, int position) {
        if (null != mContext) {
            Movie currentMovie = mMovieListData[position];
            URL currentMoviePosterURL = NetworkUtils.buildPosterUrl(currentMovie.getPosterUrl());
            Uri currentMoviePosterUri = Uri.parse(currentMoviePosterURL.toString());
            Picasso.with(mContext)
                    .load(currentMoviePosterUri)
                    .into(movieListAdapterViewHolder.mPosterImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mMovieListData)
            return 0;
        return mMovieListData.length;
    }

    /**
     * This method is used to set the movie list data on a MovieListAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new MovieListAdapter to display it.
     *
     * @param movieListData The new movie list data to be displayed.
     */
    public void setMovieListData(Movie[] movieListData) {
        mMovieListData = movieListData;
        notifyDataSetChanged();
    }
}
