package com.thomasdomingues.popularmovies.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thomasdomingues.popularmovies.R;
import com.thomasdomingues.popularmovies.data.api.MoviesApiClient;
import com.thomasdomingues.popularmovies.data.api.MoviesApiService;
import com.thomasdomingues.popularmovies.data.api.responses.VideoListResponse;
import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.models.Video;
import com.thomasdomingues.popularmovies.ui.adapters.MovieVideosAdapter;
import com.thomasdomingues.popularmovies.utilities.NetworkUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends RxFragment implements
        MovieVideosAdapter.MovieVideosAdapterOnClickHandler
{
    private static final String EXTRA_MOVIE = "movie_tag";

    private Movie mMovie;

    private MoviesApiService mTmdbApiService;

    private OnFragmentInteractionListener mListener;

    /* Movie trailers */
    @BindView(R.id.rv_movie_trailers)
    protected RecyclerView mTrailerListRv;

    @BindView(R.id.pb_movie_trailers_loading)
    protected ProgressBar mTrailerListLoadingIndicator;

    @BindView(R.id.tv_error_movie_trailers)
    protected TextView mTrailerListErrorTv;

    private MovieVideosAdapter mTrailerListAdapter;

    private int mTrailerListPosition = RecyclerView.NO_POSITION;

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

    public MovieDetailFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie The TMDB movie from which the user wants details.
     * @return A new instance of fragment MovieDetailFragment.
     */
    public static MovieDetailFragment newInstance(Movie movie)
    {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mMovie = getArguments().getParcelable(EXTRA_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        /* Instantiate child views */
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mTmdbApiService = MoviesApiClient.getMoviesApiClientInstance();

        /* Setup adapter */
        mTrailerListAdapter = new MovieVideosAdapter(getContext(), this);

        /* Setup RecyclerView */
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mTrailerListRv.setLayoutManager(layoutManager);

        mTrailerListRv.setHasFixedSize(true);

        mTrailerListRv.setAdapter(mTrailerListAdapter);

        if (null != mMovie) {
            setupMovieDetails(mMovie);
            fetchMovieTrailers();
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSelectedTrailer(Video video)
    {
        if (video != null && video.getSite().equalsIgnoreCase("youtube")) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
            startActivity(intent);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This method binds the movie data to the children views of {@link MovieDetailFragment}.
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
        Picasso.with(getContext()).load(posterUrl).into(mPoster);
    }

    public void fetchMovieTrailers()
    {
        /* Setup loading UI */
        showTrailerList();
        mTrailerListLoadingIndicator.setVisibility(View.VISIBLE);

        mTmdbApiService.videos(mMovie.getId())
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .map(VideoListResponse::getResults)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Video>>()
                {
                    boolean error = false;

                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        Log.d(TAG, "onSubscribe : " + d.isDisposed());
                    }

                    @Override
                    public void onNext(List<Video> videos)
                    {
                        mTrailerListAdapter.setVideoList(videos);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.e(TAG, e.getMessage());
                        error = true;
                    }

                    @Override
                    public void onComplete()
                    {
                        mTrailerListLoadingIndicator.setVisibility(View.INVISIBLE);

                        if (mTrailerListPosition == RecyclerView.NO_POSITION)
                            mTrailerListPosition = 0;

                        mTrailerListRv.smoothScrollToPosition(mTrailerListPosition);

                        if (error)
                        {
                            showTrailerListErrorMessage();
                        } else
                        {
                            showTrailerList();
                        }
                    }
                });
    }

    private void showTrailerList()
    {
        mTrailerListRv.setVisibility(View.VISIBLE);
        mTrailerListErrorTv.setVisibility(View.GONE);
    }

    private void showTrailerListErrorMessage()
    {
        mTrailerListRv.setVisibility(View.INVISIBLE);
        mTrailerListErrorTv.setVisibility(View.VISIBLE);
    }
}
