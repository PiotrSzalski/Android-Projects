package com.example.l4z2

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.add_photo.*

class AddPhoto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_photo)

        val bmpFactoryOptions = BitmapFactory.Options()
        bmpFactoryOptions.inJustDecodeBounds = false
        val imageBitmap = BitmapFactory.decodeFile(intent.getStringExtra("name"), bmpFactoryOptions)
        image.setImageBitmap(imageBitmap)
    }

    fun addPhoto(view: View) {
        val intent = Intent()
        intent.putExtra("desc", description.text.toString())
        intent.putExtra("rate", rate.rating.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}