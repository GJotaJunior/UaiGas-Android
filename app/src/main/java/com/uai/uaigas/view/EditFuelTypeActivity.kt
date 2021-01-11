package com.uai.uaigas.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import com.uai.uaigas.R
import com.uai.uaigas.api.RetrofitClient
import com.uai.uaigas.model.TipoCombustivel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditFuelTypeActivity : AppCompatActivity() {

    lateinit var descriptionInput: TextInputLayout
    lateinit var saveBtn: Button
    var id: String? = null
    var description: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_fuel_type)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        descriptionInput = findViewById(R.id.description_fuel_type)
        saveBtn = findViewById(R.id.btn_save)

        id = intent.getStringExtra("id")
        description = intent.getStringExtra("description")

        id?.let {
            descriptionInput.editText?.setText(description)
            saveBtn.setOnClickListener { update(it) }
        } ?: run {
            saveBtn.setOnClickListener { save(it) }
        }
    }

    fun save(view: View) {
        val validatorEmpty = descriptionInput.editText?.text.isNullOrEmpty()

        when {
            validatorEmpty -> Toast.makeText(
                applicationContext,
                "O campo é de preenchimento obrigatório",
                Toast.LENGTH_SHORT
            ).show()
            else ->
                RetrofitClient.instance.createFuelType(
                    TipoCombustivel(descriptionInput.editText?.text.toString())
                ).enqueue(object : Callback<TipoCombustivel> {
                    override fun onFailure(call: Call<TipoCombustivel>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Ocorreu um erro, tente novamente mais tarde!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<TipoCombustivel>,
                        response: Response<TipoCombustivel>
                    ) {
                        when {
                            response.isSuccessful -> {
                                Toast.makeText(
                                    applicationContext,
                                    "Operação realizada com sucesso!",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                startActivity(Intent(applicationContext, FuelTypeList::class.java))
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

    fun update(view: View) {
        val validatorEmpty = descriptionInput.editText?.text.isNullOrEmpty()

        when {
            validatorEmpty -> Toast.makeText(
                applicationContext,
                "O campo é de preenchimento obrigatório",
                Toast.LENGTH_SHORT
            ).show()
            else ->
                RetrofitClient.instance.updateFuelType(
                    id?.toLong()!!,
                    TipoCombustivel(
                        descriptionInput.editText?.text.toString(),
                        id?.toLong()
                    )
                ).enqueue(object : Callback<TipoCombustivel> {
                    override fun onFailure(call: Call<TipoCombustivel>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Ocorreu um erro, tente novamente mais tarde!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<TipoCombustivel>,
                        response: Response<TipoCombustivel>
                    ) {
                        when {
                            response.isSuccessful -> {
                                Toast.makeText(
                                    applicationContext,
                                    "Operação realizada com sucesso!",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                startActivity(Intent(applicationContext, FuelTypeList::class.java))
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
