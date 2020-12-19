package com.example.estore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Toast
import com.example.estore.ADAPTERS.ItemsAdapter
import com.example.estore.SERVICES.ItemService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class MainActivity : AppCompatActivity(), Callback<List<Item>> {

    private lateinit var adapter: ItemsAdapter
    private val tag = "MAIN-ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ItemsAdapter(this)
        items.adapter = adapter

        items.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            val storeItem = adapter.getItem(i)

            if(storeItem != null){
                val intent = Intent(this, ItemActivity::class.java)
                intent.putExtra("item.id", storeItem!!.id)
                startActivity(intent)
            }
        }

        container.setOnRefreshListener {
            ItemService.instance.getAll().enqueue(this)
        }
        ItemService.instance.getAll().enqueue(this)
    }

    override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
        if(response.isSuccessful){

            val adapterItems= ArrayList<MainItem>()
            val items = response.body() ?: emptyList()
            for(item in items){
                val imgUrl = if (item.pictures.size > 0) item.pictures.get(0).image  else ""
                val newItem = MainItem(id = item.storeItem.id, name = item.storeItem.name, price = item.storeItem.price, img = null, imgUrl = imgUrl)
                adapterItems.add(newItem)
            }
            adapter.clear()
            adapter.addAll(adapterItems)

        }else{
            val errorMessage = try {
                "An error occurred: ${response.errorBody()?.string()}"
            } catch (e: IOException) {
                "An error occurred: error while decoding the error message."
            }

            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            Log.e(tag, errorMessage)
        }
        container.isRefreshing = false
    }

    override fun onFailure(call: Call<List<Item>>, t: Throwable) {
        Log.w(tag, "Error: ${t.message}", t)
        container.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
//        if(id == R.id.cart){
//            startActivity(Intent(this, ShoppingCart::class.java))
//            return true
//        }
//        if(id == R.id.login){
//
//        }
        return when(id){
            R.id.cart -> {
                startActivity(Intent(this, ShoppingCartActivity::class.java))
                true
            }
            R.id.login -> {
                startActivity(Intent(this, UserActivity::class.java))
                true
            }
            else -> {
                false
            }
        }
    }
}