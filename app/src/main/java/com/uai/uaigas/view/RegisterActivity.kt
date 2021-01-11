package com.uai.uaigas.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import com.google.android.material.textfield.TextInputLayout
import com.uai.uaigas.R
import com.uai.uaigas.api.RetrofitClient
import com.uai.uaigas.model.User
import com.uai.uaigas.service.AuthService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun register(view: View) {
        val email = findViewById<TextInputLayout>(R.id.email).editText?.text
        val name = findViewById<TextInputLayout>(R.id.name).editText?.text
        val password = findViewById<TextInputLayout>(R.id.password).editText?.text
        val confirmPassword = findViewById<TextInputLayout>(R.id.confirm_password).editText?.text

        val validatorFill = email.toString().isNullOrEmpty() || name.toString().isNullOrEmpty()
                || password.toString().isNullOrEmpty() || confirmPassword.toString().isNullOrEmpty()

        val validatorConfirmPassword =
            !password.toString().contentEquals(confirmPassword.toString())

        when {
            validatorFill -> Toast.makeText(
                this,
                "Os campos são de preenchimento obrigatório!",
                Toast.LENGTH_SHORT
            ).show()
            validatorConfirmPassword -> Toast.makeText(
                this,
                "As senhas precisam ser iguais!",
                Toast.LENGTH_SHORT
            ).show()
            else -> {
                var user: User =
                    User(
                        nome = name.toString(),
                        email = email.toString(),
                        senha = password.toString()
                    )
                RetrofitClient.instance.createUser(user).enqueue(object : Callback<User> {
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(applicationContext, "Ocorreu um erro, tente novamente mais tarde!", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        when {
                            response.isSuccessful -> response.body()?.let {
                                AuthService.user = it
                                NavUtils.navigateUpFromSameTask(this@RegisterActivity)
                                Toast.makeText(
                                    applicationContext,
                                    "Bem vindo(a) ${it.nome}!",
                                    Toast.LENGTH_SHORT
                                ).show()
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
                        }
                    }
                })
            }
        }
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