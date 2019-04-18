package com.example.l3z1

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.adding_activity.*
import java.util.*


class AddingActivity : AppCompatActivity() {

    private val c = Calendar.getInstance()
    private var year = c.get(Calendar.YEAR)
    private var month = c.get(Calendar.MONTH) + 1
    private var day = c.get(Calendar.DAY_OF_MONTH)
    private var hour = (c.get(Calendar.HOUR_OF_DAY) + 2) % 24
    private var minute = c.get(Calendar.MINUTE)
    private var image = 1
    private var index = -1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adding_activity)

        if(intent.getStringExtra("index") != null) {
            addButton.text = "Edytuj zadanie"
            index = intent.getStringExtra("index").toInt()
            taskName.setText(intent.getStringExtra("name"))
            year = intent.getStringExtra("year").toInt()
            month = intent.getStringExtra("month").toInt()
            day = intent.getStringExtra("day").toInt()
            hour = intent.getStringExtra("hour").toInt()
            minute = intent.getStringExtra("minute").toInt()
            dataView.text = "$day-$month-$year"
            timeView.text = "$hour:$minute"
            seekBar.progress = intent.getStringExtra("priority").toInt() - 1
            val p = intent.getStringExtra("image").toInt()
            when (p) {
                1 -> imageSelect(image1)
                2 -> imageSelect(image2)
                3 -> imageSelect(image3)
                4 -> imageSelect(image4)
                5 -> imageSelect(image5)
                else -> imageSelect(image6)
            }
        } else {
            dataView.text = "$day-$month-$year"
            timeView.text = "$hour:$minute"
            taskName.setText(intent.getStringExtra("name"))
        }
    }

    fun showCalendar(view: View) {
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            year = selectedYear
            month = selectedMonth + 1
            day = selectedDay
            dataView.text = "$day-$month-$year"
        }, year, month - 1, day)
        dpd.show()
    }

    fun showClock(view: View) {
        val mTimePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            hour = selectedHour
            minute = selectedMinute
            timeView.text = "$hour:$minute"
        }, hour, minute, true)
        mTimePicker.show()
    }

    fun imageSelect(view: View) {
        image1.setBackgroundColor(Color.WHITE)
        image2.setBackgroundColor(Color.WHITE)
        image3.setBackgroundColor(Color.WHITE)
        image4.setBackgroundColor(Color.WHITE)
        image5.setBackgroundColor(Color.WHITE)
        image6.setBackgroundColor(Color.WHITE)
        when (view) {
            image1 -> {
                image = 1
                image1.setBackgroundColor(Color.MAGENTA)
            }
            image2 -> {
                image = 2
                image2.setBackgroundColor(Color.MAGENTA)
            }
            image3 -> {
                image = 3
                image3.setBackgroundColor(Color.MAGENTA)
            }
            image4 -> {
                image = 4
                image4.setBackgroundColor(Color.MAGENTA)
            }
            image5 -> {
                image = 5
                image5.setBackgroundColor(Color.MAGENTA)
            }
            else -> {
                image = 6
                image6.setBackgroundColor(Color.MAGENTA)
            }
        }
    }

    fun addUpdateTask(view: View) {
        if(taskName.text.toString() == "") {
            setResult(Activity.RESULT_OK, null)
        } else {
            val taskIntent = Intent()
            taskIntent.putExtra("name", taskName.text.toString())
            taskIntent.putExtra("day", day.toString())
            taskIntent.putExtra("month", month.toString())
            taskIntent.putExtra("year", year.toString())
            taskIntent.putExtra("hour", hour.toString())
            taskIntent.putExtra("minute", minute.toString())
            taskIntent.putExtra("image", image.toString())
            taskIntent.putExtra("priority", (seekBar.progress + 1).toString())
            if(index == -1) {
                setResult(Activity.RESULT_OK, taskIntent)
            } else {
                taskIntent.putExtra("index", index.toString())
                setResult(Activity.RESULT_OK, taskIntent)
            }
        }
        finish()
    }
}