package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.databinding.ActivityCalcualtionsBinding

class Calcualtions : AppCompatActivity() {
    lateinit var  calcualtionsBinding: ActivityCalcualtionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        calcualtionsBinding = ActivityCalcualtionsBinding.inflate(layoutInflater)
        setContentView(calcualtionsBinding.root)

        val button = findViewById<Button>(R.id.btnAddition)
        button.setOnClickListener {
            val intent = Intent(this, Addition::class.java)
            startActivity(intent)
        }

        val buttons = findViewById<Button>(R.id.btnSubtraction)
        buttons.setOnClickListener {
            val intent = Intent(this, Subtraction::class.java)
            startActivity(intent)
        }

        val buttonm = findViewById<Button>(R.id.btnMultiplications)
        buttonm.setOnClickListener {
            val intent = Intent(this, Multiplication::class.java)
            startActivity(intent)
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}