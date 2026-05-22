package com.example.datalatte.data.network

import com.example.datalatte.data.network.model.CoffeeDto
import retrofit2.http.GET

/**
 * Interfaz de servicio Retrofit para la API de cafés.
 * Endpoint único: GET /coffee/hot → List<CoffeeDto>
 */
interface CoffeeApiService {

    @GET("coffee/hot")
    suspend fun getHotCoffees(): List<CoffeeDto>
}
