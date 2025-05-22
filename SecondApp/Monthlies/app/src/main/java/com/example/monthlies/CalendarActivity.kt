package com.example.monthlies

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val backBtn = findViewById<Button>(R.id.back_to_home)
        backBtn.setOnClickListener {
            // Go back to HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // optional: removes CalendarActivity from stack
            startActivity(intent)
            finish()
        }
    }
}
