package com.example.l5z1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var nThreads = 0
    private var leftServe = false
    var isPause = false
    var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val sharedPref = getSharedPreferences("gameState", MODE_PRIVATE)
            ball.restoreStateGame(sharedPref)
            leftServe = sharedPref.getBoolean("leftServe", false)
            nThreads = sharedPref.getInt("nThreads",0)
            isRunning = sharedPref.getBoolean("isRunning", false)
            isPause = sharedPref.getBoolean("isPause", false)
            if(!isPause && !isRunning){
                if (leftServe) {
                    serveRight.visibility = View.INVISIBLE
                } else {
                    serveLeft.visibility = View.INVISIBLE
                }
            } else {
                serveLeft.visibility = View.INVISIBLE
                serveRight.visibility = View.INVISIBLE
                pauseButton.text = "Start"
                isPause = true
                isRunning = false
            }
        }
    }

    fun serve(view: View) {
        ball.newServe(leftServe)
        if (leftServe) {
            serveLeft.visibility = View.INVISIBLE
            leftServe = false
        } else {
            serveRight.visibility = View.INVISIBLE
            leftServe = true
        }
        startThread(40)
    }


    fun startThread(time: Long) {
        nThreads++
        isRunning = true
        Thread {
            while(isRunning) {
                Thread.sleep(time)
                runOnUiThread {
                    ball.update()
                }
            }
        }.start()
    }

    fun pause(view: View) {
        if(!isRunning && isPause) {
            pauseButton.text = "Pause"
            nThreads--
            startThread(40)
            for(i in 0 until nThreads) {
                nThreads--
                startThread(100)
            }
            isPause = false
        } else if (isRunning) {
            pauseButton.text = "Start"
            isPause = true
            isRunning = false
        }
    }

    fun endRound() {
        if(leftServe) {
            serveLeft.visibility = View.VISIBLE
        } else {
            serveRight.visibility = View.VISIBLE
        }
        isRunning = false
        nThreads = 0
    }

    fun newGame(view: View) {
        if(!isRunning) {
            pauseButton.text = "Pause"
            ball.newGame(leftServe)
            isPause = false
            isRunning = false
            nThreads = 0
            if (leftServe) {
                serveLeft.visibility = View.VISIBLE
            } else {
                serveRight.visibility = View.VISIBLE
            }
        }
    }

    override fun onStop() {
        super.onStop()

        val sharedPref = getSharedPreferences("gameState", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.putBoolean("isRunning", isRunning)
        editor.putBoolean("isPause", isPause)
        editor.putBoolean("leftServe", leftServe)
        editor.putInt("nThreads",nThreads)
        ball.save(editor)
        editor.commit()
        pauseButton.text = "Start"
        isPause = true
        isRunning = false
    }
}
