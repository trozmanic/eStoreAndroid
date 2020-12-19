package com.example.estore.ADAPTERS

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.estore.HistoryOrderItem
import com.example.estore.OrderHistory
import com.example.estore.R
import kotlinx.android.synthetic.main.item_cart.view.price
import kotlinx.android.synthetic.main.item_order.view.*

class OrdersAdapter(context: Context): ArrayAdapter<OrderHistory>(context, 0, ArrayList()) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView == null)
            LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
        else
            convertView

        var item = getItem(position)
        Log.d("NEVEM", item.toString() )
        if(item != null){
            view.price.text = "Price: ${sumPrice(item.storeItems).toString()}"

            view.date.text = item.order.created_at.toString()

            val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listItems(item.storeItems))
            view.items.adapter = adapter



        }



        return view
    }

    fun listItems(storeItemList: List<HistoryOrderItem>): ArrayList<String>{
        val list = ArrayList<String>()
        for(item in storeItemList){
            list.add("${item.name}    ${item.pivot.primary_price} X ${item.pivot.quantity} ")
        }
        return list
    }


    fun sumPrice(storeItemList: List<HistoryOrderItem>): Double{
        var price = 0.0
        for(item in storeItemList){
            price += (item.pivot.primary_price * item.pivot.quantity)
        }
        return price
    }

}