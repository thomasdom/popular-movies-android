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
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

import static com.thomasdomingues.popularmovies.TestUtilities.studentReadableClassNotFound;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class TestMoviesDatabase {

    private final Context context = InstrumentationRegistry.getTargetContext();

    private static final String packageName = "com.thomasdomingues.popularmovies";
    private static final String dataPackageName = packageName + ".data";

    private Class movieDbHelperClass;
    private static final String movieDbHelperName = ".MovieDbHelper";

    private static final String REFLECTED_DATABASE_NAME = "movies.db";
    private static final String REFLECTED_TABLE_NAME = "movies";

    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;

    @Before
    public void before() {
        try {

            movieDbHelperClass = Class.forName(dataPackageName + movieDbHelperName);

            Constructor weatherDbHelperCtor = movieDbHelperClass.getConstructor(Context.class);

            dbHelper = (SQLiteOpenHelper) weatherDbHelperCtor.newInstance(context);

            context.deleteDatabase(REFLECTED_DATABASE_NAME);

            Method getWritableDatabase = SQLiteOpenHelper.class.getDeclaredMethod("getWritableDatabase");
            database = (SQLiteDatabase) getWritableDatabase.invoke(dbHelper);

        } catch (ClassNotFoundException e) {
            fail(studentReadableClassNotFound(e));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        }
    }

    /**
     * This method tests that our database contains all of the tables that we think it should
     * contain. Although in our case, we just have one table that we expect should be added
     * <p>
     * {@link com.thomasdomingues.popularmovies.data.MovieContract.MovieEntry#TABLE_NAME}.
     * <p>
     * Despite only needing to check one table name in Popular Movies, we set this method up so that
     * you can use it in other apps to test databases with more than one table.
     */
    @Test
    public void testCreateDb() {
        final HashSet<String> tableNameHashSet = new HashSet<>();

        tableNameHashSet.add(REFLECTED_TABLE_NAME);

        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals(databaseIsNotOpen,
                true,
                database.isOpen());

        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'",
                null);

        String errorInCreatingDatabase =
                "Error: This means that the database has not been created correctly";
        assertTrue(errorInCreatingDatabase,
                tableNameCursor.moveToFirst());

        do {
            tableNameHashSet.remove(tableNameCursor.getString(0));
        } while (tableNameCursor.moveToNext());

        assertTrue("Error: Your database was created without the expected tables.",
                tableNameHashSet.isEmpty());

        tableNameCursor.close();
    }

    @Test
    public void testInsertSingleRecordIntoWeatherTable() {

        /* Obtain movie values from TestUtilities */
        ContentValues testMovieValues = TestUtilities.createTestMovieContentValues();

        /* Insert ContentValues into database and get a row ID back */
        long movieRowId = database.insert(
                REFLECTED_TABLE_NAME,
                null,
                testMovieValues);

        /* If the insert fails, database.insert returns -1 */
        int valueOfIdIfInsertFails = -1;
        String insertFailed = "Unable to insert into the database";
        assertNotSame(insertFailed,
                valueOfIdIfInsertFails,
                movieRowId);

        Cursor movieCursor = database.query(
                REFLECTED_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        String emptyQueryError = "Error: No Records returned from weather query";
        assertTrue(emptyQueryError,
                movieCursor.moveToFirst());

        String expectedMovieDidntMatchActual =
                "Expected weather values didn't match actual values.";
        TestUtilities.validateCurrentRecord(expectedMovieDidntMatchActual,
                movieCursor,
                testMovieValues);

        assertFalse("Error: More than one record returned from weather query",
                movieCursor.moveToNext());

        movieCursor.close();
    }
}