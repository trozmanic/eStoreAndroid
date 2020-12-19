package com.example.estore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.estore.ADAPTERS.CartAdapter
import com.example.estore.SERVICES.CartService
import kotlinx.android.synthetic.main.activity_shopping_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ShoppingCartActivity : AppCompatActivity(), Callback<List<CartItem>> {

    private lateinit var adapter: CartAdapter

    private val tag = "CART"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)

        adapter = CartAdapter(context = this)

        cartList.adapter = adapter

        val token = this.getSharedPreferences("e-store", 0).getString("token", null)
        if (token != null)  CartService.instance.getCart("Bearer $token").enqueue(this)

        cartRefresh.setOnRefreshListener {
            if (token != null)  CartService.instance.getCart("Bearer $token").enqueue(this)
        }

        update.setOnClickListener {
            val listItems = ArrayList<CartItemUpdate>()
            for (item in adapter.dataSet()) {
                if (item.pivot.quantity > 0) listItems.add(CartItemUpdate(id = item.pivot.store_item_id, quantity = item.pivot.quantity))
            }

            if(token != null) CartService.instance.updateCart("Bearer $token",  ShoppingCart(shoppingCart = listItems)).enqueue(this)
        }

        buy.setOnClickListener {
            val listItems = ArrayList<OrderItem>()
            for (item in adapter.dataSet()) {
                if (item.pivot.quantity > 0) listItems.add(OrderItem(id = item.pivot.store_item_id, quantity = item.pivot.quantity, price = item.price))
            }
            CartService.instance.buy("Bearer $token", Order(orderItems = listItems)).enqueue(object : Callback<Any>{
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if(response.isSuccessful){
                        startActivity(Intent(applicationContext, UserActivity::class.java))
                    }else{
                        val errorMessage = try {
                            "An error occurred: ${response.errorBody()?.string()}"
                        } catch (e: IOException) {
                            "An error occurred: error while decoding the error message."
                        }

                        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
                        Log.e(tag, errorMessage)
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Log.w(tag, "Error: ${t.message}", t)
                }

            })
            CartService.instance.updateCart("Bearer $token",  ShoppingCart(shoppingCart = emptyList())).enqueue(this)
        }

    }

    override fun onResponse(call: Call<List<CartItem>>, response: Response<List<CartItem>>) {
        if (response.isSuccessful){

            adapter.clear()
            response.body()?.let { adapter.addAll(it) }
        }else{
            val errorMessage = try {
                "An error occurred: ${response.errorBody()?.string()}"
            } catch (e: IOException) {
                "An error occurred: error while decoding the error message."
            }

            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            Log.e(tag, errorMessage)
        }
        cartRefresh.isRefreshing = false
    }

    override fun onFailure(call: Call<List<CartItem>>, t: Throwable) {
        Log.w(tag, "Error: ${t.message}", t)
        cartRefresh.isRefreshing = false
    }
}