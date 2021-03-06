package com.uai.uaigas.api

import com.uai.uaigas.dto.EmailDTO
import com.uai.uaigas.model.*
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @POST("/usuario")
    fun createUser(@Body user: User): Call<User>

    @POST("/usuario/login")
    fun login(@Body user: User): Call<User>

    @POST("/usuario/forgot")
    fun forgot(@Body email: EmailDTO): Call<Void>

    @PUT("/usuario")
    fun updateUser(@Body user: User): Call<User>

    @GET("/combustivel")
    fun findAllFuel(): Call<List<Combustivel>>

    @POST("/combustivel")
    fun createFuel(@Body combustivel: Combustivel): Call<Combustivel>

    @PUT("/combustivel/{id}")
    fun updateFuel(@Path("id") id: Long, @Body combustivel: Combustivel): Call<Combustivel>

    @DELETE("/combustivel/{id}")
    fun deleteFuel(@Path("id") id: Long): Call<Void>

    @GET("/tipo-combustivel")
    fun findAllFuelType(): Call<List<TipoCombustivel>>

    @POST("/tipo-combustivel")
    fun createFuelType(@Body tipoCombustivel: TipoCombustivel): Call<TipoCombustivel>

    @PUT("/tipo-combustivel/{id}")
    fun updateFuelType(@Path("id") id: Long, @Body tipoCombustivel: TipoCombustivel): Call<TipoCombustivel>

    @DELETE("/tipo-combustivel/{id}")
    fun deleteFuelType(@Path("id") id: Long): Call<Void>

    @GET("/posto/endereco/{city}")
    fun findGasStationByCidade(@Path("city") city: String): Call<List<Endereco>>

    @DELETE("/posto/{id}")
    fun deleteGasStation(@Path("id") id: Long): Call<Void>

    @GET("/posto/{id}")
    fun findGasStation(@Path("id") id: Int): Call<Posto>
    @POST
    fun insertGasStation(@Body posto: Posto): Call<Posto>
    @PUT("/posto/{id}")
    fun updateGasStation(@Path("id") id: Int, @Body posto: Posto): Call<Posto>

    @GET("/endereco/{id}")
    fun findAddress(@Path("id") id: Int): Call<Endereco>
    @POST
    fun insertAddress(@Body endereco: Endereco): Call<Endereco>
    @PUT("/endereco/{id}")
    fun updateAddress(@Path("id") id: Int, @Body endereco: Endereco): Call<Endereco>
    @GET("/endereco")
    fun findAllAddress(): Call<List<Endereco>>
}