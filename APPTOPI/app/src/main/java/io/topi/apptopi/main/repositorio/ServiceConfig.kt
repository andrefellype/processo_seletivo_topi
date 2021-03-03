package io.topi.apptopi.main.repositorio

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceConfig {

    companion object Factory {
        fun getRetrofit(): Retrofit {
            val url: String = "https://api.github.com/search/";
            var client: OkHttpClient = OkHttpClient.Builder().connectTimeout(25, TimeUnit.SECONDS)
                    .writeTimeout(25, TimeUnit.SECONDS).connectTimeout(25, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true).addInterceptor { chain ->
                        var original: Request = chain.request()
                        var originalHttpUrl: HttpUrl = original.url()
                        var url: HttpUrl = originalHttpUrl.newBuilder().build()
                        var requestBuilder: Request.Builder = original.newBuilder().url(url)
                        var request: Request = requestBuilder.build()
                        chain.proceed(request)
                    }.build()

            var retrofitClient: Retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

            return retrofitClient
        }
    }
}