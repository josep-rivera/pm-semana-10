package com.tecsup.pm_semana_10.data.remote

import com.tecsup.pm_semana_10.data.model.User
import retrofit2.http.GET


interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}