package com.example.l4z2

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.android.synthetic.main.photo_list.*
import java.io.File


class PhotoList : Fragment() {

    private lateinit var act : MainActivity
    private lateinit var list : ArrayList<Photo>
    lateinit var adapter : ListViewArrayAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.photo_list, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 2 && resultCode == RESULT_OK) {
            val intent = Intent(activity, AddPhoto::class.java)
            intent.putExtra("name", act.imageFilePath)
            startActivityForResult(intent,3)
        }
        if(data != null) {
            if (requestCode == 1) {
                val index = data.getStringExtra("index").toInt()
                val rate = data.getStringExtra("rate").toFloat()
                list[index].rate = rate
                list.sortByDescending { it.rate }
            }
            if(requestCode == 3) {
                val desc = data.getStringExtra("desc")
                val rate = data.getStringExtra("rate").toFloat()
                list.add(Photo(act.imageFilePath,desc,rate,1))
                list.sortByDescending { it.rate }
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        act = activity as MainActivity
        list = act.list
        adapter = ListViewArrayAdapter(activity!!,list)
        listView.adapter = adapter
        listView.setOnItemClickListener { _,_, index, _ ->
            onSelectedPhoto(index)
        }
        addButton.setOnClickListener{ addPhoto(it) }
        listView.setOnItemLongClickListener { _,_, index, _ ->
            if(list[index].camera == 1) {
                val file = File(list[index].name)
                file.delete()
            }
            list.removeAt(index)
            adapter.notifyDataSetChanged()
            true
        }
    }

    private fun onSelectedPhoto(index: Int) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val intent = Intent(activity, MainActivity2::class.java)
            intent.putExtra("index", index.toString())
            intent.putExtra("name", list[index].name)
            intent.putExtra("description", list[index].descript)
            intent.putExtra("rate", list[index].rate.toString())
            startActivityForResult(intent,1)
        } else {
            val frag = fragmentManager!!.findFragmentById(R.id.detailsFragment) as DetailsFragment
            if(list[index].camera == 0) {
                frag.image.setImageResource(act.resources.getIdentifier( list[index].name,"drawable",act.packageName))
            } else if (list[index].camera == 1){
                val bmpFactoryOptions = BitmapFactory.Options()
                bmpFactoryOptions.inJustDecodeBounds = false
                val imageBitmap = BitmapFactory.decodeFile(list[index].name,bmpFactoryOptions)
                frag.image.setImageBitmap(imageBitmap)
            }
            frag.description.text = list[index].descript
            frag.rate.rating = list[index].rate
            act.index = index
            frag.rate.setIsIndicator(false)
            frag.rateView.visibility = View.VISIBLE
        }
    }

    private fun addPhoto(view: View) {
        var imageFile = File.createTempFile("JPEG_picture_",".jpg",act.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        var imageFileUri = FileProvider.getUriForFile(act,"com.example.l4z2",imageFile)
        act.imageFilePath = imageFile.path
        val it = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        it.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri)
        startActivityForResult(it, 2)
    }

}