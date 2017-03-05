/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thomasdomingues.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;

import com.thomasdomingues.popularmovies.data.MovieContract.MovieEntry;

import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

class TestUtilities {

    private static final String COLUMN_TITLE = MovieEntry.COLUMN_TITLE;
    private static final String COLUMN_RELEASE_DATE = MovieEntry.COLUMN_RELEASE_DATE;
    private static final String COLUMN_POSTER_PATH = MovieEntry.COLUMN_POSTER_PATH;
    private static final String COLUMN_SYNOPSIS = MovieEntry.COLUMN_SYNOPSIS;
    private static final String COLUMN_VOTE_AVERAGE = MovieEntry.COLUMN_VOTE_AVERAGE;
    private static final String COLUMN_FAVORITE = MovieEntry.COLUMN_FAVORITE;

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);

            /* Test to see if the column is contained within the cursor */
            String columnNotFoundError = "Column '" + columnName + "' not found. " + error;
            assertFalse(columnNotFoundError, index == -1);

            /* Test to see if the expected value equals the actual value (from the Cursor) */
            String expectedValue = entry.getValue().toString();
            String actualValue = valueCursor.getString(index);

            String valuesDontMatchError = "Actual value '" + actualValue
                    + "' did not match the expected value '" + expectedValue + "'. "
                    + error;

            assertEquals(valuesDontMatchError,
                    expectedValue,
                    actualValue);
        }
    }

    static ContentValues createTestMovieContentValues() {

        ContentValues testMovieValues = new ContentValues();

        testMovieValues.put(COLUMN_TITLE, "Fifty Shades Darker");
        testMovieValues.put(COLUMN_RELEASE_DATE, 1486508400L); // 2017-02-08 00:00:00
        testMovieValues.put(COLUMN_POSTER_PATH, "/7SMCz5724COOYDhY0mj0NfcJqxH.jpg");
        testMovieValues.put(COLUMN_SYNOPSIS, "When a wounded Christian Grey tries to entice a cautious Ana Steele back into his life, she demands a new arrangement before she will give him another chance. As the two begin to build trust and find stability, shadowy figures from Christian’s past start to circle the couple, determined to destroy their hopes for a future together.");
        testMovieValues.put(COLUMN_VOTE_AVERAGE, 6.1);
        testMovieValues.put(COLUMN_FAVORITE, 1);

        return testMovieValues;
    }

    static String studentReadableClassNotFound(ClassNotFoundException e) {
        String message = e.getMessage();
        int indexBeforeSimpleClassName = message.lastIndexOf('.');
        String simpleClassNameThatIsMissing = message.substring(indexBeforeSimpleClassName + 1);
        simpleClassNameThatIsMissing = simpleClassNameThatIsMissing.replaceAll("\\$", ".");
        return "Couldn't find the class "
                + simpleClassNameThatIsMissing
                + ".\nPlease make sure you've created that class.";
    }
}