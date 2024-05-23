package com.example.quizapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.databinding.ActivityAdditionBinding
import java.util.*

class Addition : AppCompatActivity() {
    private lateinit var additionBinding: ActivityAdditionBinding
    private var score = 0
    private var lives = 3
    private val handler = Handler(Looper.getMainLooper())
    private var currentProblem: Pair<Int, Int>? = null
    private var timer: Timer? = null
    private val problemTimeLimit = 5000L // 5 seconds per problem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        additionBinding = ActivityAdditionBinding.inflate(layoutInflater)
        setContentView(additionBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        additionBinding.submitAnswerButton.setOnClickListener { checkAnswer() }
        additionBinding.nextButton.setOnClickListener { generateNewProblem() }
        additionBinding.playAgainButton.setOnClickListener { restartGame() }

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
        additionBinding.problemTextView.text = "${currentProblem!!.first} + ${currentProblem!!.second} = ?"
        additionBinding.answerEditText.text.clear()
        additionBinding.submitAnswerButton.visibility = Button.VISIBLE
        additionBinding.nextButton.visibility = Button.GONE
        additionBinding.answerEditText.isEnabled = true

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
        val answer = additionBinding.answerEditText.text.toString().toIntOrNull()
        if (answer == (currentProblem!!.first + currentProblem!!.second)) {
            score++
            updateScoreAndLives()
            additionBinding.submitAnswerButton.visibility = Button.GONE
            additionBinding.nextButton.visibility = Button.VISIBLE
        } else {
            loseLife()
        }
    }

    private fun loseLife() {
        lives--
        updateScoreAndLives()
        if (lives > 0) {
            additionBinding.submitAnswerButton.visibility = Button.GONE
            additionBinding.nextButton.visibility = Button.VISIBLE
        } else {
            endGame()
        }
    }

    private fun updateScoreAndLives() {
        additionBinding.scoreTextView.text = "Score: $score"
        additionBinding.livesTextView.text = "Lives: $lives"
    }

    private fun endGame() {
        additionBinding.problemTextView.text = "Game Over! Your score: $score"
        additionBinding.playAgainButton.visibility = Button.VISIBLE
        additionBinding.submitAnswerButton.isEnabled = false
        additionBinding.nextButton.isEnabled = false
        additionBinding.answerEditText.isEnabled = false
    }

    private fun restartGame() {
        additionBinding.playAgainButton.visibility = Button.GONE
        additionBinding.submitAnswerButton.isEnabled = true
        additionBinding.nextButton.isEnabled = true
        additionBinding.answerEditText.isEnabled = true
        startNewGame()
    }
}
