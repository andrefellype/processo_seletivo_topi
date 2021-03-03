package io.topi.apptopi.main.model

import java.io.Serializable

data class GitItem(var id: Int, var name: String, var description: String, var stargazers_count: Int,
              var forks: Int, var owner: Owner): Serializable {}