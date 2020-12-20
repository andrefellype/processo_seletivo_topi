package io.topi.apptopi.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GitServiceRetrofit {
    @Headers("ContentType: application/json")

    @GET("repositories")
    Call<JsonObject> getList(@Query("q") String query, @Query("sort") String sort, @Query("page") String page);
}
