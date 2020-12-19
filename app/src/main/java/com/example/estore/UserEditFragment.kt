package com.example.estore

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.estore.SERVICES.UserService

import kotlinx.android.synthetic.main.fragment_user_edit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class UserEditFragment: Fragment(), Callback<Any> {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onStart() {
        super.onStart()

        val  token = activity?.getSharedPreferences("e-store", 0)?.getString("token", null)


        update.setOnClickListener {
            val nameV = if (TextUtils.isEmpty(name.text.toString())) null else name.text.toString()
            val lastnameV = if (TextUtils.isEmpty(lastname.text.toString())) null else lastname.text.toString()
            val passwordV = if (TextUtils.isEmpty(password.text.toString())) null else lastname.text.toString()

            val user = UserUpdate(name = nameV, lastname = lastnameV, pasword = passwordV )
            Log.d(tag, user.toString())
            if (token != null) UserService.instance.selfUpdate(auth = "Bearer ${token}", user = user).enqueue(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_edit, container, false)


        return view
    }

    override fun onResponse(call: Call<Any>, response: Response<Any>) {
        if(response.isSuccessful) {

            fragmentManager?.beginTransaction()?.replace(R.id.fragment, UserFragment(), "FRAGMENT-ONE")?.commit()

        }else
            {
                val errorMessage = try {
                    "An error occurred: ${response.errorBody()?.string()}"
                } catch (e: IOException) {
                    "An error occurred: error while decoding the error message."
                }

                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                Log.e(tag, errorMessage)
            }

    }

    override fun onFailure(call: Call<Any>, t: Throwable) {
        Log.w(tag, "Error: ${t.message}", t)
    }


}