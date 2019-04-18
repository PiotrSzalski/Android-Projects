package com.example.l4z2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }

    fun backToList(index: String, rate: String) {
        val intent = Intent()
        intent.putExtra("index", index)
        intent.putExtra("rate", rate)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}