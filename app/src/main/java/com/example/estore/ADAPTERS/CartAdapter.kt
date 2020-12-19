package com.example.estore.ADAPTERS

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.estore.CartItem
import com.example.estore.R
import kotlinx.android.synthetic.main.item_cart.view.*


class CartAdapter(context: Context): ArrayAdapter<CartItem>(context, 0, ArrayList()) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView == null)
            LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        else
            convertView
        var item = getItem(position)
        if(item != null){

            view.name.text = item.name
            view.price.text = item.price.toString()
            view.quantity.text = item.pivot.quantity.toString()

            view.inc.setOnClickListener {
                item.pivot.quantity++
                view.quantity.text = item.pivot.quantity.toString()
            }
            view.dec.setOnClickListener {
                if(item.pivot.quantity > 0) item.pivot.quantity--
                view.quantity.text = item.pivot.quantity.toString()
            }
        }


        return view
    }

    fun dataSet(): List<CartItem>
    {
        val list = ArrayList<CartItem>()
        for(i in 0 until count){
            getItem(i)?.let { list.add(it) }
        }
        return list
    }
}