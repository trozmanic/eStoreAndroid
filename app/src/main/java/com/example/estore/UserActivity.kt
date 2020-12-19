package com.example.estore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class UserActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        if(savedInstanceState == null){
            val userfrag = UserFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragment, userfrag, "FRAGMENT-ONE")
            transaction.commit()
        }
    }

}