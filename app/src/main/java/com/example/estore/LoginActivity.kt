package com.example.estore

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.estore.SERVICES.UserService
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LoginActivity : AppCompatActivity(), Callback<LoginResponse> {

    val tag = "LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()

            UserService.instance.login(Login(email=email, password = password)).enqueue(this)
        }
    }

    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
        if(response.isSuccessful){
            if (response.body() is LoginResponse){
                this.getSharedPreferences("e-store", Context.MODE_PRIVATE).edit().putString("token", response.body()!!.token).apply()
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            }
        }else{
            val errorMessage = try {
                "An error occurred: ${response.errorBody()?.string()}"
            } catch (e: IOException) {
                "An error occurred: error while decoding the error message."
            }

            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            Log.e(tag, errorMessage)
        }


    }

    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
        Log.w(tag, "Error: ${t.message}", t)
    }
}