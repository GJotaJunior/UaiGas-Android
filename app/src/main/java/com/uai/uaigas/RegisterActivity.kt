package com.uai.uaigas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun register(view: View) {
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirm_password)

        var message: String = if (password.text.toString()
                .contentEquals(confirmPassword.text.toString())
        ) email.text.toString() else "As senhas precisam ser iguais!"

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun clearFields(view: View) {
        val email = findViewById<EditText>(R.id.email)
        val name = findViewById<EditText>(R.id.name)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirm_password)

        email.text.clear()
        name.text.clear()
        password.text.clear()
        confirmPassword.text.clear()
    }
}