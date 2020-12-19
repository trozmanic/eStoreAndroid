package com.example.estore.SERVICES

import com.example.estore.Item
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object ItemService {

    interface RestApi {

        companion object {
            // AVD emulator
            const val URL = "http://10.0.2.2:8000/api/"

        }

        @GET("storeItem")
        fun getAll(): Call<List<Item>>

        @GET("storeItem/{id}")
        fun get(@Path("id") id: Int): Call<Item>
    }

    val instance: RestApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(RestApi.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RestApi::class.java)
    }
}