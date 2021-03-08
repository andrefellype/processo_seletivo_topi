package br.com.andrefellype.appgithub.main.repository

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GitServiceRetrofit {
    @Headers("ContentType: application/json")

    @GET("repositories")
    fun getList(@Query("q") query: String = "language:Java", @Query("sort") sort: String = "stars", @Query("page") page: String = "1"): Call<JsonObject>
}