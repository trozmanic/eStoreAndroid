package com.example.estore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.estore.ADAPTERS.OrdersAdapter
import com.example.estore.SERVICES.CartService
import com.example.estore.SERVICES.UserService
import com.example.estore.dummy.DummyContent
import kotlinx.android.synthetic.main.fragment_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


/**
 * A fragment representing a list of Items.
 */
class UserFragment : Fragment(), Callback<User> {

    private lateinit var adapter: OrdersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onStart() {
        super.onStart()

        val token = activity?.getSharedPreferences("e-store", 0)?.getString("token", null)
        if (token != null) UserService.instance.self("Bearer ${token}").enqueue(this)

        edit.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment, UserEditFragment(), "FRAGMENT-TWO")?.commit()
        }

        logout.setOnClickListener{
            activity!!.getSharedPreferences("e-store", Context.MODE_PRIVATE).edit().putString("token", null).apply()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
        adapter = OrdersAdapter(context!!)
        orders.adapter = adapter


        if (token != null) CartService.instance.getOrders("Bearer $token").enqueue(object : Callback<Orders>{
            override fun onResponse(call: Call<Orders>, response: Response<Orders>) {

                if(response.isSuccessful){
                    val orders = response.body()
                    Log.d(tag, orders.toString())
                    adapter.clear()
                    adapter.addAll(orders!!.orders)
                }else{
                    val errorMessage = try {
                        "An error occurred: ${response.errorBody()?.string()}"
                    } catch (e: IOException) {
                        "An error occurred: error while decoding the error message."
                    }

                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.e(tag, errorMessage)
                }
            }

            override fun onFailure(call: Call<Orders>, t: Throwable) {
                Log.w(tag, "Error: ${t.message}", t)
            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)


        return view
    }

    override fun onResponse(call: Call<User>, response: Response<User>) {
        if(response.isSuccessful) {
            val user = response.body()
            if(user != null){
                name.text = "Name: ${user.name}"
                lastname.text = "Last name: ${user.lastname}"
                email.text = "E-mail: ${user.email}"
            }

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

    override fun onFailure(call: Call<User>, t: Throwable) {
        Log.w(tag, "Error: ${t.message}", t)
    }





}