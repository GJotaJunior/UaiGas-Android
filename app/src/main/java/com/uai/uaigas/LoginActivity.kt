package com.uai.uaigas

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        var message =
            if (email.text.toString().isNullOrEmpty() || password.text.toString().isNullOrEmpty())
                "Os campos são de preenchimento obrigatório!"
            else
                email.text.toString()

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    fun clearFields(view: View) {
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        email.text.clear()
        password.text.clear()
    }
}