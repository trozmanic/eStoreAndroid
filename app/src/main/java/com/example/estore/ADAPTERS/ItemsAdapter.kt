package com.example.estore.ADAPTERS

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.estore.MainItem
import com.example.estore.R
import kotlinx.android.synthetic.main.item_element.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.net.URL

class ItemsAdapter(context: Context): ArrayAdapter<MainItem>(context, 0, ArrayList())  {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView == null)
            LayoutInflater.from(context).inflate(R.layout.item_element, parent, false)
        else
            convertView
        var item = getItem(position)

        if (item != null) {
            if (item.img == null && item.imgUrl != "") {

                val bitmapAwait = GlobalScope.async {
                    getImage(item.imgUrl)
                }

                runBlocking {
                    item.img = bitmapAwait.await()
                    view.image.setImageBitmap(item.img)
                }
            } else {
                view.image.setImageBitmap(item.img)
            }
        }

        view.price.text = "${item?.price.toString()} €"
        view.name.text = item?.name.toString()

        return view
    }

    fun getImage(imageUrl: String): Bitmap? {
        val url = URL("http://10.0.2.2:8000/storage/storeItems/$imageUrl")
        val connection = url.openConnection() as HttpURLConnection
        connection.connect()
        val stream = connection.inputStream
        return if (stream != null) BitmapFactory.decodeStream(stream) else null
    }
}