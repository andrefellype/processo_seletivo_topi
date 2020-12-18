package io.topi.apptopi.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceConfig {

    public static Retrofit getRetrofit(){
        String url = "https://api.github.com/search/";

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS).connectTimeout(25, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true).addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();
                        HttpUrl url = originalHttpUrl.newBuilder().build();
                        Request.Builder requestBuilder = original.newBuilder().url(url);
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit retrofitClient = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofitClient;
    }
}
