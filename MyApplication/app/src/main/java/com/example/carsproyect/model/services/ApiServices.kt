package com.example.carsproyect.model.services

import android.telecom.Call
import com.example.carsproyect.data.model.LoginRequest
import com.example.carsproyect.data.model.LoginResponse
import com.example.carsproyect.model.cars.Car
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServices {


    @GET("cars/")
    suspend fun getCars(): Response<List<Car>>

    @DELETE("cars/{id}/")
    suspend fun deleteCars(@Path("id") id: Int): Response<Unit>

    // Actualizar un auto específico por ID
    @PATCH("cars/{id}/")
    suspend fun updateCars(@Path("id") id: Int, @Body car: Car): Response<Car>

    // Agregar un nuevo auto
    @POST("cars/")
    suspend fun createCars(@Body car: Car): Response<Car>

    @POST("auth/api/token/")
    @Headers("Content-Type:application/json")
    suspend fun getLogin(@Body loginRequest: LoginRequest): LoginResponse

}