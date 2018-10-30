package com.example.asus.testkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)
    }

    fun testCRUD (v : View)
    {
        val intent = Intent(applicationContext, CRUD::class.java)
        startActivity(intent)
    }
    fun viewData (v : View)
    {
        val intent = Intent(applicationContext, ListData::class.java)
        startActivity(intent)
    }
}

