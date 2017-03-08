package com.thomasdomingues.popularmovies.data.api;

import com.thomasdomingues.popularmovies.models.Genre;
import com.thomasdomingues.popularmovies.models.Movie;
import com.thomasdomingues.popularmovies.models.Review;
import com.thomasdomingues.popularmovies.models.Video;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi
{
    @GET("/genre/movie/list") Genre[] genres();

    @GET("/discover/movie") Movie[] discoverMovies(
            @Query("sort_by") String sort,
            @Query("page") int page);

    @GET("/movie/{id}/videos") Video[] videos(
            @Path("id") long movieId);

    @GET("/movie/{id}/reviews") Review[] reviews(
            @Path("id") long movieId,
            @Query("page") int page);
}
