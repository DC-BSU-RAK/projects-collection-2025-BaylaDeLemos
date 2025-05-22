package com.example.monthlies

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class HomeActivity : AppCompatActivity() {
    private lateinit var symptomBtn: Button
    private lateinit var calendarBtn: MaterialButton
    private lateinit var statisticsBtn: MaterialButton

    @SuppressLint("UseKtx")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Symptom button logic
        symptomBtn = findViewById(R.id.mood)

        symptomBtn.setOnClickListener {
            symptomBtn.backgroundTintList = null
            symptomBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.cal_pink))
            symptomBtn.setTextColor(Color.WHITE)

            val dialogView = layoutInflater.inflate(R.layout.dialog_symptom, null)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create()

            dialog.setOnDismissListener {
                symptomBtn.setBackgroundColor(Color.TRANSPARENT)
                symptomBtn.setTextColor(Color.parseColor("#A4A3A3"))
            }

            val closeBtn = dialogView.findViewById<Button>(R.id.closeBtn)
            closeBtn.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        // Calendar button logic
        calendarBtn = findViewById(R.id.calendar)

        calendarBtn.setOnClickListener {
            calendarBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.cal_pink))
            calendarBtn.setTextColor(Color.WHITE)
            calendarBtn.strokeWidth = 2
            calendarBtn.strokeColor = ColorStateList.valueOf(Color.WHITE)

            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        // Statistics button logic
        statisticsBtn = findViewById(R.id.statistics)

        statisticsBtn.setOnClickListener {
            statisticsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.cal_pink))
            statisticsBtn.setTextColor(Color.WHITE)
            statisticsBtn.strokeWidth = 2
            statisticsBtn.strokeColor = ColorStateList.valueOf(Color.WHITE)

            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("UseKtx")
    override fun onResume() {
        super.onResume()

        // Reset calendar button
        if (::calendarBtn.isInitialized) {
            calendarBtn.setBackgroundColor(Color.TRANSPARENT)
            calendarBtn.setTextColor(Color.parseColor("#A4A3A3"))
            calendarBtn.strokeWidth = 5
            calendarBtn.strokeColor = ColorStateList.valueOf(Color.parseColor("#A4A3A3"))
        }

        // Reset statistics button
        if (::statisticsBtn.isInitialized) {
            statisticsBtn.setBackgroundColor(Color.TRANSPARENT)
            statisticsBtn.setTextColor(Color.parseColor("#A4A3A3"))
            statisticsBtn.strokeWidth = 5
            statisticsBtn.strokeColor = ColorStateList.valueOf(Color.parseColor("#A4A3A3"))
        }
    }
}
