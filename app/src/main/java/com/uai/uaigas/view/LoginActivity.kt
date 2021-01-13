package com.uai.uaigas.view

import android.content.Context
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
import com.uai.uaigas.model.User
import com.uai.uaigas.service.AuthService
import org.json.JSONObject
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
                var user = User(email = email.toString(), senha = password.toString())
                RetrofitClient.instance.login(user).enqueue(object : Callback<User> {
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(applicationContext, "Ocorreu um erro, tente novamente mais tarde!", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        when {
                            response.isSuccessful -> {
                                response.body()?.let {
                                    AuthService.user = it
                                    saveUser(it)
                                    NavUtils.navigateUpFromSameTask(this@LoginActivity)
                                    Toast.makeText(
                                        applicationContext,
                                        "Bem vindo(a) ${it.nome}!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            response.code() == 400 -> {
                                response.errorBody()?.let {
                                    var resp = it.string()
                                    if (resp.contains("message")) {
                                        resp = JSONObject(resp).getString("message")
                                    }
                                    Toast.makeText(applicationContext, resp, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
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
                            Toast.makeText(applicationContext, "Ocorreu um erro, tente novamente mais tarde!", Toast.LENGTH_LONG).show()
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

    private fun saveUser(user: User) {
        var sharedPref = getSharedPreferences("USER", Context.MODE_PRIVATE).edit()
        sharedPref.putString("id", user.id.toString())
        sharedPref.putString("nome", user.nome)
        sharedPref.putString("email", user.email)
        sharedPref.putBoolean("admin", user.admin)
        sharedPref.putString("fotoUrl", user.fotoUrl)
        sharedPref.commit()
    }
}