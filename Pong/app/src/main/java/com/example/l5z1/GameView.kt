package com.example.l5z1

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class GameView(context: Context, attrs: AttributeSet)
    : View(context, attrs) {

    private var rect1 : RectF? = null
    private var rect2 : RectF? = null
    private lateinit var ball : RectF
    private lateinit var net : RectF
    private var ballX = 0f
    private var ballY = 0f
    private var dx = -5f
    private var dy = 5f
    private val random = Random()
    private val white = Paint()
    private val green = Paint()
    private var wasEndRound = false
    private var leftPoints = 0
    private var rightPoints = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        if(rect1 == null || rect2 == null) {
            ball = RectF(ballX, ballY,ballX+30f,ballY+30f)
            net = RectF(width/2-2f,0f,width/2+2f,height.toFloat())
            rect1 = RectF(50f, (height/2-height/6).toFloat(),80f, (height/2+height/6).toFloat())
            rect2 = RectF(width-80f, (height/2-height/6).toFloat(),width-50f, (height/2+height/6).toFloat())
            if(ballX == 0f) {
                ballX = width/2 - 15f
            }
            if(ballY == 0f) {
                ballY = height/2 - 15f
            }
        }
        ball.set(ballX, ballY,ballX+30f,ballY+30f)
        with(canvas) {
            drawRect(net,green)
            drawRect( rect1!!, green)
            drawRect( rect2!!, green)
            drawOval(ball, white)
        }

        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!(context as MainActivity).isPause) {
            val pointer1: Int = event.getPointerId(0)
            val pointer2: Int

            val (x1: Float, y1: Float) = event.findPointerIndex(pointer1).let { pointerIndex ->
                event.getX(pointerIndex) to event.getY(pointerIndex)
            }
            if(x1 < width/2) {
                rect1!!.set(50f,y1-height/6,80f,y1+height/6)
            } else {
                rect2!!.set(rect2!!.left,y1-height/6,rect2!!.right,y1+height/6)
            }
            if(event.pointerCount == 2) {
                pointer2 = event.getPointerId(1)
                val (x2: Float, y2: Float) = event.findPointerIndex(pointer2).let { pointerIndex ->
                    event.getX(pointerIndex) to event.getY(pointerIndex)
                }
                if(x2 < this.width/2) {
                    rect1!!.set(50f,y2-height/6,80f,y2+height/6)
                } else {
                    rect2!!.set(rect2!!.left,y2-height/6,rect2!!.right,y2+height/6)
                }
            }
        }
        return true
    }

    fun update() {
        ballX+=dx
        ballY+=dy

        if(ballX < 80) {
            if(ballX <= 0 && !wasEndRound) {
                wasEndRound = true
                rightPoints++
                if(rightPoints == 21) {
                    with(context as MainActivity) {
                        points.text = "Right Player Won"
                        serveLeft.visibility = View.INVISIBLE
                        serveRight.visibility = View.INVISIBLE
                        isRunning = false
                    }
                } else {
                    with(context as MainActivity) {
                        endRound()
                        points.text = "$leftPoints:$rightPoints"
                    }
                }
            } else if(ballX > 70 && ballY+15f > rect1!!.top && ballY+15f < rect1!!.bottom) {
                dx = -dx
                dy = if(dy < 0) {
                    -computeDy(rect1!!)
                } else {
                    computeDy(rect1!!)
                }
                (context as MainActivity).startThread(100)
            }
        } else if (ballX+30f > rect2!!.left && !wasEndRound) {
            if(ballX+30f >= width) {
                wasEndRound = true
                leftPoints++
                if(leftPoints == 21) {
                    with(context as MainActivity) {
                        points.text = "Left Player Won"
                        serveLeft.visibility = View.INVISIBLE
                        serveRight.visibility = View.INVISIBLE
                        isRunning = false
                    }
                } else {
                    with(context as MainActivity) {
                        endRound()
                        points.text = "$leftPoints:$rightPoints"
                    }
                }
            } else if(ballX+20f < rect2!!.left && ballY+15f > rect2!!.top && ballY+15f < rect2!!.bottom) {
                dx = -dx
                dy = if(dy < 0) {
                    -computeDy(rect2!!)
                } else {
                    computeDy(rect2!!)
                }
                (context as MainActivity).startThread(100)
            }
        }
        if (ballY <= 0 || ballY+30f >= height) {
            dy = -dy
        }
        postInvalidate()
    }

    private fun computeDy(rect: RectF) : Float {
        val d = rect.height()/7
        val y = ballY+15f
        return if(rect.top + 3*d < y && rect.bottom - 3*d > y) {
            0f
        } else if(rect.top + 2*d < y && rect.bottom - 2*d > y) {

            1.7f
        } else if(rect.top + d < y && rect.bottom - d > y) {
            3.4f
        } else {
            5f
        }
    }

    fun newServe(leftServe: Boolean) {
        wasEndRound = false
        ballX = width/2 - 15f
        ballY = height/2 - 15f
        dx = when {
            leftServe -> 5f
            else -> -5f
        }
        dy = when {
            random.nextBoolean() -> 5f
            else -> -5f
        }
    }

    fun newGame(leftServe: Boolean) {
        wasEndRound = false
        ballX = width/2 - 15f
        ballY = height/2 - 15f
        dx = when {
            leftServe -> 5f
            else -> -5f
        }
        dy = when {
            random.nextBoolean() -> 5f
            else -> -5f
        }
        leftPoints = 0
        rightPoints = 0
        (context as MainActivity).points.text = "0:0"
    }

    fun save(editor: SharedPreferences.Editor) {
        editor.putInt("leftP",leftPoints)
        editor.putInt("rightP",rightPoints)
        editor.putFloat("dx",dx)
        editor.putFloat("dy",dy)
        if(ballX <= 0 || ballX+30f > rect2!!.left) {
            editor.putFloat("ballY",height/2 - 15f)
            editor.putFloat("ballX",width/2 - 15f)
        } else {
            editor.putFloat("ballX",ballX)
            editor.putFloat("ballY",ballY)
        }
    }

    fun restoreStateGame(pref: SharedPreferences) {
        green.setARGB(255,0, 255, 0)
        white.setARGB(255,255,255,255)
        leftPoints = pref.getInt("leftP",0)
        rightPoints = pref.getInt("rightP",0)
        (context as MainActivity).points.text = "$leftPoints:$rightPoints"
        dx = pref.getFloat("dx", 5f)
        dy = pref.getFloat("dy",5f)
        ballX = pref.getFloat("ballX", 0f)
        ballY = pref.getFloat("ballY", 0f)
        postInvalidate()
    }
}