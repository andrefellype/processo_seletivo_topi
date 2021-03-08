package br.com.andrefellype.appgithub.main.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GitItem(@SerializedName("id") var id: Int,
                   @SerializedName("name") var name: String,
                   @SerializedName("description") var description: String,
                   @SerializedName("stargazers_count") var stargazers: Int,
                   @SerializedName("forks") var forks: Int,
                   @SerializedName("owner") var owner: Owner): Serializable