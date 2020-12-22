package com.uai.uaigas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun register(view: View) {
        val email = findViewById<TextInputLayout>(R.id.email).editText?.text
        val name = findViewById<TextInputLayout>(R.id.name).editText?.text
        val password = findViewById<TextInputLayout>(R.id.password).editText?.text
        val confirmPassword = findViewById<TextInputLayout>(R.id.confirm_password).editText?.text

        val validatorFill = email.toString().isNullOrEmpty() || name.toString().isNullOrEmpty()
                || password.toString().isNullOrEmpty() || confirmPassword.toString().isNullOrEmpty()

        val validatorConfirmPassword = !password.toString().contentEquals(confirmPassword.toString())

        var message = ""

        when {
            validatorFill -> message = "Os campos são de preenchimento obrigatório!"
            validatorConfirmPassword -> message = "As senhas precisam ser iguais!"
            else -> message = email.toString()
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun clearFields(view: View) {
        val email = findViewById<TextInputLayout>(R.id.email).editText?.text
        val name = findViewById<TextInputLayout>(R.id.name).editText?.text
        val password = findViewById<TextInputLayout>(R.id.password).editText?.text
        val confirmPassword = findViewById<TextInputLayout>(R.id.confirm_password).editText?.text

        email?.clear()
        name?.clear()
        password?.clear()
        confirmPassword?.clear()
    }
}