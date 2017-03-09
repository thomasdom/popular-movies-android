package com.thomasdomingues.popularmovies.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.thomasdomingues.popularmovies.data.api.interceptors.AuthInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesApiClient
{
    /* Singleton instances */
    private static OkHttpClient sHttpClient;
    private static MoviesApiService sMoviesApiService;

    /* OkHttp parameters */
    private static final int CONNECT_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 30;

    /* Retrofit parameters */
    public static final String MOVIE_DB_API_URL = "http://api.themoviedb.org/3/";

    private static OkHttpClient getHttpClientInstance()
    {
        if (null == sHttpClient)
        {
            sHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(new AuthInterceptor())
                    .build();
        }
        return sHttpClient;
    }

    public static MoviesApiService getMoviesApiClientInstance()
    {
        if (null == sMoviesApiService) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIE_DB_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getHttpClientInstance())
                    .build();

            sMoviesApiService = retrofit.create(MoviesApiService.class);
        }
        return sMoviesApiService;
    }
}
