package com.example.l3z1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ListViewArrayAdapter (context: Context, var data: ArrayList<Task>) : ArrayAdapter<Task>(context, R.layout.list_view_item, data){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_view_item, parent, false)
            holder = ViewHolder()
            holder.image = view.findViewById(R.id.im) as ImageView
            holder.priority1 = view.findViewById(R.id.priorityColor) as ImageView
            holder.priority2 = view.findViewById(R.id.priorityColor2) as ImageView
            holder.name = view.findViewById(R.id.t1) as TextView
            holder.date = view.findViewById(R.id.t2) as TextView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }
        holder.name.text = data[position].name
        holder.date.text = data[position].date.toString().substring(0,10)+" "+data[position].date.toString().substring(11)
        val pri = data[position].priority
        when (pri) {
            1 -> {
                holder.priority1.setImageResource(android.R.color.holo_green_light)
                holder.priority2.setImageResource(android.R.color.holo_green_light)
            }
            2 -> {
                holder.priority1.setImageResource(android.R.color.holo_orange_light)
                holder.priority2.setImageResource(android.R.color.holo_orange_light)
            }
            else -> {
                holder.priority1.setImageResource(android.R.color.holo_red_light)
                holder.priority2.setImageResource(android.R.color.holo_red_light)
            }
        }
        val pic = data[position].picture
        when (pic) {
            1 -> holder.image.setImageResource(R.drawable.bills)
            2 -> holder.image.setImageResource(R.drawable.home)
            3 -> holder.image.setImageResource(R.drawable.mail)
            4 -> holder.image.setImageResource(R.drawable.meeting)
            5 -> holder.image.setImageResource(R.drawable.shool)
            else -> holder.image.setImageResource(R.drawable.work)
        }
        return view
    }

    private class ViewHolder {
        lateinit var image: ImageView
        lateinit var priority1: ImageView
        lateinit var priority2: ImageView
        lateinit var name: TextView
        lateinit var date: TextView
    }
}