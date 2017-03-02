package com.thomasdomingues.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * This class defines a representation of a TMDB movie.
 */
public class Movie implements Parcelable {

    private String title;
    private Date releaseDate;
    private String posterPath;
    private double voteAverage;
    private String overview;

    private boolean favorite;

    public Movie(String title, Date releaseDate, String posterPath, double voteAverage, String overview, boolean favorite) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.favorite = favorite;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        releaseDate = new Date(in.readLong());
        posterPath = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
        favorite = in.readInt() == 1;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeLong(releaseDate.getTime());
        out.writeString(posterPath);
        out.writeDouble(voteAverage);
        out.writeString(overview);
        out.writeInt(favorite ? 1 : 0);
    }

    public String getTitle() {
        return title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
