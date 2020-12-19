package com.example.estore.SERVICES

import com.example.estore.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object CartService {
    interface RestApi {

        companion object {
            // AVD emulator
            const val URL = "http://10.0.2.2:8000/api/"

        }

        @GET("user/self/shoppingCart")
        fun getCart(@Header("Authorization") auth: String): Call<List<CartItem>>

        @PUT("user/self/shoppingCart")
        fun updateCart(@Header("Authorization") auth: String, @Body shoppingCart: ShoppingCart): Call<List<CartItem>>

        @POST("user/self/order")
        fun buy(@Header("Authorization") auth: String, @Body order: Order): Call<Any>

        @GET("order")
        fun getOrders(@Header("Authorization") auth: String): Call<Orders>
    }

    val instance: RestApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(RestApi.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        retrofit.create(RestApi::class.java)
    }
}