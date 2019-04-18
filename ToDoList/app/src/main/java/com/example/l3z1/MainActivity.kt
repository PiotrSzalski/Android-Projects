package com.example.l3z1

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.util.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.concurrent.fixedRateTimer


class MainActivity : AppCompatActivity() {

    private var taskList = ArrayList<Task>()
    private var arrayAdapter: ListViewArrayAdapter? = null
    private var notificationManager: NotificationManager? = null
    private var imagedesc = false
    private var datedesc = false
    private var prioritydesc = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arrayAdapter = ListViewArrayAdapter(this, taskList)
        listView.adapter = arrayAdapter
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        listView.setOnItemLongClickListener { _,_, index, _ ->
            taskList.removeAt(index)
            arrayAdapter!!.notifyDataSetChanged()
            true
        }
        listView.setOnItemClickListener { _, _, index, _ ->
            val t = taskList[index]
            val editIntent = Intent(this, AddingActivity::class.java)
            editIntent.putExtra("index",index.toString())
            editIntent.putExtra("name", t.name)
            editIntent.putExtra("day", t.date.dayOfMonth.toString())
            editIntent.putExtra("month", t.date.monthValue.toString())
            editIntent.putExtra("year", t.date.year.toString())
            editIntent.putExtra("hour", t.date.hour.toString())
            editIntent.putExtra("minute", t.date.minute.toString())
            editIntent.putExtra("priority", t.priority.toString())
            editIntent.putExtra("image", t.picture.toString())
            startActivityForResult(editIntent, 2)
        }
        if(savedInstanceState == null){
            readList()
            createNotificationChannel()
            fixedRateTimer("notify-timer", initialDelay = 1000, period = 60000) {
                checkEvent()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkEvent() {
        var massage = ""
        var wasEvent = false
        for(i in 0 until taskList.size) {
            if(taskList[i].date.withSecond(0).withNano(0) == LocalDateTime.now().withSecond(0).withNano(0).plusHours(2)) {
                if(wasEvent) {
                    massage = "$massage, "
                }
                massage += taskList[i].name
                wasEvent = true
            }
        }
        if (wasEvent) {
            sendNotification(massage)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(name: String) {
        val notification = Notification.Builder(this, "l3z1")
            .setContentTitle("Zdarzenie z ToDo List")
            .setContentText(name)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setChannelId("l3z1")
            .build()

        notificationManager?.notify(101, notification)
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        taskName.setText("")
        if (data != null) {
            val name = data.getStringExtra("name")
            val day = data.getStringExtra("day").toInt()
            val month = data.getStringExtra("month").toInt()
            val year = data.getStringExtra("year").toInt()
            val hour = data.getStringExtra("hour").toInt()
            val minute = data.getStringExtra("minute").toInt()
            val priority = data.getStringExtra("priority").toInt()
            val image = data.getStringExtra("image").toInt()
            if (requestCode == 1) {
                taskList.add(Task(name!!, LocalDateTime.of(year, month, day, hour, minute), priority, image))
            } else if (requestCode == 2) {
                val index = data.getStringExtra("index").toInt()
                taskList[index].name = name
                taskList[index].priority = priority
                taskList[index].picture = image
                val date = LocalDateTime.of(year, month, day, hour, minute)
                taskList[index].date = date
            }
            arrayAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList("list",taskList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        taskList.addAll(savedInstanceState?.getParcelableArrayList("list")!!)
        arrayAdapter!!.notifyDataSetChanged()
    }

    fun sort(view: View) {
        when (view) {
            pictureSortButton -> {
                imagedesc = when {
                    imagedesc -> {
                        taskList.sortByDescending { it.picture }
                        false
                    }
                    else -> {
                        taskList.sortBy { it.picture }
                        true
                    }
                }
            }
            prioritySortButton -> {
                prioritydesc = when {
                    prioritydesc -> {
                        taskList.sortByDescending { it.priority }
                        false
                    }
                    else -> {
                        taskList.sortBy { it.priority }
                        true
                    }
                }
            }
            else -> {
                datedesc = when {
                    datedesc -> {
                        taskList.sortByDescending { it.date }
                        false
                    }
                    else -> {
                        taskList.sortBy { it.date }
                        true
                    }
                }
            }
        }
        arrayAdapter!!.notifyDataSetChanged()
    }

    fun addTask(view: View) {
        val addIntent = Intent(this, AddingActivity::class.java)
        addIntent.putExtra("name", taskName.text.toString())
        startActivityForResult(addIntent, 1)
    }

    override fun onStop() {
        super.onStop()
        writeList()
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun writeList() {
        var fileContents = ""
        for(i in 0 until taskList.size) {
            fileContents = fileContents+taskList[i].name+";"+taskList[i].date.year+";"+taskList[i].date.monthValue+";"+
                    taskList[i].date.dayOfMonth+";"+taskList[i].date.hour+";"+taskList[i].date.minute+";"+taskList[i].priority+";"+
                        taskList[i].picture+"\n"
        }
        this.openFileOutput("tasks", Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun readList() {
        if (!File(filesDir,"tasks").exists()) {
            return
        }
        val fis = this.openFileInput("tasks")
        val isr = InputStreamReader(fis)
        val bufferedReader = BufferedReader(isr)
        var line = bufferedReader.readLine()
        while ( line != null) {
            var t = line.split(";")
            taskList.add(Task(t[0], LocalDateTime.of(t[1].toInt(),t[2].toInt(),t[3].toInt(),t[4].toInt(),t[5].toInt()),t[6].toInt(),t[7].toInt()))
            line = bufferedReader.readLine()
        }
        arrayAdapter!!.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel("l3z1", "Notify", NotificationManager.IMPORTANCE_LOW)
        notificationManager?.createNotificationChannel(channel)
    }
}
