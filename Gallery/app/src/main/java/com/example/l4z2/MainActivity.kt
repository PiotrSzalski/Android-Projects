package com.example.l4z2

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    val list = ArrayList<Photo>()
    var index = -1
    var imageFilePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val array = resources.getStringArray(R.array.descript)
            for (i in 0 until 15) {
                list.add(Photo("photo$i", array[i], 0.0f, 0))
            }
            val sharedPref = getSharedPreferences("photos", Context.MODE_PRIVATE)
            val n = sharedPref.getInt("number", 0)
            for (i in 0 until n) {
                list.add(Photo(sharedPref.getString("photo$i", "")!!, sharedPref.getString("desc$i", "")!!, 0.0f, 1))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList("list",list)
        outState?.putString("path",imageFilePath)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        list.addAll(savedInstanceState?.getParcelableArrayList("list")!!)
        imageFilePath = savedInstanceState?.getString("path")
    }

    override fun onStop() {
        super.onStop()

        var n = 0
        val sharedPref = getSharedPreferences("photos", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        for(i in 0 until  list.size) {
            if(list[i].camera == 1) {
                editor.putString("photo$n",list[i].name)
                editor.putString("desc$n",list[i].descript)
                n++
            }
        }
        editor.putInt("number",n)
        editor.commit()
    }
}
