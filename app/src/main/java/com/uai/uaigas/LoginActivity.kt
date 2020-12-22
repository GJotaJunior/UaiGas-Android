package com.uai.uaigas

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        val email = findViewById<TextInputLayout>(R.id.email).editText?.text
        val password = findViewById<TextInputLayout>(R.id.password).editText?.text

        val validator = email.toString().isNullOrEmpty() || password.toString().isNullOrEmpty()

        var message =
            if (validator)
                "Os campos são de preenchimento obrigatório!"
            else
                email.toString()

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    fun clearFields(view: View) {
        val email = findViewById<TextInputLayout>(R.id.email).editText?.text
        val password = findViewById<TextInputLayout>(R.id.password).editText?.text

        email?.clear()
        password?.clear()
    }
}