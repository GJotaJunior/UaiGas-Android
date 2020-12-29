package com.uai.uaigas.view

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.uai.uaigas.R
import com.uai.uaigas.api.RetrofitClient
import com.uai.uaigas.dto.EmailDTO
import com.uai.uaigas.model.UserModel
import com.uai.uaigas.service.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        when {
            validator -> Toast.makeText(
                this,
                "Os campos são de preenchimento obrigatório!",
                Toast.LENGTH_SHORT
            )
                .show()
            else -> {
                var user = UserModel(email.toString(), password.toString())
                RetrofitClient.instance.login(user).enqueue(object : Callback<UserModel> {
                    override fun onFailure(call: Call<UserModel>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                        when {
                            response.isSuccessful -> {
                                response.body()?.let {
                                    AuthService.user = it
                                    NavUtils.navigateUpFromSameTask(this@LoginActivity)
                                }
                                Toast.makeText(
                                    applicationContext,
                                    response.body()?.nome,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            response.code() == 400 -> Toast.makeText(
                                applicationContext,
                                "O email e/ou senha não estão corretos!",
                                Toast.LENGTH_SHORT
                            ).show()
                            else -> Toast.makeText(
                                applicationContext,
                                "Ocorreu um erro, tente novamente!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        }

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
                    val emailDto = EmailDTO(editText.text.toString())
                    RetrofitClient.instance.forgot(emailDto).enqueue(object : Callback<Void> {
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            when {
                                response.isSuccessful -> Toast.makeText(
                                    applicationContext,
                                    "Acesse o email enviado para você",
                                    Toast.LENGTH_SHORT
                                ).show()
                                response.code() == 404 -> Toast.makeText(
                                    applicationContext,
                                    "Verifique o email informado e tente novamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                                else -> Toast.makeText(
                                    applicationContext,
                                    "Ocorreu um erro, tente novamente!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
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