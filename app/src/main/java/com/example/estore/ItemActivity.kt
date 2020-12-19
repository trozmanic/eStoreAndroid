package com.example.estore

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.estore.ADAPTERS.ImagesAdapter

import com.example.estore.SERVICES.CartService
import com.example.estore.SERVICES.ItemService
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.activity_item.name
import kotlinx.android.synthetic.main.activity_item.price
import kotlinx.android.synthetic.main.activity_item.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class ItemActivity : AppCompatActivity(), Callback<Item> {

    private lateinit var adapter: ImagesAdapter
    private lateinit var shoppingCart: List<CartItem>
    private val tag = "ItemActivity"
    private lateinit var storeItem: StoreItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        adapter = ImagesAdapter(this)
        images.adapter = adapter

        val token = this?.getSharedPreferences("e-store", 0)?.getString("token", null)
        if (token != null) hasToken.visibility = View.VISIBLE

        val id = intent.getIntExtra("item.id", 0)
        if(id > 0){
            ItemService.instance.get(id).enqueue(this)
        }

        CartService.instance.getCart("Bearer $token").enqueue(object : Callback<List<CartItem>>{
            override fun onResponse(call: Call<List<CartItem>>, response: Response<List<CartItem>>) {
                if(response.isSuccessful){
                    if(response.body() != null) shoppingCart = response.body()!!
                }else{
                    val errorMessage = try {
                        "An error occurred: ${response.errorBody()?.string()}"
                    } catch (e: IOException) {
                        "An error occurred: error while decoding the error message."
                    }

                    Toast.makeText(application, "Item is already in the shopping cart.", Toast.LENGTH_SHORT).show()
                    Log.e(tag, errorMessage)
                }
            }

            override fun onFailure(call: Call<List<CartItem>>, t: Throwable) {
                Log.w(tag, "Error: ${t.message}", t)
            }

        })

        toCart.setOnClickListener {

            //Log.d(tag, quantity.text.toString())
            if(quantity.text.isNotEmpty()) {
                var koliko: Int = quantity.text.toString().toInt()
                if (koliko != null) {
                    val listItems = ArrayList<CartItemUpdate>()
                    for (item in shoppingCart) {
                        listItems.add(CartItemUpdate(id = item.pivot.store_item_id, quantity = item.pivot.quantity))
                    }

                    listItems.add(CartItemUpdate(id = storeItem.id, quantity = koliko))
                    CartService.instance.updateCart("Bearer $token", ShoppingCart(shoppingCart = listItems)).enqueue(object :
                            Callback<List<CartItem>> {
                        override fun onResponse(call: Call<List<CartItem>>, response: Response<List<CartItem>>) {
                            if (response.isSuccessful) {
                                Toast.makeText(application, "Item added to cart.", Toast.LENGTH_LONG).show()
                            } else {
                                val errorMessage = try {
                                    "An error occurred: ${response.errorBody()?.string()}"
                                } catch (e: java.io.IOException) {
                                    "An error occurred: error while decoding the error message."
                                }

                                android.widget.Toast.makeText(application, errorMessage, android.widget.Toast.LENGTH_SHORT).show()
                                android.util.Log.e(tag, errorMessage)
                            }
                        }

                        override fun onFailure(call: Call<List<CartItem>>, t: Throwable) {
                            Log.w(tag, "Error: ${t.message}", t)
                        }

                    })
                }
            }
        }
    }

    override fun onResponse(call: Call<Item>, response: Response<Item>) {

        if(response.isSuccessful){
            storeItem = response.body()!!.storeItem
            val images = response.body()!!.pictures

            price.text = storeItem.price.toString()
            name.text = storeItem.name
            description.text = storeItem.description
            val listItems = ArrayList<ItemImage>()
            for(image in images){
                listItems.add(ItemImage(bitmap = null, url = image.image))
            }
            adapter.clear()
            adapter.addAll(listItems)

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

    override fun onFailure(call: Call<Item>, t: Throwable) {
        Log.w(tag, "Error: ${t.message}", t)
    }

    fun getImage(imageUrl: String): Bitmap {
        val url = URL("http://10.0.2.2:8000/storage/storeItems/$imageUrl")
        val connection = url.openConnection() as HttpURLConnection
        connection.connect()
        return BitmapFactory.decodeStream(connection.inputStream)
    }

}