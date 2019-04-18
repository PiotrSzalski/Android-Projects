package com.example.l4z2

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.details_fragment.*

class DetailsFragment : Fragment() {

    private var index = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity!!.intent.getStringExtra("index") != null) {
            index = activity!!.intent.getStringExtra("index")
            rateView.visibility = View.VISIBLE
            val name = activity!!.intent.getStringExtra("name")
            val desc = activity!!.intent.getStringExtra("description")
            val rating = activity!!.intent.getStringExtra("rate").toFloat()
            setData(name,desc,rating)
        }
        saveRate.setOnClickListener{ saveRate() }
    }

    private fun setData(name: String, desc: String, rating: Float) {
        val act = activity as MainActivity2
        image.setImageResource(act.resources.getIdentifier(name,"drawable",act.packageName))
        if(name.contains("photo")) {
            image.setImageResource(act.resources.getIdentifier( name,"drawable",act.packageName))
        } else {
            val bmpFactoryOptions = BitmapFactory.Options()
            bmpFactoryOptions.inJustDecodeBounds = false
            val imageBitmap = BitmapFactory.decodeFile(name,bmpFactoryOptions)
            image.setImageBitmap(imageBitmap)
        }
        description.text = desc
        rate.rating = rating
    }

    private fun saveRate() {
        if(index == "") {
            val act = activity as MainActivity
            act.list[act.index].rate = rate.rating
            rate.setIsIndicator(true)
            act.list.sortByDescending { it.rate }
            val frag = fragmentManager!!.findFragmentById(R.id.photo_list) as PhotoList
            frag.adapter.notifyDataSetChanged()
        } else {
            (activity as MainActivity2).backToList(index,rate.rating.toString())
        }
    }
}