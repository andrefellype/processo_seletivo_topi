package io.topi.apptopi.main.repositorio

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GitServiceRetrofit {
    @Headers("ContentType: application/json")

    @GET("repositories")
    fun getList(@Query("q") query: String, @Query("sort") sort: String, @Query("page") page: String): Call<JsonObject>
}
