package com.uai.uaigas.api

import com.uai.uaigas.dto.EmailDTO
import com.uai.uaigas.model.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Api {
    @POST("/usuario")
    fun createUser(@Body user: UserModel): Call<UserModel>

    @POST("/usuario/login")
    fun login(@Body user: UserModel): Call<UserModel>

    @POST("/usuario/forgot")
    fun forgot(@Body email: EmailDTO): Call<Void>

    @PUT("/usuario")
    fun updateUser(@Body user: UserModel): Call<UserModel>
}