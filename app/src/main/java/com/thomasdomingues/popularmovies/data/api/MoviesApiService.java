package com.thomasdomingues.popularmovies.data.api;

import com.thomasdomingues.popularmovies.data.api.responses.MovieListResponse;
import com.thomasdomingues.popularmovies.data.api.responses.ReviewListResponse;
import com.thomasdomingues.popularmovies.data.api.responses.VideoListResponse;

import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApiService
{
    @GET("movie/{sort_by}")
    Observable<MovieListResponse> discoverMovies(
            @Path("sort_by") String sort,
            @Query("page") int page);

    @GET("movie/{id}/videos")
    Observable<VideoListResponse> videos(
            @Path("id") long movieId);

    @GET("movie/{id}/reviews")
    Observable<ReviewListResponse> reviews(
            @Path("id") long movieId,
            @Query("page") int page);
}
