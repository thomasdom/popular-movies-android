package com.thomasdomingues.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie implements Parcelable
{
    @SerializedName("poster_path")
    private String posterPath;

    private boolean adult;

    private String overview;

    @SerializedName("release_date")
    private Date releaseDate;

    @SerializedName("genre_ids")
    private List<Integer> genreIds = null;

    private int id;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("original_language")
    private String originalLanguage;

    private String title;

    @SerializedName("backdrop_path")
    private String backdropPath;

    private double popularity;

    @SerializedName("vote_count")
    private int voteCount;

    private boolean video;

    @SerializedName("vote_average")
    private double voteAverage;

    /**
     * @param id               TMDB movie identifier
     * @param genreIds         TMDB movie genres identifier list
     * @param title            Movie title
     * @param releaseDate      Movie release date
     * @param overview         Movie synopsis
     * @param posterPath       Movie poster TMDB path
     * @param originalTitle    Movie original title
     * @param voteAverage      TMDB vote average for movie
     * @param originalLanguage Movie original language code
     * @param adult            Defines if the movie is intended to an adult audience
     * @param backdropPath     Movie backdrop TMDB path
     * @param voteCount        TMDB vote count for movie
     * @param video            Checks if movie has videos
     * @param popularity       TMDB popularity score for movie
     */
    public Movie(String posterPath, boolean adult, String overview, Date releaseDate, List<Integer> genreIds, int id, String originalTitle, String originalLanguage, String title, String backdropPath, double popularity, int voteCount, boolean video, double voteAverage)
    {
        super();
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in)
    {
        posterPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = new Date(in.readLong());
        genreIds = new ArrayList<>();
        in.readList(genreIds, Integer.class.getClassLoader());
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        video = in.readByte() != 0;
        voteAverage = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>()
    {
        @Override
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };

    public String getPosterPath()
    {
        return posterPath;
    }

    public void setPosterPath(String posterPath)
    {
        this.posterPath = posterPath;
    }

    public boolean getAdult()
    {
        return adult;
    }

    public void setAdult(boolean adult)
    {
        this.adult = adult;
    }

    public String getOverview()
    {
        return overview;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public Date getReleaseDate()
    {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds()
    {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds)
    {
        this.genreIds = genreIds;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getOriginalTitle()
    {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle)
    {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage()
    {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage)
    {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getBackdropPath()
    {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath)
    {
        this.backdropPath = backdropPath;
    }

    public double getPopularity()
    {
        return popularity;
    }

    public void setPopularity(double popularity)
    {
        this.popularity = popularity;
    }

    public int getVoteCount()
    {
        return voteCount;
    }

    public void setVoteCount(int voteCount)
    {
        this.voteCount = voteCount;
    }

    public boolean getVideo()
    {
        return video;
    }

    public void setVideo(boolean video)
    {
        this.video = video;
    }

    public double getVoteAverage()
    {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage)
    {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(posterPath);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeLong(releaseDate.getTime());
        parcel.writeList(genreIds);
        parcel.writeInt(id);
        parcel.writeString(originalTitle);
        parcel.writeString(originalLanguage);
        parcel.writeString(title);
        parcel.writeString(backdropPath);
        parcel.writeDouble(popularity);
        parcel.writeInt(voteCount);
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeDouble(voteAverage);
    }
}