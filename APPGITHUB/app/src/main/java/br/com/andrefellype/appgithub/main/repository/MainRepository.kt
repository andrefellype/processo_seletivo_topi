package br.com.andrefellype.appgithub.main.repository

import br.com.andrefellype.appgithub.helper.ServiceConfig
import br.com.andrefellype.appgithub.main.model.GitItem
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository() {

    interface GitApiCallback {
        fun onSucess(gitItems: List<GitItem>)
        fun onNotConnection()
    }

    var client: GitServiceRetrofit = ServiceConfig.getRetrofit().create(GitServiceRetrofit::class.java)

    suspend fun getList(callback: GitApiCallback){
        return withContext(Dispatchers.Default) {
            client.getList().enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback.onNotConnection()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        var typeList = object : TypeToken<ArrayList<GitItem>>() {}.type
                        var gson: Gson = Gson()
                        var gitItems: ArrayList<GitItem> = gson.fromJson(response.body()!!
                                .getAsJsonArray("items").toString(), typeList)
                        callback.onSucess(gitItems)
                    } else {
                        callback.onNotConnection()
                    }
                }
            })
        }
    }
}