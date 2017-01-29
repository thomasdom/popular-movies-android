package com.thomasdomingues.popularmovies.models;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable {
    private String title;
    private Date releaseDate;
    private String posterUrl;
    private double voteAverage;
    private String synopsis;

    public Movie(String title, Date releaseDate, String posterUrl, double voteAverage, String synopsis) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.voteAverage = voteAverage;
        this.synopsis = synopsis;
    }

    public String getTitle() {
        return title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getSynopsis() {
        return synopsis;
    }
}
