package com.example.l4z2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

class ListViewArrayAdapter (context: Context, var data: ArrayList<Photo>) : ArrayAdapter<Photo>(context, R.layout.photo_item, data){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.photo_item, parent, false)
            holder = ViewHolder()
            holder.image = view.findViewById(R.id.imageView1) as ImageView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }
        if(data[position].camera == 0) {
            holder.image.setImageResource(context.resources.getIdentifier( data[position].name,"drawable",context.packageName))
        } else if (data[position].camera == 1) {
            val bmpFactoryOptions = BitmapFactory.Options()
            bmpFactoryOptions.inJustDecodeBounds = false
            val imageBitmap = BitmapFactory.decodeFile(data[position].name, bmpFactoryOptions)
            holder.image.setImageBitmap(imageBitmap)
        }
        return view
    }

    private class ViewHolder {
        lateinit var image: ImageView
    }
}