package com.example.quizapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.databinding.ActivityMultiplicationBinding
import java.util.*

class Multiplication : AppCompatActivity() {
    private lateinit var multiplicationBinding: ActivityMultiplicationBinding
    private var score = 0
    private var lives = 3
    private val handler = Handler(Looper.getMainLooper())
    private var currentProblem: Pair<Int, Int>? = null
    private var timer: Timer? = null
    private val problemTimeLimit = 5000L // 5 seconds per problem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        multiplicationBinding = ActivityMultiplicationBinding.inflate(layoutInflater)
        setContentView(multiplicationBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        multiplicationBinding.submitAnswerButton.setOnClickListener { checkAnswer() }
        multiplicationBinding.nextButton.setOnClickListener { generateNewProblem() }
        multiplicationBinding.playAgainButton.setOnClickListener { restartGame() }

        startNewGame()
    }

    private fun startNewGame() {
        score = 0
        lives = 3
        updateScoreAndLives()
        generateNewProblem()
    }

    private fun generateNewProblem() {
        currentProblem = Pair((1..10).random(), (1..10).random())
        multiplicationBinding.problemTextView.text = "${currentProblem!!.first} * ${currentProblem!!.second} = ?"
        multiplicationBinding.answerEditText.text.clear()
        multiplicationBinding.submitAnswerButton.visibility = Button.VISIBLE
        multiplicationBinding.nextButton.visibility = Button.GONE
        multiplicationBinding.answerEditText.isEnabled = true

        timer?.cancel()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.post { loseLife() }
            }
        }, problemTimeLimit)
    }

    private fun checkAnswer() {
        timer?.cancel()
        val answer = multiplicationBinding.answerEditText.text.toString().toIntOrNull()
        if (answer == (currentProblem!!.first * currentProblem!!.second)) {
            score++
            updateScoreAndLives()
            multiplicationBinding.submitAnswerButton.visibility = Button.GONE
            multiplicationBinding.nextButton.visibility = Button.VISIBLE
        } else {
            loseLife()
        }
    }

    private fun loseLife() {
        lives--
        updateScoreAndLives()
        if (lives > 0) {
            multiplicationBinding.submitAnswerButton.visibility = Button.GONE
            multiplicationBinding.nextButton.visibility = Button.VISIBLE
        } else {
            endGame()
        }
    }

    private fun updateScoreAndLives() {
        multiplicationBinding.scoreTextView.text = "Score: $score"
        multiplicationBinding.livesTextView.text = "Lives: $lives"
    }

    private fun endGame() {
        multiplicationBinding.problemTextView.text = "Game Over! Your score: $score"
        multiplicationBinding.playAgainButton.visibility = Button.VISIBLE
        multiplicationBinding.submitAnswerButton.isEnabled = false
        multiplicationBinding.nextButton.isEnabled = false
        multiplicationBinding.answerEditText.isEnabled = false
    }

    private fun restartGame() {
        multiplicationBinding.playAgainButton.visibility = Button.GONE
        multiplicationBinding.submitAnswerButton.isEnabled = true
        multiplicationBinding.nextButton.isEnabled = true
        multiplicationBinding.answerEditText.isEnabled = true
        startNewGame()
    }
}
