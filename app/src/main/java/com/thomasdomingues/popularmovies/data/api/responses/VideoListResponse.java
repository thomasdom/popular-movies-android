package com.thomasdomingues.popularmovies.data.api.responses;

import com.google.gson.annotations.SerializedName;
import com.thomasdomingues.popularmovies.models.Video;

import java.util.ArrayList;

public class VideoListResponse
{
    @SerializedName("id")
    private long movieId;

    @SerializedName("results")
    private ArrayList<Video> results;

    public VideoListResponse(long movieId, ArrayList<Video> results) {
        this.movieId = movieId;
        this.results = results;
    }

    public long getMovieId() {
        return movieId;
    }

    public ArrayList<Video> getResults() {
        return results;
    }
}
