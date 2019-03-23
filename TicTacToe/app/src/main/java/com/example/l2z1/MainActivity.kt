package com.example.l2z1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var move = false
    private var board = arrayOf<Array<Button>>()
    private var end = false
    private var moves = 0
    private var bot = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lm.visibility = View.INVISIBLE
        reset.visibility = View.INVISIBLE
        info.visibility = View.INVISIBLE

        var dimension = 0
        if(Resources.getSystem().displayMetrics.widthPixels >= Resources.getSystem().displayMetrics.heightPixels) {
            var statusBarHeight = 0
            var navigationBarHeight = 0
            var resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = resources.getDimensionPixelSize(resourceId)
            }
            resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                navigationBarHeight = resources.getDimensionPixelSize(resourceId)
            }
            dimension = (Resources.getSystem().displayMetrics.heightPixels - statusBarHeight - navigationBarHeight) / 5
        } else {
            dimension = Resources.getSystem().displayMetrics.widthPixels / 5
        }

        val params = button1.layoutParams
        params.width = dimension
        params.height = dimension

        for (i in 0..4) {
            var array = arrayOf<Button>()
            var l = lm.getChildAt(i) as LinearLayout
            for (j in 0..4) {
                array += (lm.getChildAt(j) as LinearLayout).getChildAt(i) as Button
                l.getChildAt(j).layoutParams = params
            }
            board += array
        }
    }

    fun choiceOpponent(view: View) {
        bot = view != playerOpponent
        playerOpponent.visibility = View.INVISIBLE
        botOpponent.visibility = View.INVISIBLE
        lm.visibility = View.VISIBLE
        reset.visibility = View.VISIBLE
        info.visibility = View.VISIBLE
    }

    fun click(view: View) {
        if(!end) {
            if( (view as Button).text == "" && move) {
                view.text = "X"
                move = false
                info.text = "Ruch \ngracza: O"
                checkWin(view)
            } else if ( view.text == "") {
                view.text = "O"
                move = true
                info.text = "Ruch \ngracza: X"
                checkWin(view)
                if(bot && !end) {
                    botMove()
                }
            }
        }
    }

    fun newGame(view:View) {
        for(i in 0 until 5) {
            for(j in 0 until 5) {
                board[i][j].text = ""
                board[i][j].setTextColor(Color.BLACK)
            }
        }
        end = false
        moves = 0
        if(Random.nextInt(2) == 0) {
            move = true
            info.text = "Ruch \ngracza: X"
            if(bot) {
                botMove()
            }
        } else {
            move = false
            info.text = "Ruch \ngracza: O"
        }
    }

    private fun checkWin(view: View) {
        moves++
        var x= 0
        var y= 0
        for(i in 0 until 5) {
            if(lm.getChildAt(i) == (view as Button).parent) {
                y = i
                for(j in 0 until 5) {
                    if((lm.getChildAt(i) as LinearLayout).getChildAt(j) == view) {
                        x = j
                    }
                }
            }
        }
        for(i in 0 until 4) {
            if(board[i][y].text != board[i+1][y].text) {
                break
            }
            if(i == 3) {
                end = true
                for(j in 0..4) {
                    board[j][y].setTextColor(Color.WHITE)
                }
            }
        }
        for(i in 0 until 4) {
            if(board[x][i].text != board[x][i+1].text) {
                break
            }
            if(i == 3) {
                end = true
                for(j in 0..4) {
                    board[x][j].setTextColor(Color.WHITE)
                }
            }
        }
        if(x == y) {
            for(i in 0 until 4) {
                if(board[i][i].text != board[i+1][i+1].text) {
                    break
                }
                if(i == 3) {
                    end = true
                    for(j in 0..4) {
                        board[j][j].setTextColor(Color.WHITE)
                    }
                }
            }
        }
        if(x + y == 4) {
            for(i in 0 until 4) {
                if(board[4-i][i].text != board[3-i][i+1].text) {
                    break
                }
                if(i == 3) {
                    end = true
                    for(j in 0..4) {
                        board[4-j][j].setTextColor(Color.WHITE)
                    }
                }
            }
        }
        if(end) {
            info.text = "Wygrał: "+ (view as Button).text
        } else if (moves == 25) {
            info.text = "Remis"
            end = true
        }
    }

    private fun botMove() {
                var wages = IntArray(25)
                var listOfWages = ArrayList<Pair>()
                for(y in 0..4) {
                    for(x in 0..4){
                        val wage = when {
                            board[x][y].text == "X" -> 1
                            board[x][y].text == "O" -> -1
                            else -> 0
                        }
                        for (j in 0..4) {
                            wages[y * 5 + j] += wage
                            wages[j * 5 + x] += wage
                        }
                        if (y == x) {
                            for (j in 0..4) {
                                wages[j * 5 + j] += wage
                            }
                        }
                        if (x + y == 4) {
                            for (j in 0..4) {
                                wages[(4 - j) * 5 + j] += wage
                            }
                        }
                    }
                }
                for(y in 0..4) {
                    for(x in 0..4) {
                        if(board[x][y].text == "") {
                            listOfWages.add(Pair(y * 5 + x, wages[y * 5 + x]))
                        }
                    }
        }
        listOfWages.sortWith(compareBy {it.wage})
        click(board[listOfWages[0].i%5][listOfWages[0].i/5])
    }

    class Pair(var i: Int, var wage: Int)
}
