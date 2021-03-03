package io.topi.apptopi.main.repositorio

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import io.topi.apptopi.main.model.GitItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GitService {

    interface GitApiCallback {
        fun onSucess(gitItems: ArrayList<GitItem>)
        fun onNotConnection()
    }

    companion object Factory {
        fun getGitApi(query: String, sort: String, page: String, gitApiCallback: GitApiCallback) {
            ServiceConfig.getRetrofit().create(GitServiceRetrofit::class.java).getList(query, sort, page)
                    .enqueue(object : Callback<JsonObject> {
                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                            if (response.isSuccessful) {
                                var typeList = object : TypeToken<ArrayList<GitItem>>() {
                                }.type
                                var gson: Gson = Gson()
                                var gitItems: ArrayList<GitItem> = gson.fromJson(response.body()!!
                                        .getAsJsonArray("items").toString(), typeList)
                                gitApiCallback.onSucess(gitItems)
                            }
                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            gitApiCallback.onNotConnection()
                        }
                    })
        }
    }
}