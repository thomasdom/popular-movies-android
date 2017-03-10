package com.thomasdomingues.popularmovies.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thomasdomingues.popularmovies.R;
import com.thomasdomingues.popularmovies.data.api.MoviesApiClient;
import com.thomasdomingues.popularmovies.data.api.MoviesApiService;
import com.thomasdomingues.popularmovies.data.api.responses.MovieListResponse;
import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.ui.activities.MainActivity;
import com.thomasdomingues.popularmovies.ui.adapters.MovieListAdapter;
import com.thomasdomingues.popularmovies.utilities.PreferenceUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class MovieListFragment extends RxFragment
{
    /* Default number of columns displayed in the grid */
    private static final int GRID_SPAN_COUNT = 2;

    /* Activity listener */
    private OnFragmentInteractionListener mListener;

    /* TMDB API service */
    private MoviesApiService mTmdbApiService;

    /* Movie grid adapter */
    private MovieListAdapter mMovieListAdapter;

    /* Recycler view position */
    private int mPosition = RecyclerView.NO_POSITION;

    /* Child views */
    @BindView(R.id.rv_movie_list)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.tv_error_message_display)
    protected TextView mErrorMessageDisplay;

    @BindView(R.id.pb_loading_indicator)
    protected ProgressBar mLoadingIndicator;

    public MovieListFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieListFragment.
     */
    public static MovieListFragment newInstance()
    {
        return new MovieListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        /* Setup children views of main activity's layout */
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        /* Setup TMDB API service */
        mTmdbApiService = MoviesApiClient.getMoviesApiClientInstance();

        /* Setup adapter */
        mMovieListAdapter = new MovieListAdapter(getContext(), (MainActivity) getActivity());

        /* Setup RecyclerView */
        GridLayoutManager layoutManager =
                new GridLayoutManager(getContext(), GRID_SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        mRecyclerView
                .setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mMovieListAdapter);

        /* Fetch movie list from API */
        fetchMovies();
    }

    // TODO Replace stub by real function
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
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

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * This method fetch movie list by saved criteria in SharedPreferences, right after
     * cleaning the RecyclerView.
     */
    public void fetchMovies()
    {
        /* Setup loading UI */
        showMovieList();
        mLoadingIndicator.setVisibility(View.VISIBLE);

        String sortByCriteria = PreferenceUtils.getUserSortCriteria(getContext());

        mTmdbApiService.discoverMovies(sortByCriteria, 1)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .map(MovieListResponse::getResults)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Movie>>()
                {
                    boolean error = false;

                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        Log.d(TAG, "onSubscribe : " + d.isDisposed());
                    }

                    @Override
                    public void onNext(List<Movie> movies)
                    {
                        mMovieListAdapter.setMovieList(movies);
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
                        mLoadingIndicator.setVisibility(View.INVISIBLE);

                        if (mPosition == RecyclerView.NO_POSITION)
                            mPosition = 0;

                        mRecyclerView.smoothScrollToPosition(mPosition);

                        if (error)
                        {
                            showErrorMessage();
                        } else
                        {
                            showMovieList();
                        }
                    }
                });
    }

    /**
     * This method will make the View for the movie list data visible and
     * hide the error message.
     */
    private void showMovieList()
    {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie list
     * View.
     */
    private void showErrorMessage()
    {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
