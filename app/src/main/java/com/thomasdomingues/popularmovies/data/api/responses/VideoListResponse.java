package com.thomasdomingues.popularmovies.data.api.responses;

import com.google.gson.annotations.SerializedName;
import com.thomasdomingues.popularmovies.models.Video;

import java.util.List;

public class VideoListResponse
{
    @SerializedName("id")
    private long movieId;

    @SerializedName("results")
    private List<Video> results;

    public VideoListResponse(long movieId, List<Video> results) {
        this.movieId = movieId;
        this.results = results;
    }

    public long getMovieId() {
        return movieId;
    }

    public List<Video> getResults() {
        return results;
    }
}
