package com.thomasdomingues.popularmovies.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thomasdomingues.popularmovies.R;
import com.thomasdomingues.popularmovies.data.MovieContract;
import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.ui.activities.MainActivity;
import com.thomasdomingues.popularmovies.ui.adapters.MovieListAdapter;
import com.thomasdomingues.popularmovies.utilities.DataUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteMovieListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = FavoriteMovieListFragment.class.getSimpleName();

    /* Default number of columns displayed in the grid */
    private static final int GRID_SPAN_COUNT = 2;

    /* Activity listener */
    private OnFragmentInteractionListener mListener;

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

    public static final String[] FAVORITE_MOVIES_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_TMDB_ID
    };

    private static final int FAVORITE_MOVIES_LOADER_ID = 51;

    private Cursor mCursor;

    public FavoriteMovieListFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieListFragment.
     */
    public static FavoriteMovieListFragment newInstance()
    {
        return new FavoriteMovieListFragment();
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
        getActivity().getSupportLoaderManager().initLoader(FAVORITE_MOVIES_LOADER_ID, null, this);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        getActivity().getSupportLoaderManager().restartLoader(FAVORITE_MOVIES_LOADER_ID, null, this);
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

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args)
    {
        switch (loaderId)
        {
            case FAVORITE_MOVIES_LOADER_ID:
                return new CursorLoader(getContext(),
                        MovieContract.MovieEntry.CONTENT_URI,
                        FAVORITE_MOVIES_PROJECTION,
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
        mCursor = data;

        List<Movie> movies = DataUtils.cursorToMovieList(mCursor);

        if (mPosition == RecyclerView.NO_POSITION) {
            mPosition = 0;
        }

        mRecyclerView.smoothScrollToPosition(mPosition);

        if (null != movies && movies.size() > 0)
        {
            mMovieListAdapter.setMovieList(movies);
            showMovieList();
        }
        else
            showErrorMessage();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mCursor = null;
        mMovieListAdapter.setMovieList(new ArrayList<>());
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
