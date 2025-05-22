package com.example.monthlies

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signupBtn = findViewById<Button>(R.id.signup_btn)
        val usernameInput = findViewById<EditText>(R.id.user)
        val passwordInput = findViewById<EditText>(R.id.password)

        signupBtn.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Proceed to next screen (e.g., UserHomeActivity or CalendarActivity)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
                finish()
            } else {
                // Optional: Add feedback/toast
                usernameInput.error = "Enter your email/username"
                passwordInput.error = "Enter your password"
            }
        }
    }
}
