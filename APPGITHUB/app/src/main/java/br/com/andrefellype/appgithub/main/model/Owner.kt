package br.com.andrefellype.appgithub.main.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Owner(@SerializedName("id") var id: Int,
                 @SerializedName("avatar_url") var photo: String,
                 @SerializedName("login") var username: String): Serializable