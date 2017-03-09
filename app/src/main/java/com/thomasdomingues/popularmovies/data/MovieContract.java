package com.thomasdomingues.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.thomasdomingues.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Movies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        /* Used internally as the name of our movies table. */
        public static final String TABLE_NAME = "movies";

        /* Movie title */
        public static final String COLUMN_TITLE = "title";

        /* Movie release date is stored as a long representing milliseconds since January, 1st 1970 */
        public static final String COLUMN_RELEASE_DATE = "release_date";

        /* Movie poster path as returned by API */
        public static final String COLUMN_POSTER_PATH = "poster_path";

        /* Movie vote average (stored as float in the database) */
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        /* Movie synopsis  */
        public static final String COLUMN_SYNOPSIS = "synopsis";


        /**
         * Builds a URI that adds the movie identifier to the end of the movies content URI path.
         *
         * @param _id Movie identifier in database
         * @return Uri to query details about a single movie entry
         */
        public static Uri buildMovieUriWithId(long _id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(_id))
                    .build();
        }
    }
}
