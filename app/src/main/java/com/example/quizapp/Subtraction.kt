package com.example.quizapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.databinding.ActivitySubtractionBinding
import java.util.*

class Subtraction : AppCompatActivity() {
    private lateinit var subtractionBinding: ActivitySubtractionBinding
    private var score = 0
    private var lives = 3
    private val handler = Handler(Looper.getMainLooper())
    private var currentProblem: Pair<Int, Int>? = null
    private var timer: Timer? = null
    private val problemTimeLimit = 5000L // 5 seconds per problem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        subtractionBinding = ActivitySubtractionBinding.inflate(layoutInflater)
        setContentView(subtractionBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        subtractionBinding.submitAnswerButton.setOnClickListener { checkAnswer() }
        subtractionBinding.nextButton.setOnClickListener { generateNewProblem() }
        subtractionBinding.playAgainButton.setOnClickListener { restartGame() }

        startNewGame()
    }

    private fun startNewGame() {
        score = 0
        lives = 3
        updateScoreAndLives()
        generateNewProblem()
    }

    private fun generateNewProblem() {
        val first = (1..20).random()
        val second = (1..first).random() // Ensure the second number is not greater than the first
        currentProblem = Pair(first, second)
        subtractionBinding.problemTextView.text = "${currentProblem!!.first} - ${currentProblem!!.second} = ?"
        subtractionBinding.answerEditText.text.clear()
        subtractionBinding.submitAnswerButton.visibility = Button.VISIBLE
        subtractionBinding.nextButton.visibility = Button.GONE
        subtractionBinding.answerEditText.isEnabled = true

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
        val answer = subtractionBinding.answerEditText.text.toString().toIntOrNull()
        if (answer == (currentProblem!!.first - currentProblem!!.second)) {
            score++
            updateScoreAndLives()
            subtractionBinding.submitAnswerButton.visibility = Button.GONE
            subtractionBinding.nextButton.visibility = Button.VISIBLE
        } else {
            loseLife()
        }
    }

    private fun loseLife() {
        lives--
        updateScoreAndLives()
        if (lives > 0) {
            subtractionBinding.submitAnswerButton.visibility = Button.GONE
            subtractionBinding.nextButton.visibility = Button.VISIBLE
        } else {
            endGame()
        }
    }

    private fun updateScoreAndLives() {
        subtractionBinding.scoreTextView.text = "Score: $score"
        subtractionBinding.livesTextView.text = "Lives: $lives"
    }

    private fun endGame() {
        subtractionBinding.problemTextView.text = "Game Over! Your score: $score"
        subtractionBinding.playAgainButton.visibility = Button.VISIBLE
        subtractionBinding.submitAnswerButton.isEnabled = false
        subtractionBinding.nextButton.isEnabled = false
        subtractionBinding.answerEditText.isEnabled = false
    }

    private fun restartGame() {
        subtractionBinding.playAgainButton.visibility = Button.GONE
        subtractionBinding.submitAnswerButton.isEnabled = true
        subtractionBinding.nextButton.isEnabled = true
        subtractionBinding.answerEditText.isEnabled = true
        startNewGame()
    }
}
