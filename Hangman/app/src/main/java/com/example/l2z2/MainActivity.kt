package com.example.l2z2

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var word = ""
    private var qword = ""
    private var n = 0
    private var end = false
    private val r = Random()
    private var array = arrayOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        array = resources.getStringArray(R.array.words)
        startNewGame()
    }

    fun reset(view:View) {
        for(i in 0 until keyboard.childCount) {
            for(j in 0 until (keyboard.getChildAt(i) as LinearLayout).childCount) {
                (keyboard.getChildAt(i) as LinearLayout).getChildAt(j).visibility = View.VISIBLE
            }
        }
        startNewGame()
    }

    fun letter(view: View) {
        if(!end) {
            view.visibility = View.INVISIBLE
            guessLetter(findViewById<Button>(view.id).text.single())
        }
    }

    private fun guessLetter(c: Char) {
        var guess = false
        for(i in 0 until word.length) {
            if(word[i] == c) {
                qword = qword.substring(0,i)+c+qword.substring(i+1);
                guess = true
            }
        }
        if(guess) {
            if(qword == word) {
                guessWord.text = "Wygrałeś! Prawidłowe hasło to: $word."
                end = true
            } else {
                guessWord.text = ""
                for(i in 0 until word.length) {
                    guessWord.append(qword[i].toString())
                    guessWord.append(" ")
                }
            }
        } else {
            n++
            image.setImageResource(resources.getIdentifier("wisielec$n","drawable",packageName))
            if(n == 9) {
                guessWord.text = "Przegrałeś! Prawidłowe hasło to: $word."
                end = true
            }
        }
    }

    private fun startNewGame() {
        word = array[r.nextInt(array.size)].toUpperCase()
        qword = ""
        guessWord.text = ""
        n = 0
        end = false
        image.setImageResource(R.drawable.wisielec0)
        for(i in 0 until word.length) {
            qword += "_"
            guessWord.append("_ ")
        }
    }
}