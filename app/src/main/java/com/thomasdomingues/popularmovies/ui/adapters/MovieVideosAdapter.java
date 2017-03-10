package com.thomasdomingues.popularmovies.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thomasdomingues.popularmovies.R;
import com.thomasdomingues.popularmovies.models.Video;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosAdapter.MovieVideosAdapterViewHolder>
{
    private Context mContext;
    private List<Video> mVideos;

    private final MovieVideosAdapterOnClickHandler mClickHandler;

    public MovieVideosAdapter(@NonNull Context context, MovieVideosAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MovieVideosAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.movie_trailer_list_item, viewGroup, false);

        return new MovieVideosAdapterViewHolder(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(MovieVideosAdapterViewHolder movieVideosAdapterViewHolder, int position) {
        Video video = mVideos.get(position);

        movieVideosAdapterViewHolder.itemView.setTag(video);

        movieVideosAdapterViewHolder.mTrailerLabelTv.setText(video.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        if (null == mVideos)
            return 0;
        return mVideos.size();
    }

    /**
     * This method is used to set the movie videos list data on adapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new adapter to display it.
     *
     * @param videoList The new movie videos list data to be displayed.
     */
    public void setVideoList(List<Video> videoList) {
        mVideos = videoList;
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a movie trailer list item.
     */
    class MovieVideosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_trailer_label)
        TextView mTrailerLabelTv;

        MovieVideosAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Video selectedTrailer = (Video) view.getTag();
            Log.d(TAG, "Clicked on video: " + selectedTrailer.getName());
            mClickHandler.onSelectedTrailer(selectedTrailer);
        }
    }

    public interface MovieVideosAdapterOnClickHandler {
        void onSelectedTrailer(Video video);
    }
}
