package com.example.estore

import android.graphics.Bitmap
import java.util.*


data class Item(var storeItem: StoreItem, var pictures: List<Picture>)

data class StoreItem(var id: Int, var name: String, var price: Float, var description: String)

data class Picture(var id: Int, var image: String, var store_item_id: Int)

data class MainItem(var id: Int, var name: String, var price: Float, var img: Bitmap?, var imgUrl: String)

data class Login(var email: String, var password: String)

data class LoginResponse(var token: String, var user: User)

data class User(var id:Int, var name:String, var lastname: String, var email: String, var role:String?)

data class CartItem(var id: Int, var price: Float, var name: String, var description: String, var pivot: Pivot)

data class Pivot(var shopping_cart_id: Int, var store_item_id: Int, var quantity: Int)

data class ItemImage(var bitmap: Bitmap?, var url: String)

data class UserUpdate(var name: String?, var lastname: String?, var pasword: String?)

data class CartItemUpdate(var id: Int, var quantity: Int)

data class ShoppingCart(var shoppingCart: List<CartItemUpdate>)

data class OrderItem( var id: Int, var quantity: Int, var price: Float)

data class Order(var orderItems: List<OrderItem>)

data class HistoryPivot(var order_id: Int, var store_item_id: Int, var quantity: Int, var primary_price: Float)

data class HistoryOrderItem(var id: Int, var price: Float, var name: String, var description: String, var pivot: HistoryPivot)
data class OrderStatus(var created_at: Date, var staus: String)
data class OrderHistory(var storeItems: List<HistoryOrderItem>, var status: OrderStatus)

data class Orders(var orders: List<OrderHistory>)