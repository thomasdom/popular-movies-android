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
import com.thomasdomingues.popularmovies.models.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsAdapterViewHolder>
{
    private Context mContext;
    private List<Review> mReviews;

    private final MovieReviewsAdapterOnClickHandler mClickHandler;

    public MovieReviewsAdapter(@NonNull Context context, MovieReviewsAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MovieReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.movie_review_list_item, viewGroup, false);

        return new MovieReviewsAdapterViewHolder(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(MovieReviewsAdapterViewHolder movieReviewsAdapterViewHolder, int position) {
        Review review = mReviews.get(position);

        movieReviewsAdapterViewHolder.itemView.setTag(review);

        movieReviewsAdapterViewHolder.mReviewAuthorTv.setText(review.getAuthor());
        movieReviewsAdapterViewHolder.mReviewContentTv.setText(review.getContent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        if (null == mReviews)
            return 0;
        return mReviews.size();
    }

    /**
     * This method is used to set the movie reviews list data on adapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new adapter to display it.
     *
     * @param reviewList The new movie videos list data to be displayed.
     */
    public void setReviewList(List<Review> reviewList) {
        mReviews = reviewList;
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a movie trailer list item.
     */
    class MovieReviewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_review_author)
        TextView mReviewAuthorTv;

        @BindView(R.id.tv_review_content)
        TextView mReviewContentTv;

        MovieReviewsAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Review selectedReview = (Review) view.getTag();
            Log.d(TAG, "Clicked on review from author: " + selectedReview.getAuthor());
            mClickHandler.onSelectedReview(selectedReview);
        }
    }

    public interface MovieReviewsAdapterOnClickHandler {
        void onSelectedReview(Review review);
    }
}
