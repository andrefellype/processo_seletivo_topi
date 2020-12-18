package io.topi.apptopi.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface GitServiceRetrofit {
    @Headers("ContentType: application/json")

    @GET("repositories?q=language:Java&sort=stars&page=1")
    Call<JsonObject> getList();
}
