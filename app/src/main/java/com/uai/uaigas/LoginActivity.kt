package com.uai.uaigas

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    fun btnResetPassword(view: View) {
        val textInputLayout = TextInputLayout(this)
        textInputLayout.placeholderText = getString(R.string.placeholder_email)
        val editText = TextInputEditText(textInputLayout.context)
        editText.hint = "Informe seu email"
        editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        var layout = FrameLayout(this)
        layout.addView(editText)
        layout.setPadding(45, 15, 45, 0)

        val alert = MaterialAlertDialogBuilder(this)
        alert.setTitle("Resetar senha: ")
            .setView(layout)
            .setPositiveButton("Recuperar", DialogInterface.OnClickListener { dialog, which ->
                run {
                    val email = editText.text.toString()
                    Toast.makeText(this, "Acesse o email enviado para você", Toast.LENGTH_SHORT)
                        .show();
                }
            }).setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            }).show()
    }

    fun clearFields(view: View) {
        val email = findViewById<TextInputLayout>(R.id.email).editText?.text
        val password = findViewById<TextInputLayout>(R.id.password).editText?.text

        email?.clear()
        password?.clear()
    }
}