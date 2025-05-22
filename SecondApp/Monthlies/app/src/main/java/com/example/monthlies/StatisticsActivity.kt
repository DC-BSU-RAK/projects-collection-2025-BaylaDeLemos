package com.example.monthlies

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class StatisticsActivity : AppCompatActivity() {

    @SuppressLint("UseKtx")
    private fun toggleCardSelection(
        card: CardView,
        icon: ImageView,
        label: TextView,
        selected: Boolean
    ) {
        if (selected) {
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            icon.setColorFilter(Color.parseColor("#A4A3A3"))
            label.setTextColor(Color.parseColor("#A4A3A3"))
        } else {
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.cal_pink))
            icon.setColorFilter(Color.WHITE)
            label.setTextColor(Color.WHITE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val backBtn = findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish() // Go back to HomeActivity
        }

        // Card setup
        var lightSelected = false
        var mediumSelected = false
        var heavySelected = false

        val lightCard = findViewById<CardView>(R.id.lightCard)
        val lightIcon = findViewById<ImageView>(R.id.lightIcon)
        val lightLabel = findViewById<TextView>(R.id.lightLabel)

        val mediumCard = findViewById<CardView>(R.id.mediumCard)
        val mediumIcon = findViewById<ImageView>(R.id.mediumIcon)
        val mediumLabel = findViewById<TextView>(R.id.mediumLabel)

        val heavyCard = findViewById<CardView>(R.id.heavyCard)
        val heavyIcon = findViewById<ImageView>(R.id.heavyIcon)
        val heavyLabel = findViewById<TextView>(R.id.heavyLabel)

        lightCard.setOnClickListener {
            lightSelected = !lightSelected
            toggleCardSelection(lightCard, lightIcon, lightLabel, !lightSelected)
        }

        mediumCard.setOnClickListener {
            mediumSelected = !mediumSelected
            toggleCardSelection(mediumCard, mediumIcon, mediumLabel, !mediumSelected)
        }

        heavyCard.setOnClickListener {
            heavySelected = !heavySelected
            toggleCardSelection(heavyCard, heavyIcon, heavyLabel, !heavySelected)
        }
    }
}
