package com.thomasdomingues.popularmovies.data.api.interceptors;

import com.thomasdomingues.popularmovies.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor
{
    private static final String API_KEY_PARAM = "api_key";

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request originalRequest = chain.request();

        HttpUrl originalHttpUrl = originalRequest.url();
        HttpUrl newHttpUrl = originalHttpUrl.newBuilder()
                .addQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .build();

        Request newRequest = originalRequest.newBuilder()
                .url(newHttpUrl)
                .build();

        return chain.proceed(newRequest);

    }
}
