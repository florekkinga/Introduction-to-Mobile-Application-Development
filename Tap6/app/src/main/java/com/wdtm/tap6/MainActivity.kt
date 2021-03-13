package com.wdtm.tap6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    internal lateinit var button: Button
    internal lateinit var scoreLabel: TextView
    internal lateinit var timeLabel: TextView

    private var score = 0
        set(value){
            field = value
            val text = "Score: {field}"
            scoreLabel.text = text
        }
        get(){
            println(field)
            return field
        }

    private var gameStarted = false
    private var multiplier = 1
    private lateinit var timer: CountDownTimer
    private var countDownTime: Long = 15000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        scoreLabel = findViewById(R.id.scoreLabel)
        timeLabel = findViewById(R.id.timeLabel)

        button.setOnClickListener { buttonClicked() }

//        setScore(0)
        score = 0
        setTime(0)

        timer = object : CountDownTimer(15000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                setTime(millisUntilFinished/1000)
                multiplier = 1 + (multiplier*(countDownTime-millisUntilFinished)/1000).toInt()
            }

            override fun onFinish() {
                gameOver()
            }
        }
    }

    private fun gameOver() {
        gameStarted = false
        setButtonLabel(getString(R.string.buttonLabelText))
        val message = getString(R.string.message, score)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun startGame() {
        gameStarted = true
        score = 0
        setButtonLabel(getString(R.string.buttonLabelTextGameStarted))
        timer.start()
    }

    private fun buttonClicked() {
        if (gameStarted) {
            score += multiplier
//            setScore(score)
        } else {
            startGame()
        }
    }

//    private fun setScore(score: Int) {
//        val text = getString(R.string.scoreLabelText, score)
//        // "Score: " + score.toString()
//        scoreLabel.text = text
//    }

    private fun setTime(time: Long) {
        val text = getString(R.string.timeLabelText, time)
        timeLabel.text = text
    }

    private fun setButtonLabel(s: String) {
        button.text = s
    }

}