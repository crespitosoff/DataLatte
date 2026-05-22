package com.example.datalatte.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton que provee la instancia de Retrofit configurada.
 * Base URL: https://api.sampleapis.com/
 * Converter: Gson (deserialización automática JSON → CoffeeDto)
 */
object RetrofitInstance {

    private const val BASE_URL = "https://api.sampleapis.com/"

    val api: CoffeeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoffeeApiService::class.java)
    }
}
