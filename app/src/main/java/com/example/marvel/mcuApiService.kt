package com.example.marvel

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface McuApiService {
    @GET("api")
    suspend fun getNextMovie(@Query("date") date: String? = null): Movie
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://www.whenisthenextmcufilm.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val mcuApiService: McuApiService = retrofit.create(McuApiService::class.java)
