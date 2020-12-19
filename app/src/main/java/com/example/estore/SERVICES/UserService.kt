package com.example.estore.SERVICES

import com.example.estore.Login
import com.example.estore.LoginResponse
import com.example.estore.User
import com.example.estore.UserUpdate
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object UserService {

    interface RestApi {

        companion object {
            // AVD emulator
            const val URL = "http://10.0.2.2:8000/api/"

        }

        @POST("login")
        fun login(@Body login: Login): Call<LoginResponse>

        @GET("user/self")
        fun self(@Header("Authorization") auth: String): Call<User>

        @PUT("user/self")
        fun selfUpdate(@Header("Authorization") auth: String, @Body user:UserUpdate): Call<Any>
    }

    val instance: RestApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(ItemService.RestApi.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RestApi::class.java)
    }
}